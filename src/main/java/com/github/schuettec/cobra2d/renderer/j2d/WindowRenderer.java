package com.github.schuettec.cobra2d.renderer.j2d;

import static java.util.Objects.isNull;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.github.schuettec.cobra2d.benchmark.Benchmarker;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.renderer.Renderer;

public class WindowRenderer implements Renderer {

	/**
	 *
	 */
	private static final long serialVersionUID = 2790433653469935308L;
	private final int CORRECTION_X = 0;
	private final int CORRECTION_Y = 0;

	private final int NUM_BUFFERS = 2;

	private GraphicsDevice gd;

	private VolatileImage worldView;
	protected Graphics2D worldViewGraphics;

	// private boolean initialized = false;

	private int resolutionX, resolutionY, bitDepth, refreshRate;

	private boolean fullscreen;

	private boolean drawEntityLines = false;
	private boolean drawEntityPoints = false;
	private boolean drawEntities = true;

	private boolean cursorVisible = true;
	private Cobra2DEngine engine;

	protected Benchmarker benchmarkAfterRendering;

	private boolean drawEntityCenterPoint;
	private JFrame frame;

	@Override
	public void render() {
		BufferStrategy bufferStrategy = frame.getBufferStrategy();
		if (isNull(bufferStrategy)) {
			frame.createBufferStrategy(WindowRenderer.this.NUM_BUFFERS);
			bufferStrategy = frame.getBufferStrategy();
		}

		Graphics2D bufferGraphics = (Graphics2D) bufferStrategy.getDrawGraphics();

		// Clear now the screen buffer
		bufferGraphics.setColor(Color.BLACK);
		bufferGraphics.fillRect(0, 0, this.resolutionX, this.resolutionY);

		bufferGraphics.setColor(Color.WHITE);
		bufferGraphics.drawLine(0, 0, 800, 800);

		Map map = engine.getMap();
		Set<Camera> cameras = map.getCameras();
		for (Camera camera : cameras) {
			List<Collision> capturedEntities = map.getCameraCollision(camera);
			camera.render(bufferGraphics, map, capturedEntities);
		}

		// Dispose the graphics
		bufferGraphics.dispose();

		// Display the buffer
		bufferStrategy.show();
		Toolkit.getDefaultToolkit()
		    .sync();
	}

	public WindowRenderer() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					WindowRenderer.this.frame = new JFrame();
				}
			});
		} catch (Exception e) {
			throw new RuntimeException("Cannot construct the UI of Java2D renderer", e);
		}
	}

	@Override
	public void initializeRenderer(final Cobra2DEngine engine, final int resolutionX, final int resolutionY,
	    final int bitDepth, final int refreshRate, final boolean fullscreen) throws RendererException {

		this.engine = engine;
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
		this.bitDepth = bitDepth;
		this.refreshRate = refreshRate;
		this.fullscreen = fullscreen;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {

					frame.addWindowListener(new WindowListener() {

						@Override
						public void windowOpened(final WindowEvent e) {

						}

						@Override
						public void windowIconified(final WindowEvent e) {

						}

						@Override
						public void windowDeiconified(final WindowEvent e) {

						}

						@Override
						public void windowDeactivated(final WindowEvent e) {

						}

						@Override
						public void windowClosing(final WindowEvent e) {
							engine.shutdownEngine();
						}

						@Override
						public void windowClosed(final WindowEvent e) {

						}

						@Override
						public void windowActivated(final WindowEvent e) {

						}
					});

					createWorldViewBuffer(resolutionX, resolutionY);

					if (fullscreen) {
						frame.setUndecorated(true);
					}

					frame.setBackground(Color.BLACK);
					frame.setForeground(Color.BLACK);

					frame.setTitle("Cobra2DEngine");
					frame.setFocusable(true);

					if (fullscreen) {
						final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
						gd = ge.getDefaultScreenDevice();
						// setUndecorated(true); // no menu bar, borders, etc.
						frame.setIgnoreRepaint(true);
						// turn off paint events since doing active rendering
						frame.setResizable(false);
						if (!gd.isFullScreenSupported()) {
							throw new RendererException("Fullscreenmode is not supported by vido card.");
						}
						gd.setFullScreenWindow(frame); // switch on FSEM
						// we can now adjust the display modes, if we wish
						try {
							DisplayMode displayMode = engine.getDisplayMode(resolutionX, resolutionY, bitDepth, refreshRate);
							gd.setDisplayMode(displayMode);

						} catch (final Exception e) {
							finish();
							throw new RendererException("Cannot establish display mode! " + resolutionX + "x" + resolutionY, e);
						}

					} else {

						// setIgnoreRepaint(true);
						// GraphicsDevice graphicsDevice = getGraphicsConfiguration()
						// .getDevice();
						// graphicsDevice.setFullScreenWindow(this);
						// graphicsDevice.setDisplayMode(new DisplayMode(xResolution,
						// yResolution, bitDepht , refreshRate));
						// frame.setResizable(false);
						frame.setSize(resolutionX + CORRECTION_X + 5, resolutionY + CORRECTION_Y + 5);
						frame.requestFocus();
						frame.setVisible(true);
					}
				}
			});
		} catch (Exception e) {
			throw new RendererException("Cannot initialize the Java 2D renderer.", e);
		}

	}

	private void createWorldViewBuffer(final int resolutionX, final int resolutionY) {
		this.worldView = RenderToolkit.createVolatileImage(resolutionX, resolutionY);
		this.worldViewGraphics = (Graphics2D) this.worldView.getGraphics();
	}

	public void setWorldViewSize(final Dimension size) {
		this.worldView = RenderToolkit.createVolatileImage(size.width, size.height);

	}

	public Dimension getWorldViewSize() {
		return new Dimension(this.worldView.getWidth(), this.worldView.getHeight());
	}

	@Override
	public void finish() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// UNINITIALIZE RENDERING MODE
				if (WindowRenderer.this.fullscreen) {
					final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
					WindowRenderer.this.gd = ge.getDefaultScreenDevice();
					WindowRenderer.this.gd.setFullScreenWindow(null);
				}

				frame.setVisible(false);
				frame.dispose();
			}

		});
	}

	public Dimension getScreenSize() {
		return new Dimension(this.resolutionX, this.resolutionY);
	}

	public int getResolutionX() {
		return this.resolutionX;
	}

	public int getResolutionY() {
		return this.resolutionY;
	}

	public int getBitDepth() {
		return this.bitDepth;
	}

	public int getRefreshRate() {
		return this.refreshRate;
	}

	public boolean isFullscreen() {
		return this.fullscreen;
	}

	public boolean isDrawEntityLines() {
		return this.drawEntityLines;
	}

	public void setDrawEntityLines(final boolean drawEntityLines) {
		this.drawEntityLines = drawEntityLines;
	}

	public boolean isDrawEntityPoints() {
		return this.drawEntityPoints;
	}

	public void setDrawEntityPoints(final boolean drawEntityPoints) {
		this.drawEntityPoints = drawEntityPoints;
	}

	public boolean isDrawEntities() {
		return this.drawEntities;
	}

	public void setDrawEntities(final boolean drawEntities) {
		this.drawEntities = drawEntities;

	}

	public void showCursor(final boolean visible) {
		this.cursorVisible = visible;
		if (visible) {
			frame.setCursor(Cursor.getDefaultCursor());
		} else {
			frame.setCursor(frame.getToolkit()
			    .createCustomCursor(new ImageIcon("").getImage(), new java.awt.Point(0, 0), "No Cursor"));
		}

	}

	public void setCursorImage(final String texturAddress) {
		final VolatileImage image = this.engine.getImageMemory()
		    .getImage(texturAddress);
		if (image == null) {
			System.out.println("Cannot set cursor to image with texture address '" + texturAddress + "'.");
			return;
		}
		this.cursorVisible = true;
		frame.setCursor(frame.getToolkit()
		    .createCustomCursor(image.getSnapshot(), new java.awt.Point(0, 0), "CustomCursor"));
	}

	public boolean isCursorVisible() {
		return this.cursorVisible;
	}

	public Benchmarker getBenchmarker() {
		return this.benchmarkAfterRendering;
	}

	public boolean isDrawEntityCenterPoint() {
		return this.drawEntityCenterPoint;
	}

	public void setDrawEntityCenterPoint(boolean b) {
		this.drawEntityCenterPoint = b;
	}

	@Override
	public void addKeyListener(KeyListener listener) {
		frame.addKeyListener(listener);
	}

	@Override
	public void removeKeyListener(KeyListener listener) {
		frame.removeKeyListener(listener);
	}

	@Override
	public void addMouseListener(MouseListener listener) {
		frame.addMouseListener(listener);
	}

	@Override
	public void removeMouseListener(MouseListener listener) {
		frame.removeMouseListener(listener);
	}

	@Override
	public void addMouseMotionListener(MouseMotionListener listener) {
		frame.addMouseMotionListener(listener);
	}

	@Override
	public void removeMouseMotionListener(MouseMotionListener listener) {
		frame.removeMouseMotionListener(listener);
	}

}

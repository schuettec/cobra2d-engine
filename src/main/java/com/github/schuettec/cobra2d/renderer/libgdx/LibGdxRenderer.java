package com.github.schuettec.cobra2d.renderer.libgdx;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.renderer.Renderer;
import com.github.schuettec.cobra2d.renderer.RendererException;

public class LibGdxRenderer extends ApplicationAdapter implements Renderer {

	private Map map;
	private Lwjgl3Application application;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private int resolutionX;
	private int resolutionY;
	private LibGdxRendererAccess rendererAccess;

	@Override
	public void initializeRenderer(Cobra2DEngine engine, int resolutionX, int resolutionY, int bitDepth, int refreshRate,
	    boolean fullscreen) throws RendererException {
		this.map = engine.getMap();
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setInitialBackgroundColor(Color.BLACK);
		config.setHdpiMode(HdpiMode.Pixels);
		if (fullscreen) {
			DisplayMode mode = getDisplayMode(resolutionX, resolutionY, bitDepth, refreshRate);
			config.setFullscreenMode(mode);
		} else {
			config.setWindowedMode(resolutionX, resolutionY);
		}
		config.setForegroundFPS(60);
		config.setTitle("Cobra2D Engine");
		this.application = new Lwjgl3Application(this, config);
	}

	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(resolutionX, resolutionY);
		camera.viewportWidth = resolutionX;
		camera.viewportHeight = resolutionY;

		this.rendererAccess = new LibGdxRendererAccess(this);
	}

	@Override
	public void render() {
		map.update();
		camera.update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		rendererAccess.fillCircle(0f, 0f, 10f, com.github.schuettec.cobra2d.renderer.common.Color.YELLOW);
		rendererAccess.fillCircle(0f, 50f, 10f, com.github.schuettec.cobra2d.renderer.common.Color.YELLOW);
		rendererAccess.fillCircle(0f, 100f, 10f, com.github.schuettec.cobra2d.renderer.common.Color.YELLOW);
		rendererAccess.fillCircle(0f, 150f, 10f, com.github.schuettec.cobra2d.renderer.common.Color.YELLOW);
		rendererAccess.fillCircle(0f, 200f, 10f, com.github.schuettec.cobra2d.renderer.common.Color.YELLOW);

		rendererAccess.fillCircle(50f, 0f, 10f, com.github.schuettec.cobra2d.renderer.common.Color.YELLOW);
		rendererAccess.fillCircle(100f, 50f, 10f, com.github.schuettec.cobra2d.renderer.common.Color.YELLOW);
		rendererAccess.fillCircle(150f, 100f, 10f, com.github.schuettec.cobra2d.renderer.common.Color.YELLOW);
		rendererAccess.fillCircle(200f, 150f, 10f, com.github.schuettec.cobra2d.renderer.common.Color.YELLOW);
		rendererAccess.fillCircle(250f, 200f, 10f, com.github.schuettec.cobra2d.renderer.common.Color.YELLOW);

		Set<Camera> cameras = map.getCameras();
		for (Camera camera : cameras) {
			List<Collision> capturedEntities = map.getCameraCollision(camera);
			camera.render(rendererAccess, map, capturedEntities);
		}

	}

	@Override
	public void finish() {

	}

	@Override
	public void addKeyListener(KeyListener singlePlayerController) {

	}

	@Override
	public void removeKeyListener(KeyListener singlePlayerController) {

	}

	@Override
	public void addMouseListener(MouseListener listener) {

	}

	@Override
	public void removeMouseListener(MouseListener listener) {

	}

	@Override
	public void addMouseMotionListener(MouseMotionListener listener) {

	}

	@Override
	public void removeMouseMotionListener(MouseMotionListener listener) {

	}

	public DisplayMode getDisplayMode(int resolutionX, int resolutionY, int bitDepth, int refreshRate) {
		Monitor primary = Lwjgl3ApplicationConfiguration.getPrimaryMonitor();
		DisplayMode[] availableModes = Lwjgl3ApplicationConfiguration.getDisplayModes(primary);
		for (DisplayMode mode : availableModes) {
			System.out.println(mode.toString());
			if (mode.width == resolutionX && mode.height == resolutionY
			    && ((mode.bitsPerPixel == bitDepth) || mode.bitsPerPixel == -1) && mode.refreshRate == refreshRate) {
				return mode;
			}
		}
		throw new RuntimeException("Cannot find display mode: " + resolutionX + "x" + resolutionY + ":" + refreshRate
		    + "hz at " + bitDepth + " bit depth.");
	}

	ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

}

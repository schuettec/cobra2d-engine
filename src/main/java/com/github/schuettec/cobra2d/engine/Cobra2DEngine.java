package com.github.schuettec.cobra2d.engine;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.network.server.Cobra2DServer;
import com.github.schuettec.cobra2d.renderer.Renderer;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.RendererType;
import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxRenderer;
import com.github.schuettec.cobra2d.resource.RessourceUtil;
import com.github.schuettec.cobra2d.resource.URLClasspathHandler;
import com.github.schuettec.cobra2d.resource.URLInstallDirectoryHandler;
import com.github.schuettec.cobra2d.resource.URLResourceTypeHandler;
import com.github.schuettec.cobra2d.resource.URLStreamHandlerRegistry;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

public class Cobra2DEngine {

	private Cobra2DProperties cobra2DConfig;
	private Renderer renderer;
	private Cobra2DWorld world;

	private Map<String, URL> textures;
	private RendererType rendererType;
	private int refreshRate;
	private int resolutionX;
	private int resolutionY;
	private int bitDepth;
	private boolean fullscreen;
	private int tcpPort;
	private int udpPort;

	public Cobra2DEngine(final Properties properties) {
		super();
		this.cobra2DConfig = new Cobra2DProperties(properties);
		ResourceLocation resourceLocation = cobra2DConfig.getResourceLocation();
		setupEnvironment(resourceLocation);
		this.rendererType = cobra2DConfig.getRendererType();

		createRendererAndController();

		boolean doMapUpdate = cobra2DConfig.isDoMapUpdate();
		this.world = new Cobra2DWorld(this, doMapUpdate);
		this.textures = new Hashtable<>();
	}

	private void createRendererAndController() {
		if (RendererType.LIBGDX.equals(rendererType)) {
			this.renderer = createRenderer(rendererType);
		} else if (RendererType.DEDICATED_SERVER.equals(rendererType)) {
			this.renderer = new Cobra2DServer();
		} else {
			this.renderer = null;
		}
	}

	public void initialize() {
		this.refreshRate = cobra2DConfig.getRefreshRate();
		this.resolutionX = cobra2DConfig.getResolutionX();
		this.resolutionY = cobra2DConfig.getResolutionY();
		this.bitDepth = cobra2DConfig.getBitDepth();
		this.fullscreen = cobra2DConfig.getFullscreen();
		this.tcpPort = cobra2DConfig.getTcpPort();
		this.udpPort = cobra2DConfig.getUdpPort();

		renderer.initializeRenderer(this);
	}

	public void start() {
		this.renderer.start();
	}

	private Renderer createRenderer(RendererType rendererType) {
		Renderer renderer = null;
		if (RendererType.LIBGDX.equals(rendererType)) {
			renderer = new LibGdxRenderer();
		}
		return renderer;
	}

	public Cobra2DWorld getWorld() {
		return this.world;
	}

	public void shutdownEngine() {
		this.renderer.finish();
	}

	public DisplayMode getDisplayMode(int resolutionX, int resolutionY, int bitDepth, int refreshRate) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice display = ge.getDefaultScreenDevice();
		DisplayMode[] availableModes = display.getDisplayModes();
		for (DisplayMode mode : availableModes) {
			System.out.println(mode.toString());
			if (mode.getWidth() == resolutionX && mode.getHeight() == resolutionY
			    && ((mode.getBitDepth() == bitDepth) || mode.getBitDepth() == -1) && mode.getRefreshRate() == refreshRate) {
				return mode;
			}
		}
		throw new RuntimeException("Cannot find display mode: " + resolutionX + "x" + resolutionY + ":" + refreshRate
		    + "hz at " + bitDepth + " bit depth.");
	}

	/**
	 * Use this static method at the beginning of your application to setup the JVM
	 * Properties used by this engine. Use the resource type to determine where
	 * resources are loaded from.
	 */
	private void setupEnvironment(ResourceLocation resourceLocation) {
		// Prepare VM configurations:
		URLStreamHandlerRegistry registry = new URLStreamHandlerRegistry();
		registry.addHandler("resource", new URLResourceTypeHandler(resourceLocation.toString()));
		registry.addHandler("classpath", new URLClasspathHandler());
		registry.addHandler("install-dir", new URLInstallDirectoryHandler());
		URL.setURLStreamHandlerFactory(registry);
	}

	public void addImage(String address, URL ressourceURL) {
		this.textures.put(address, ressourceURL);
	}

	public void addEntity(List<? extends Entity> entities) {
		entities.stream()
		    .forEach(e -> {
			    world.addEntity(e);
		    });
	}

	public void addEntity(Entity... entities) {
		world.addEntity(entities);
	}

	public void addEntity(Entity entity) {
		world.addEntity(entity);
	}

	public void removeEntity(Entity... entities) {
		world.removeEntity(entities);
	}

	public void removeEntity(Entity entity) {
		world.removeEntity(entity);
	}

	public void setCameraForInput(Camera camera) {
		world.setCameraForInput(camera);
	}

	public Renderer getRenderer() {
		return renderer;
	}

	/**
	 * This is a util method to get the size of a texture before the renderer was started. This method reads the specified
	 * texture and analyzes the size.
	 * 
	 * Note: This method requires, that the texture was added to the engine before.
	 * 
	 * Note: Do not use this method during rendering. Use {@link RendererAccess} for
	 * efficient renderer interaction.
	 * 
	 * @param textureId The texture id added to the engine before.
	 * @return Returns the dimension.
	 */
	public Dimension dimensionOf(String textureId) {
		if (this.textures.containsKey(textureId)) {
			URL url = this.textures.get(textureId);
			try (InputStream ressource = RessourceUtil.getRessource(url)) {
				BufferedImage img = ImageIO.read(ressource);
				return new Dimension(img.getWidth(), img.getHeight());
			} catch (IOException e) {
				throw new RuntimeException(
				    "Texture with id " + textureId + " and URL " + url.toString() + " could not be loaded.", e);
			}
		} else {
			throw new RuntimeException(
			    "Texture with id " + textureId + " not found. Add the texture to engine before calling this method!");
		}
	}

	public RendererType getRendererType() {
		return rendererType;
	}

	public int getRefreshRate() {
		return refreshRate;
	}

	public int getResolutionX() {
		return resolutionX;
	}

	public int getResolutionY() {
		return resolutionY;
	}

	public Map<String, URL> getTextures() {
		return textures;
	}

	public int getBitDepth() {
		return bitDepth;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public int getUdpPort() {
		return udpPort;
	}

}

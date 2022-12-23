package com.github.schuettec.cobra2d.engine;

import static java.util.Objects.nonNull;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.VolatileImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.controller.MockController;
import com.github.schuettec.cobra2d.controller.SinglePlayerController;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.renderer.Renderer;
import com.github.schuettec.cobra2d.renderer.RendererType;
import com.github.schuettec.cobra2d.renderer.j2d.WindowRenderer;
import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxRenderer;
import com.github.schuettec.cobra2d.resource.Animation;
import com.github.schuettec.cobra2d.resource.AnimationMemory;
import com.github.schuettec.cobra2d.resource.ImageMemory;
import com.github.schuettec.cobra2d.resource.ImageMemoryException;
import com.github.schuettec.cobra2d.resource.URLClasspathHandler;
import com.github.schuettec.cobra2d.resource.URLInstallDirectoryHandler;
import com.github.schuettec.cobra2d.resource.URLResourceTypeHandler;
import com.github.schuettec.cobra2d.resource.URLStreamHandlerRegistry;

public class Cobra2DEngine {

	private Cobra2DProperties cobra2DConfig;
	private ImageMemory imageMemory;
	private AnimationMemory animationMemory;
	private Renderer renderer;
	private Map map;
	private Controller controller;
	private ActiveWorldUpdater worldUpdater;

	public Cobra2DEngine(final Properties properties) {
		super();
		this.cobra2DConfig = new Cobra2DProperties(properties);
	}

	public void initialize() {
		RendererType rendererType = cobra2DConfig.getRendererType();
		int refreshRate = cobra2DConfig.getRefreshRate();
		ResourceLocation resourceLocation = cobra2DConfig.getResourceLocation();

		boolean doMapUpdate = cobra2DConfig.isDoMapUpdate();
		boolean doRender = cobra2DConfig.hasRenderer();

		this.imageMemory = new ImageMemory();
		this.animationMemory = new AnimationMemory(imageMemory);
		this.renderer = createRenderer(rendererType);
		this.controller = createController(renderer);
		this.map = new Map(controller);

		boolean createWorldUpdater = cobra2DConfig.isCreateWorldUpdater();
		if (createWorldUpdater) {
			this.worldUpdater = new ActiveWorldUpdater(refreshRate, doMapUpdate, doRender, map, renderer);
		}

		setupEnvironment(resourceLocation);
	}

	private Controller createController(Renderer renderer) {
		if (cobra2DConfig.isCreateController()) {
			return new SinglePlayerController(renderer);
		} else {
			return new MockController();
		}
	}

	public void start() {
		int resolutionX = cobra2DConfig.getResolutionX();
		int resolutionY = cobra2DConfig.getResolutionY();
		int bitDepth = cobra2DConfig.getBitDepth();
		int refreshRate = cobra2DConfig.getRefreshRate();
		boolean fullscreen = cobra2DConfig.getFullscreen();

		renderer.initializeRenderer(this, resolutionX, resolutionY, bitDepth, refreshRate, fullscreen);
		if (nonNull(worldUpdater)) {
			this.worldUpdater.start();
		}
	}

	private Renderer createRenderer(RendererType rendererType) {
		Renderer renderer = null;
		if (RendererType.JAVA2D.equals(rendererType)) {
			renderer = new WindowRenderer();
		} else if (RendererType.LIBGDX.equals(rendererType)) {
			renderer = new LibGdxRenderer();
		}
		return renderer;
	}

	public ImageMemory getImageMemory() {
		return this.imageMemory;
	}

	public AnimationMemory getAnimationMemory() {
		return this.animationMemory;
	}

	public Map getMap() {
		return this.map;
	}

	public void shutdownEngine() {
		if (nonNull(worldUpdater)) {
			this.worldUpdater.stop();
		}
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

	public HashMap<String, URL> getImageKeyToUrlMapping() {
		return imageMemory.getImageKeyToUrlMapping();
	}

	public void addImage(String address, URL ressourceURL) throws ImageMemoryException {
		imageMemory.addImage(address, ressourceURL);
	}

	public void clearImageMemory() {
		imageMemory.clearImageMemory();
	}

	public VolatileImage getImage(String address) {
		return imageMemory.getImage(address);
	}

	public VolatileImage removeImage(String imageAddress) {
		return imageMemory.removeImage(imageAddress);
	}

	public HashMap<String, VolatileImage> getImages() {
		return imageMemory.getImages();
	}

	public HashMap<String, Animation> getAnimations() {
		return animationMemory.getAnimations();
	}

	public void addAnimation(String address, String texturAddress, int width, int height) {
		animationMemory.addAnimation(address, texturAddress, width, height);
	}

	public Animation removeAnimation(String animationAddress) {
		return animationMemory.removeAnimation(animationAddress);
	}

	public void clearAnimationMemory() {
		animationMemory.clearAnimationMemory();
	}

	public Animation getAnimation(String address) {
		return animationMemory.getAnimation(address);
	}

	public void addEntity(Entity... entities) {
		map.addEntity(entities);
	}

	public void addEntity(Entity entity) {
		map.addEntity(entity);
	}

	public void removeEntity(Entity... entities) {
		map.removeEntity(entities);
	}

	public void removeEntity(Entity entity) {
		map.removeEntity(entity);
	}

	public Controller getController() {
		return controller;
	}

}

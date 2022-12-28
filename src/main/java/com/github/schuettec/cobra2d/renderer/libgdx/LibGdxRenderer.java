package com.github.schuettec.cobra2d.renderer.libgdx;

import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.renderer.Renderer;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.RendererException;
import com.github.schuettec.cobra2d.world.World;

public class LibGdxRenderer extends ApplicationAdapter implements Renderer {

	enum RendererState {
		INITIALIZED,
		CREATED,
		FINISHED;
	}

	private Cobra2DEngine engine;
	private World world;

	private RendererState state;

	private int resolutionX;
	private int resolutionY;

	private LibGdxRendererAccess rendererAccess;
	private LibGdxController controller;

	private SpriteBatch spriteRenderer;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;

	private Map<String, URL> textureLocations = new Hashtable<>();
	private Map<String, Texture> textures = new Hashtable<>();

	public LibGdxRenderer() {
		this.controller = new LibGdxController();
	}

	@Override
	public void initializeRenderer(Cobra2DEngine engine, int resolutionX, int resolutionY, int bitDepth, int refreshRate,
	    boolean fullscreen) throws RendererException {
		this.engine = engine;
		this.world = engine.getWorld();
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		// Anti-Aliasing here ------------------------vv
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 16); // 8, 8, 8, 8, 16, 0 are default values
		config.setInitialBackgroundColor(Color.BLACK);
		config.setHdpiMode(HdpiMode.Pixels);
		config.useVsync(true);
		if (fullscreen) {
			DisplayMode mode = getDisplayMode(resolutionX, resolutionY, bitDepth, refreshRate);
			config.setFullscreenMode(mode);
		} else {
			config.setWindowedMode(resolutionX, resolutionY);
		}
		config.setForegroundFPS(refreshRate);
		config.setTitle("Cobra2D Engine");

		this.state = RendererState.INITIALIZED;
		new Lwjgl3Application(LibGdxRenderer.this, config);
	}

	@Override
	public void create() {
		this.state = RendererState.CREATED;

		this.rendererAccess = new LibGdxRendererAccess(this);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		spriteRenderer = new SpriteBatch();

		camera = new OrthographicCamera(resolutionX, resolutionY);
		camera.viewportWidth = resolutionX;
		camera.viewportHeight = resolutionY;

		loadTextures();
	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();

		if (controller.isEscapePressed()) {
			engine.shutdownEngine();
		} else {
			world.update(deltaTime);
			camera.update();

			ScreenUtils.clear(Color.BLACK);

			Set<Camera> cameras = world.getCameras();
			for (Camera camera : cameras) {
				List<Collision> capturedEntities = world.getCameraCollision(camera);

				renderClippingMask(camera);

				renderCameraView(camera, capturedEntities);

			}
		}
	}

	private void renderCameraView(Camera camera, List<Collision> capturedEntities) {
		/* Enable RGBA color writing. */
		Gdx.gl.glColorMask(true, true, true, true);

		/* Set the depth function to EQUAL. */
		Gdx.gl.glDepthFunc(GL20.GL_EQUAL);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.render(rendererAccess, world, capturedEntities);

		shapeRenderer.flush();
	}

	private void renderClippingMask(Camera camera) {
		/* Clear our depth buffer info from previous frame. */
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

		/* Set the depth function to LESS. */
		Gdx.gl.glDepthFunc(GL20.GL_LESS);

		/* Enable depth writing. */
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		/* Disable RGBA color writing. */
		Gdx.gl.glColorMask(false, false, false, false);

		camera.renderClippingMask(rendererAccess);
		shapeRenderer.flush();
	}

	@Override
	public void finish() {
		this.state = RendererState.FINISHED;
		shapeRenderer.dispose();
		spriteRenderer.dispose();
		disposeTextures();
		Gdx.app.exit();
	}

	public DisplayMode getDisplayMode(int resolutionX, int resolutionY, int bitDepth, int refreshRate) {
		Monitor primary = Lwjgl3ApplicationConfiguration.getPrimaryMonitor();
		DisplayMode[] availableModes = Lwjgl3ApplicationConfiguration.getDisplayModes(primary);
		for (DisplayMode mode : availableModes) {
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

	@Override
	public Controller getController() {
		return controller;
	}

	int getResolutionX() {
		return resolutionX;
	}

	int getResolutionY() {
		return resolutionY;
	}

	SpriteBatch getSpriteRenderer() {
		return spriteRenderer;
	}

	@Override
	public RendererAccess getRendererAccess() {
		return rendererAccess;
	}

	@Override
	public void addTexture(String textureId, URL url) {
		textureLocations.put(textureId, url);
		loadTextureOnDemand(textureId, url);
	}

	private void loadTextureOnDemand(String textureId, URL url) {
		// load texture on demand
		if (RendererState.CREATED.equals(state)) {
			loadTexture(textureId, url);
		}
	}

	private void loadTexture(String textureId, URL url) {
		String path = url.getPath();
		FileHandle fileHandle = Gdx.files.internal(path);
		Texture texture = new Texture(fileHandle);
		textures.put(textureId, texture);
	}

	private void loadTextures() {
		textureLocations.keySet()
		    .stream()
		    .forEach(id -> {
			    URL url = textureLocations.get(id);
			    loadTexture(id, url);
		    });
	}

	private void disposeTextures() {
		textures.values()
		    .stream()
		    .forEach(t -> {
			    t.dispose();
		    });
	}

	Texture getTexture(String imageId) {
		if (textures.containsKey(imageId)) {
			return textures.get(imageId);
		} else {
			throw new RendererException("Texture not loaded by renderer: " + imageId);
		}
	}

}

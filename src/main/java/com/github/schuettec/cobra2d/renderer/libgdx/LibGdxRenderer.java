package com.github.schuettec.cobra2d.renderer.libgdx;

import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.audio.Sound;
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
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.SoundEffect;
import com.github.schuettec.cobra2d.entity.skills.sound.SoundCamera;
import com.github.schuettec.cobra2d.renderer.Renderer;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.RendererException;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;
import com.github.schuettec.cobra2d.world.Collision;

public class LibGdxRenderer extends ApplicationAdapter implements Renderer {

	enum RendererState {
		INITIALIZED,
		CREATED,
		FINISHED;
	}

	private Cobra2DEngine engine;
	private Cobra2DWorld world;

	private RendererState state;

	private LibGdxRendererAccess rendererAccess;
	private LibGdxSoundAccess soundAccess;
	private LibGdxController controller;

	private SpriteBatch spriteRenderer;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;

	private Map<String, URL> textureLocations = new Hashtable<>();
	private Map<String, Texture> textures = new Hashtable<>();

	private Map<String, URL> soundLocations = new Hashtable<>();
	private Map<String, Sound> sounds = new Hashtable<>();

	private CountDownLatch waitForRenderer = new CountDownLatch(1);

	private boolean started;

	public LibGdxRenderer() {
		this.controller = new LibGdxController();
		this.state = RendererState.CREATED;
	}

	@Override
	public void initializeRenderer(Cobra2DEngine engine) throws RendererException {
		this.engine = engine;
		this.world = engine.getWorld();
		this.textureLocations = engine.getTextures();
		this.soundLocations = engine.getSounds();
		this.started = false;

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		// Anti-Aliasing here ------------------------vv
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 16); // 8, 8, 8, 8, 16, 0 are default values
		config.setInitialBackgroundColor(Color.BLACK);
		config.setHdpiMode(HdpiMode.Pixels);
		config.useVsync(true);
		if (engine.isFullscreen()) {
			DisplayMode mode = getDisplayMode(engine.getResolutionX(), engine.getResolutionY(), engine.getBitDepth(),
			    engine.getRefreshRate());
			config.setFullscreenMode(mode);
		} else {
			config.setWindowedMode(engine.getResolutionX(), engine.getResolutionY());
		}
		config.setForegroundFPS(engine.getRefreshRate());
		config.setTitle("Cobra2D Engine");

		Thread initializer = new Thread(new Runnable() {

			@Override
			public void run() {
				new Lwjgl3Application(LibGdxRenderer.this, config);
			}
		});
		initializer.start();

		try {
			waitForRenderer.await();
			this.state = RendererState.INITIALIZED;
		} catch (InterruptedException e) {
			throw new RuntimeException("Interrupted while waiting for renderer.");
		}
	}

	@Override
	public void entityAdded(Entity entity) {
		// Only initialize entities if the entity was added after renderer was started.
		if (RendererState.INITIALIZED.equals(state)) {
			Renderer.super.entityAdded(entity);
			((Renderable) entity).initialize(rendererAccess);
		}
	}

	@Override
	public void create() {
		loadTextures();
		loadSounds();

		this.rendererAccess = new LibGdxRendererAccess(this);
		this.soundAccess = new LibGdxSoundAccess(this);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		spriteRenderer = new SpriteBatch();

		int resolutionX = engine.getResolutionX();
		int resolutionY = engine.getResolutionY();
		camera = new OrthographicCamera(resolutionX, resolutionY);
		camera.viewportWidth = resolutionX;
		camera.viewportHeight = resolutionY;

		world.getRenderables()
		    .stream()
		    .forEach(r -> r.initialize(rendererAccess));

		waitForRenderer.countDown();
	}

	@Override
	public void render() {
		if (started) {
			float deltaTime = Gdx.graphics.getDeltaTime();

			if (controller.isEscapePressed()) {
				engine.shutdownEngine();
			} else {
				int refreshRate = engine.getRefreshRate();
				world.update(refreshRate, deltaTime);
				camera.update();

				ScreenUtils.clear(Color.BLACK);

				Set<Camera> cameras = world.getCameras();
				for (Camera camera : cameras) {
					List<Collision> capturedEntities = world.getCameraCollision(camera);

					renderClippingMask(camera);

					renderCameraView(camera, capturedEntities);

					if (camera instanceof SoundCamera) {
						SoundCamera soundCamera = (SoundCamera) camera;
						List<SoundEffect> soundCollision = world.getSoundCollision(camera);
						soundCamera.playback(soundAccess, world, soundCollision);
					}
				}
			}
		}
	}

	@Override
	public void start() {
		this.started = true;
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

		world.getRenderables()
		    .stream()
		    .forEach(r -> r.dispose());

		shapeRenderer.dispose();
		spriteRenderer.dispose();
		disposeSounds();
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

	int getResolutionX() {
		return engine.getResolutionX();
	}

	int getResolutionY() {
		return engine.getResolutionY();
	}

	SpriteBatch getSpriteRenderer() {
		return spriteRenderer;
	}

	public RendererAccess getRendererAccess() {
		return rendererAccess;
	}

	private void loadTexture(String textureId, URL url) {
		String path = url.getPath();
		FileHandle fileHandle = Gdx.files.internal(path);
		Texture texture = new Texture(fileHandle);
		textures.put(textureId, texture);
	}

	private void loadSound(String soundId, URL url) {
		String path = url.getPath();
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
		sounds.put(soundId, sound);
	}

	private void loadTextures() {
		textureLocations.keySet()
		    .stream()
		    .forEach(id -> {
			    URL url = textureLocations.get(id);
			    loadTexture(id, url);
		    });
	}

	private void loadSounds() {
		soundLocations.keySet()
		    .stream()
		    .forEach(id -> {
			    URL url = soundLocations.get(id);
			    loadSound(id, url);
		    });
	}

	private void disposeTextures() {
		textures.values()
		    .stream()
		    .forEach(t -> {
			    t.dispose();
		    });
	}

	private void disposeSounds() {
		sounds.values()
		    .stream()
		    .forEach(s -> {
			    s.dispose();
		    });
	}

	Texture getTexture(String imageId) {
		if (textures.containsKey(imageId)) {
			return textures.get(imageId);
		} else {
			throw new RendererException("Texture not loaded by renderer: " + imageId);
		}
	}

	Sound getSound(String soundId) {
		if (sounds.containsKey(soundId)) {
			return sounds.get(soundId);
		} else {
			throw new RendererException("Texture not loaded by renderer: " + soundId);
		}
	}

	@Override
	public Controller getControllerForEntity(Entity entity) {
		return controller;
	}

}

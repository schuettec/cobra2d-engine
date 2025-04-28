package com.github.schuettec.cobra2d.renderer.libgdx;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;

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
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Camera3D;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Renderable3D;
import com.github.schuettec.cobra2d.entity.skills.Skill;
import com.github.schuettec.cobra2d.entity.skills.SoundEffect;
import com.github.schuettec.cobra2d.entity.skills.sound.SoundCamera;
import com.github.schuettec.cobra2d.math.Dimension;
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
	private LibGdxAccess accessObject;
	private LibGdxController controller;

	private SpriteBatch spriteRenderer;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;

	private Map<String, URL> textureLocations = new Hashtable<>();
	private Map<String, Texture> textures = new Hashtable<>();

	private Map<String, URL> soundLocations = new Hashtable<>();
	private Map<String, Sound> sounds = new Hashtable<>();

	private Map<String, Model> models = new Hashtable<>();
	private Map<String, URL> modelLocations = new Hashtable<>();

	private CountDownLatch waitForRenderer = new CountDownLatch(1);

	private boolean started;
	private PerspectiveCamera camera3d;
	private Environment environment;
	private ModelBatch modelBatch;

	private ModelInstance testmodelInstance;

	public LibGdxRenderer() {
		this.state = RendererState.CREATED;

	}

	@Override
	public void initializeRenderer(Cobra2DEngine engine) throws RendererException {
		this.engine = engine;
		this.world = engine.getWorld();
		this.textureLocations = engine.getTextures();
		this.soundLocations = engine.getSounds();
		this.modelLocations = engine.getModels();
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
	public void entityAdded(Skill entity) {
		// Only initialize entities if the entity was added after renderer was started.
		if (RendererState.INITIALIZED.equals(state)) {
			Renderer.super.entityAdded(entity);
			((Renderable) entity).initialize(rendererAccess);
		}
	}

	@Override
	public void create() {
		loadTextures();
		loadModels();
		loadSounds();

		this.rendererAccess = new LibGdxRendererAccess(this);
		this.accessObject = new LibGdxAccess(this);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		spriteRenderer = new SpriteBatch();

		int resolutionX = engine.getResolutionX();
		int resolutionY = engine.getResolutionY();
		camera = new OrthographicCamera(resolutionX, resolutionY);
		camera.viewportWidth = resolutionX;
		camera.viewportHeight = resolutionY;
		camera.update();

		// camera3d = new OrthographicCamera(resolutionX, resolutionY);
		camera3d = new PerspectiveCamera(90, resolutionX, resolutionY);
		camera3d.viewportWidth = resolutionX;
		camera3d.viewportHeight = resolutionY;
		camera3d.position.set(0f, 0f, 100f);
		camera3d.lookAt(0f, 0f, 0f);
		camera3d.near = 1f;
		camera3d.far = 100f;
		camera3d.update();

		// Umgebung mit Licht
		environment = new Environment();
		environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -1f, -1f));
		environment.add(new DirectionalLight().set(1f, 1f, 1f, 1f, 1f, 1f));

		// ModelBatch initialisieren
		modelBatch = new ModelBatch();

		// KANN WEG
		// Beispielmodell erstellen (ein einfacher Würfel)
		ModelBuilder modelBuilder = new ModelBuilder();
		Model testmodel = modelBuilder.createBox(2f, 2f, 2f, // Breite, Höhe, Tiefe
		    new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		// ModelInstance erstellen
		this.testmodelInstance = new ModelInstance(testmodel);

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
				camera3d.update();

				ScreenUtils.clear(Color.BLACK);

				Set<Camera> cameras = world.getCameras();
				for (Camera camera : cameras) {
					List<Collision> capturedEntities = world.getCameraCollision(camera);

					// renderClippingMask(camera);

					renderCameraView(camera, capturedEntities);

					if (camera instanceof SoundCamera) {
						SoundCamera soundCamera = (SoundCamera) camera;
						List<SoundEffect> soundCollision = world.getSoundCollision(camera);
						soundCamera.playback(accessObject, world, soundCollision);
					}
				}
			}
		}
	}

	@Override
	public void start() {
		this.controller = new LibGdxController(engine.getCameraForInput());
		this.started = true;
	}

	private void renderCameraView(Camera camera, List<Collision> capturedEntities) {
		/* Enable RGBA color writing. */
		// Gdx.gl.glColorMask(true, true, true, true);

		/* Set the depth function to EQUAL. */
		// Gdx.gl.glDepthFunc(GL20.GL_EQUAL);

		// Gdx.gl.glClearColor(0, 0, 0, 1);

		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT | Gdx.gl.GL_DEPTH_BUFFER_BIT);

		// Clear the screen
		// Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);

		// Bildschirm löschen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1f); // Dunkelblau als Hintergrundfarbe

		// Kamera aktualisieren
		camera.render(rendererAccess, world, capturedEntities);

		if (camera instanceof Camera3D) {
			modelBatch.begin(this.camera3d);
			Camera3D camera3d = (Camera3D) camera;
			List<Renderable3D> captured3D = to3DList(capturedEntities);
			Dimension viewFieldDimension = getCamera3DTranslation();
			camera3d.render3D(accessObject, environment, modelBatch, captured3D, viewFieldDimension);
			// modelBatch.render(testmodelInstance, environment);
			modelBatch.end();

		}
	}

	private Dimension getCamera3DTranslation() {
		Vector3 ul = camera3d.unproject(new Vector3(0, 0, 1));
		Vector3 or = camera3d.unproject(new Vector3(this.camera3d.viewportWidth, this.camera3d.viewportHeight, 1));

		return new Dimension(abs(ul.x - or.x), abs(ul.y - or.y));
	}

	private List<Renderable3D> to3DList(List<Collision> capturedEntities) {
		return capturedEntities.stream()
		    .filter(col -> col.getOpponent() instanceof Renderable3D)
		    .map(col -> (Renderable3D) col.getOpponent())
		    .collect(toList());
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
		modelBatch.dispose();
		disposeSounds();
		disposeModels();
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

	private void loadModel(String modelId, URL url) {
		String path = url.getPath();
		FileHandle fileHandle = Gdx.files.internal(path); // oder .obj
		Model model = loadModel(fileHandle);
		models.put(modelId, model);
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

	private void loadModels() {
		modelLocations.keySet()
		    .stream()
		    .forEach(id -> {
			    URL url = modelLocations.get(id);
			    loadModel(id, url);
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

	private void disposeModels() {
		models.values()
		    .stream()
		    .forEach(m -> {
			    m.dispose();
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
			throw new RendererException("Sound not loaded by renderer: " + soundId);
		}
	}

	Model getModel(String modelId) {
		if (models.containsKey(modelId)) {
			return models.get(modelId);
		} else {
			throw new RendererException("Model not loaded by renderer: " + modelId);
		}
	}

	@Override
	public Controller getControllerForEntity(Skill entity) {
		return controller;
	}

	public static Model loadModel(FileHandle fileHandle) {
		String extension = fileHandle.extension()
		    .toLowerCase();

		switch (extension) {
			case "g3dj":
			case "g3db":
				// Lade mit G3dModelLoader
				G3dModelLoader g3dLoader = new G3dModelLoader(new JsonReader());
				return g3dLoader.loadModel(fileHandle);

			case "obj":
				// Lade mit ObjLoader
				ObjLoader objLoader = new ObjLoader();
				return objLoader.loadModel(fileHandle);

			default:
				throw new IllegalArgumentException("Unsupported model format: " + extension);
		}
	}

	public com.badlogic.gdx.graphics.Camera getCamera3D() {
		return this.camera3d;
	}

}

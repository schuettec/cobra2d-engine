package com.github.schuettec.cobra2Dexamples.render3d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;

public class ModelRenderingExample extends ApplicationAdapter {
  private PerspectiveCamera camera;
  private ModelBatch modelBatch;
  private Environment environment;
  private Model model;
  private ModelInstance modelInstance;

  @Override
  public void create() {
    // Kamera einrichten
    camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(5f, 5f, 5f);
    camera.lookAt(0f, 0f, 0f);
    camera.near = 1f;
    camera.far = 100f;
    camera.update();

    // Umgebung mit Licht
    environment = new Environment();
    environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));

    // ModelBatch initialisieren
    modelBatch = new ModelBatch();

    String path = "./models/uvmap/uvmap.g3dj";// "models/simple/simple.g3dj";

    FileHandle fileHandle = Gdx.files.internal(path);
    G3dModelLoader loader = new G3dModelLoader(new JsonReader());
    model = loader.loadModel(fileHandle);

    // ModelInstance erstellen
    modelInstance = new ModelInstance(model);
  }

  @Override
  public void render() {
    // Bildschirm löschen
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1f); // Dunkelblau als Hintergrundfarbe

    // Kamera aktualisieren
    camera.update();

    // Modell rendern
    modelBatch.begin(camera);
    modelBatch.render(modelInstance, environment);
    modelBatch.end();
  }

  @Override
  public void dispose() {
    // Ressourcen bereinigen
    modelBatch.dispose();
    model.dispose();
  }

  // Main-Methode
  public static void main(String[] args) {
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setTitle("Model Rendering Example");
    config.setWindowedMode(800, 600); // Fenstergröße
    config.useVsync(true); // VSync aktivieren
    config.setForegroundFPS(60); // Framerate-Limit

    new Lwjgl3Application(new ModelRenderingExample(), config);
  }
}

package com.github.schuettec.cobra2Dexamples.render3d;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;

public class ModelImportExample extends ApplicationAdapter {
  private PerspectiveCamera camera;
  private ModelBatch modelBatch;
  private Environment environment;
  private Model model;
  private ModelInstance modelInstance;

  @Override
  public void create() {
    // Kamera
    camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(10f, 10f, 10f);
    camera.lookAt(0f, 0f, 0f);
    camera.near = 1f;
    camera.far = 300f;
    camera.update();

    // Umgebung
    environment = new Environment();
    environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));

    // ModelBatch
    modelBatch = new ModelBatch();

    Gdx.app.setLogLevel(Application.LOG_DEBUG);

    // Modell laden
    try {
      String path = "models/uvmap/uvmap.g3dj";// "models/simple/simple.g3dj";

      Texture texture = new Texture(Gdx.files.internal("models/uvmap/UVImage.png"));

      FileHandle fileHandle = Gdx.files.internal(path);
      G3dModelLoader loader = new G3dModelLoader(new JsonReader());
      model = loader.loadModel(fileHandle);

      // Standardmaterial hinzufügen, falls keines vorhanden ist
      for (Material material : model.materials) {
        material.set(TextureAttribute.createDiffuse(texture));
      }

      // ModelInstance
      modelInstance = new ModelInstance(model);
      // modelInstance.transform.setToTranslation(0f, 0f, 0f);
      // modelInstance.transform.scale(0.01f, 0.01f, 0.01f);
    } catch (Exception e) {
      Gdx.app.error("ModelImport", "Fehler beim Laden des Modells", e);
    }
  }

  @Override
  public void render() {
    // Bildschirm leeren
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    camera.update();

    modelBatch.begin(camera);
    if (modelInstance != null) {
      modelBatch.render(modelInstance, environment);
    }
    modelBatch.end();
  }

  @Override
  public void dispose() {
    modelBatch.dispose();
    if (model != null)
      model.dispose();
  }

  public static void main(String[] args) {
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setTitle("Model Import Example");
    config.setWindowedMode(800, 600);
    new Lwjgl3Application(new ModelImportExample(), config);
  }
}

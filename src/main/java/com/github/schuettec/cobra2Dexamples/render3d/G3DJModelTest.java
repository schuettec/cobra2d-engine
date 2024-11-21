package com.github.schuettec.cobra2Dexamples.render3d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
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

public class G3DJModelTest extends ApplicationAdapter {

  private PerspectiveCamera camera;
  private Environment environment;
  private Model model;
  private ModelInstance modelInstance;
  private Texture texture;

  @Override
  public void create() {
    // Kamera einrichten
    camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(3f, 3f, 5f); // Kamera-Position
    camera.lookAt(0f, 0f, 0f); // Blickpunkt
    camera.near = 1f;
    camera.far = 100f;
    camera.update();

    // Beleuchtung einrichten
    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
    environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));

    // Modell und Textur laden
    texture = new Texture(
        Gdx.files.absolute("/home/chris/git/cobra2d-engine/src/main/resources/models/uvmap/UVImage.png")); // Textur
                                                                                                           // laden
    model = new G3dModelLoader(new JsonReader()).loadModel(
        Gdx.files.absolute("/home/chris/git/cobra2d-engine/src/main/resources/models/uvmap/uvmap-flippeduv.g3dj"));
    modelInstance = new ModelInstance(model);

    // Textur zu Materialien hinzufügen
    for (Material material : modelInstance.materials) {
      material.set(TextureAttribute.createDiffuse(texture));
    }
  }

  @Override
  public void render() {
    // Bildschirm löschen
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
    Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT | Gdx.gl.GL_DEPTH_BUFFER_BIT);

    // Modell rendern
    ModelBatch modelBatch = new ModelBatch();
    modelBatch.begin(camera);
    modelBatch.render(modelInstance, environment);
    modelBatch.end();
  }

  @Override
  public void dispose() {
    // Ressourcen freigeben
    if (model != null)
      model.dispose();
    if (texture != null)
      texture.dispose();
  }

  public static void main(String[] args) {
    // libGDX-Anwendung starten
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setTitle("G3DJ Model Test");
    config.setWindowedMode(800, 600);
    config.useVsync(true);
    new Lwjgl3Application(new G3DJModelTest(), config);
  }
}

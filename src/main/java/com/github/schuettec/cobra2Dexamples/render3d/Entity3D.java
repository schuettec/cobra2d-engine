package com.github.schuettec.cobra2Dexamples.render3d;

import static java.util.Objects.isNull;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Renderable3D;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.ModelAccess;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class Entity3D extends BasicCircleEntity implements CircleRenderable, Updatable, Obstacle, Renderable3D {

  private String modelAdress;
  private ModelInstance modelInstance;
  private float rotationX = 0f;
  private float rotationY = 0f;
  private float rotationZ = 0f;

  public Entity3D(String modelAdress, Point worldCoordinates, double radius) {
    super(worldCoordinates, radius);
    this.modelAdress = modelAdress;
  }

  @Override
  public void update(WorldAccess worldAccess, float deltaTime) {
    // this.rotationX = (float) Math2D.normalizeAngle(rotationX + 1f);
    // this.rotationY = (float) Math2D.normalizeAngle(rotationY + 1f);
    this.rotationZ = (float) Math2D.normalizeAngle(rotationZ + 1f);
  }

  @Override
  public void render(RendererAccess renderer, Point screenTranslation) {
    Circle collisionShape = getCollisionShapeInWorldCoordinates();
    renderCircle(collisionShape, renderer, screenTranslation);

  }

  @Override
  public void render3D(ModelAccess modelAccess, Environment environment, ModelBatch modelBatch,
      Point screenTranslation) {

    if (isNull(modelInstance)) {
      Model model = modelAccess.getModel(modelAdress);
      modelInstance = new ModelInstance(model);
      // modelInstance.transform.scale(0.1f, 0.1f, 0.1f);
      modelInstance.transform.setToScaling(0.01f, 0.01f, 0.01f);
      // modelInstance.transform.idt();
      // modelInstance.transform.setToTranslation(0f, 0f, 0.48989f);
    }

    // // Position und Skalierung beibehalten
    Vector3 position = new Vector3();
    modelInstance.transform.getTranslation(position); // Position sichern

    Vector3 scale = new Vector3();
    modelInstance.transform.getScale(scale); // Skalierung sichern

    // Transformation mit Rotation aktualisieren
    modelInstance.transform.idt() // Reset der Transformationsmatrix
        .translate(position) // Position wiederherstellen
        .rotate(Vector3.X, rotationX) // Rotation setzen
        .rotate(Vector3.Y, rotationY) // Rotation setzen
        .rotate(Vector3.Z, rotationZ) // Rotation setzen
        .scale(scale.x, scale.y, scale.z); // Skalierung wiederherstellen

    modelBatch.render(modelInstance, environment);

    Matrix4 modelTransform = new Matrix4();
    modelTransform.set(modelInstance.transform);
    /* Multiply the transform with the combined matrix of the camera. */
    modelTransform.mul(modelAccess.getCamera3D().combined);
    /* Extract the position as usual. */

    Vector3 curPosition;
    curPosition = modelTransform.getTranslation(new Vector3());
    Point cameraPosition = getPosition().clone()
        .translate(screenTranslation);
    System.out.println(
        "Modelposition: " + curPosition + " Entity Pos:" + getPosition() + " Camera position: " + cameraPosition);

  }

  @Override
  public int getLayer() {
    return 0;
  }

  @Override
  public Color getDrawColor() {
    return Color.WHITE;
  }

}
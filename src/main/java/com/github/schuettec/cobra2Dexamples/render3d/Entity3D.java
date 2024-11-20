package com.github.schuettec.cobra2Dexamples.render3d;

import static com.github.schuettec.cobra2d.math.Math2D.normalizeAngle;
import static java.util.Objects.isNull;

import java.util.function.Function;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
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

  /**
   * The z offset applied before translating to entity position.
   */
  private float zOffset = 10f;

  /**
   * The default scaling of the model applied before the entities scaling factor.
   */
  private float scaleOffset = 6f;

  private float rotationX = 0f;
  private float rotationY = 0f;

  private Vector3 modelTranslation;
  private Vector3 modelScale;
  private Quaternion modelRotation;

  private double animCount = 0;

  public Entity3D(String modelAdress, Point worldCoordinates, double radius) {
    super(worldCoordinates, radius);
    this.modelAdress = modelAdress;
  }

  @Override
  public void update(WorldAccess worldAccess, float deltaTime) {
    this.rotationX = (float) Math2D.normalizeAngle(rotationX + 1f);
    // this.rotationY = (float) Math2D.normalizeAngle(rotationY + 1f);

    setDegrees(normalizeAngle(getDegrees() + 1d));

    animCount = Math2D.normalizeAngle(animCount + 1);

    // Point position = Math2D.getCircle(new Point(0, 0), 100, animCount);
    // setPosition(position);

  }

  @Override
  public void render(RendererAccess renderer, Point screenTranslation) {
    Circle collisionShape = getCollisionShapeInWorldCoordinates();
    renderCircle(collisionShape, renderer, screenTranslation);
  }

  @Override
  public void render3D(ModelAccess modelAccess, Environment environment, ModelBatch modelBatch,
      Function<Point, Point> worldTo3DScreen) {

    if (isNull(modelInstance)) {
      Model model = modelAccess.getModel(modelAdress);
      modelInstance = new ModelInstance(model);

      // Position und Skalierung beibehalten
      this.modelTranslation = new Vector3();
      modelInstance.transform.getTranslation(modelTranslation); // Position sichern

      this.modelScale = new Vector3();
      modelInstance.transform.getScale(modelScale); // Skalierung sichern

      this.modelRotation = new Quaternion();
      modelInstance.transform.getRotation(modelRotation);
    }

    Point position3d = worldTo3DScreen.apply(getPosition().clone());

    // Transformation mit Rotation aktualisieren
    modelInstance.transform.idt() // Reset der Transformationsmatrix
        .translate(modelTranslation) // Position wiederherstellen
        .scale(modelScale.x, modelScale.y, modelScale.z)
        .rotate(modelRotation)
        .translate(position3d.getFloatX(), position3d.getFloatY(), zOffset)
        .scale(scaleOffset, scaleOffset, scaleOffset)
        .scale((float) getScale(), (float) getScale(), (float) getScale())
        .rotate(Vector3.X, rotationX) // Rotation setzen
        .rotate(Vector3.Y, rotationY) // Rotation setzen
        .rotate(Vector3.Z, (float) getDegrees()); // Rotation setzen

    modelBatch.render(modelInstance, environment);

    Matrix4 modelTransform = new Matrix4();
    modelTransform.set(modelInstance.transform);
    modelTransform.mul(modelAccess.getCamera3D().combined);
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
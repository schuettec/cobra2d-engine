package com.github.schuettec.cobra2d.entity.camera;

import static java.util.Objects.isNull;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.github.schuettec.cobra2d.entity.skills.Camera3D;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Renderable3D;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Shape;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.ModelAccess;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;
import com.github.schuettec.cobra2d.world.Collision;
import com.github.schuettec.cobra2d.world.CollisionMap;

public interface AbstractCamera extends Camera3D {
  public boolean isCenterOnScreen();

  public boolean isDrawEntityPoints();

  public boolean isDrawCollisionShape();

  public boolean isDrawCollisionPoints();

  public boolean isDrawCameraOutline();

  public void setCenterOnScreen(boolean centerOnScreen);

  public void setDrawEntityPoints(boolean drawEntityPoints);

  public void setDrawCollisionShape(boolean drawCollisionShape);

  public void setDrawCollisionPoints(boolean drawCollisionPoints);

  public void setDrawCameraOutline(boolean drawCameraOutline);

  public void setPlayerControlled(boolean playerControlled);

  default void drawPoint(RendererAccess renderer, Point point, int radius, Color color) {
    renderer.fillCircle(point.getRoundX(), point.getRoundY(), radius, color);
  }

  default List<Collision> sortByLayer(List<Collision> capturedEntities) {
    Comparator<Renderable> layerComparator = Comparator.comparingInt(Renderable::getLayer);
    Comparator<Collision> collisionComparator = Comparator.comparing(Collision::getOpponent, (e1, e2) -> {
      if (e1 instanceof Renderable && e2 instanceof Renderable) {
        Renderable r1 = (Renderable) e1;
        Renderable r2 = (Renderable) e2;
        return layerComparator.compare(r1, r2);
      } else {
        return 0;
      }
    });
    List<Collision> sorted = new LinkedList<>(capturedEntities);
    sorted.sort(collisionComparator);
    return sorted;
  }

  @Override
  default void render(final RendererAccess renderer, Cobra2DWorld map, List<Collision> capturedEntities) {
    centerOnScreen(renderer);

    capturedEntities = sortByLayer(capturedEntities);

    List<Point> collisionPoints = new LinkedList<>();

    for (Collision collision : capturedEntities) {
      Entity entity = collision.getOpponent();

      Point worldToScreenTranslation = getWorldToScreenTranslation();

      drawCollisionPoints(collisionPoints, renderer, map, worldToScreenTranslation, entity);
      drawEntityPoint(renderer, worldToScreenTranslation, entity);

      // Render entity
      if (entity instanceof Renderable) {
        Renderable renderable = (Renderable) entity;
        renderable.render(renderer, worldToScreenTranslation);
      }
    }

    drawCollisionPoints(renderer, collisionPoints);

    drawCameraOutline(renderer);

  }

  @Override
  default void render3D(ModelAccess modelAccess, Environment environment, ModelBatch modelBatch,
      List<Renderable3D> renderables3d, Dimension viewFieldDimension) {

    Point screenTranslation = getWorldToScreenTranslation();

    double w = -(viewFieldDimension.getWidth() / 2.0);
    double h = -(viewFieldDimension.getHeight() / 2.0);

    Dimension screenTo3DScaling = getScreenTo3DScaling(viewFieldDimension);

    Function<Point, Point> worldTo3DScreen = (p) -> {
      Point position3dScreen = p.clone()
          .translate(screenTranslation)
          .scale(screenTo3DScaling.getWidth(), screenTo3DScaling.getHeight())
          .translate(w, h);
      return position3dScreen;
    };

    renderables3d.forEach(renderable -> renderable.render3D(modelAccess, environment, modelBatch, worldTo3DScreen));
  }

  public default Dimension getScreenTo3DScaling(Dimension viewFieldDimension) {
    Dimension cameraDimension = getCollisionShapeDimension();
    return new Dimension(viewFieldDimension.getWidth() / cameraDimension.getWidth(),
        viewFieldDimension.getHeight() / cameraDimension.getHeight());
  }

  default void centerOnScreen(final RendererAccess renderer) {
    if (isCenterOnScreen()) {
      setScreenPosition(new Point(renderer.getWidth() / 2.0, renderer.getHeight() / 2.0));
    }
  }

  default Point getScreenToWorldTranslation() {
    Point cameraPosition = getPosition();
    return new Point(0, 0).translate(cameraPosition)
        .translate(getScreenPosition().clone()
            .translate(getHalfDimensionTranslation().scale(-1))
            .scale(-1))
        .translate(getHalfDimensionTranslation().scale(-1));
  }

  default Point getWorldToScreenTranslation() {
    Point cameraPosition = getPosition();
    return new Point(0, 0).translate(cameraPosition.clone()
        .scale(-1))
        .translate(getScreenPosition().clone()
            .translate(getHalfDimensionTranslation().scale(-1))
            .scale(-1))
        .translate(getHalfDimensionTranslation());
  }

  default Point getHalfDimensionTranslation() {
    Dimension cameraDimension = getCollisionShapeDimension();
    return new Point(cameraDimension.getWidth() / 2.0, cameraDimension.getHeight() / 2.0);
  }

  @Override
  default Point screenToWorldCoordinates(Point screenCoordinates) {
    Point screenToWorldTranslation = getScreenToWorldTranslation();
    return screenCoordinates.clone()
        .translate(screenToWorldTranslation);
  }

  @Override
  default Point worldToScreenCoordinates(Point worldCoordinates) {
    return worldCoordinates.clone()
        .translate(getWorldToScreenTranslation());
  }

  default void drawCollisionPoints(List<Point> collisionPoints, final RendererAccess renderer, Cobra2DWorld map,
      Point cameraTranslation, Entity entity) {
    // Render entity shape and get collision points
    if (isDrawCollisionShape() && entity instanceof HasCollisionShape) {
      HasCollisionShape hasCollisionShape = (HasCollisionShape) entity;
      Shape entityShape = hasCollisionShape.getCollisionShape(true, true, true)
          .translate(cameraTranslation);
      drawCollisionShape(renderer, entityShape);
      CollisionMap collisionMap = map.getCollisions()
          .detectCollision(map.getObstacles(), HasCollisionShape::getCollisionShapeInWorldCoordinates, false, true,
              true);
      if (collisionMap.hasCollision(entity)) {
        List<Collision> collisions = collisionMap.getCollision(entity);
        collisions.stream()
            .flatMap(c -> c.getPoints()
                .stream())
            .map(p -> p.translate(cameraTranslation))
            .forEach(p -> collisionPoints.add(p));
      }
    }
  }

  default void drawCollisionShape(final RendererAccess renderer, Shape entityShape) {
    if (isDrawCollisionShape() && entityShape.isPointBased()) {
      Point firstPoint = null;
      Point lastPoint = null;
      for (Point p : entityShape.getPoints()) {
        drawPoint(renderer, p, 5, Color.BLUE);
        if (isNull(lastPoint)) {
          lastPoint = p;
          firstPoint = p;
        } else {
          renderer.drawLine(lastPoint.getRoundX(), lastPoint.getRoundY(), p.getRoundX(), p.getRoundY(), Color.YELLOW);
          lastPoint = p;
        }
      }

      renderer.drawLine(firstPoint.getRoundX(), firstPoint.getRoundY(), lastPoint.getRoundX(), lastPoint.getRoundY(),
          Color.YELLOW);
    }
  }

  default void drawCameraOutline(final RendererAccess renderer) {
    // Draw camera outline
    if (isDrawCameraOutline()) {
      // Draw camera outline
      drawCameraOutline(renderer);
    }
  }

  default void drawCollisionPoints(final RendererAccess renderer, List<Point> collisionPoints) {
    // Draw collision points
    if (isDrawCollisionPoints()) {
      collisionPoints.stream()
          .forEach(p -> drawPoint(renderer, p, 5, Color.RED));
    }
  }

  default void drawEntityPoint(final RendererAccess renderer, Point cameraTranslation, Entity entity) {
    if (isDrawEntityPoints()) {
      // Render entity base-point
      Point entityPosition = entity.getPosition()
          .translate(cameraTranslation);
      drawPoint(renderer, entityPosition, 2, Color.YELLOW);
    }
  }

}

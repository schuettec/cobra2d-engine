package com.github.schuettec.cobra2d.entity.camera;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.CollisionMap;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Shape;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

public interface AbstractCamera extends Camera {

	public boolean isDrawEntityPoints();

	public boolean isDrawCollisionShape();

	public boolean isDrawCameraOutline();

	public void setDrawEntityPoints(boolean drawEntityPoints);

	public void setDrawCollisionShape(boolean drawCollisionShape);

	public void setDrawCameraOutline(boolean drawCameraOutline);

	public void setPlayerControlled(boolean playerControlled);

	default void drawPoint(RendererAccess renderer, Point point, int radius, Color color) {
		renderer.fillCircle(point.getRoundX(), point.getRoundY(), radius, color);
	}

	public Point getScreenTranslation(RendererAccess renderer);

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

	default void render(final RendererAccess renderer, Cobra2DWorld map, List<Collision> capturedEntities) {
		capturedEntities = sortByLayer(capturedEntities);

		Point screenTranslation = getScreenTranslation(renderer);

		List<Point> collisionPoints = new LinkedList<>();

		Point position = getPosition();

		// Scale to -2 because the translation of the camera must be subtracted from entity world coordinates.
		Point cameraTranslation = position.scale(-1)
		    // Then translate to the screen position.
		    .translate(screenTranslation);

		for (Collision collision : capturedEntities) {
			Entity entity = collision.getOpponent();

			// Render entity
			if (entity instanceof Renderable) {
				Renderable renderable = (Renderable) entity;
				renderable.render(renderer, cameraTranslation);
			}

			getCollisionPoints(collisionPoints, renderer, map, cameraTranslation, entity);
			drawEntityPoint(renderer, cameraTranslation, entity);
		}

		drawCollisionPoints(renderer, collisionPoints);

		drawCameraOutline(renderer);

	}

	default Point screenToWorldCoordinates(Point screenCoordinates) {
		Point cameraPosition = null;
		Dimension cameraDimension = null;
		cameraPosition = getPosition();
		cameraDimension = getDimension();
		Point mouseWorldCoordinates = screenCoordinates.translate(cameraPosition)
		    .translate(new Point(-cameraDimension.getWidth() / 2.0, -cameraDimension.getHeight() / 2.0));
		return mouseWorldCoordinates;
	}

	default Point worldToScreenCoordinates(Point worldCoordinates) {
		Point cameraPosition = null;
		Dimension cameraDimension = null;
		cameraPosition = getPosition();
		cameraDimension = getDimension();
		Point mouseWorldCoordinates = worldCoordinates.translate(cameraPosition.scale(-1))
		    .translate(new Point(cameraDimension.getWidth() / 2.0, cameraDimension.getHeight() / 2.0));
		return mouseWorldCoordinates;
	}

	default void getCollisionPoints(List<Point> collisionPoints, final RendererAccess renderer, Cobra2DWorld map,
	    Point cameraTranslation, Entity entity) {
		// Render entity shape and get collision points
		if (isDrawCollisionShape() && entity instanceof HasCollisionShape) {
			HasCollisionShape hasCollisionShape = (HasCollisionShape) entity;
			Shape entityShape = hasCollisionShape.getCollisionShape(true, true, true)
			    .translate(cameraTranslation);

			drawCollisionShape(renderer, entityShape);

			CollisionMap collisionMap = map.detectCollision(map.getObstacles(), false, true, true);
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
			entityShape.getPoints()
			    .stream()
			    .forEach(p -> drawPoint(renderer, p, 5, Color.BLUE));
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
		if (isDrawCollisionShape()) {
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

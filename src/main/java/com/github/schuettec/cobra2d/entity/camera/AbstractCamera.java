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
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Shape;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.World;

public interface AbstractCamera extends Camera {

	public void drawCameraOutline(final RendererAccess renderer);

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

	default void render(final RendererAccess renderer, World map, List<Collision> capturedEntities) {
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

			// Render entity shape and get collision points
			if (entity instanceof HasCollisionShape) {
				HasCollisionShape hasCollisionShape = (HasCollisionShape) entity;
				Shape entityShape = hasCollisionShape.getCollisionShape(true, true, true)
				    .translate(cameraTranslation);
				if (entityShape.isPointBased()) {
					entityShape.getPoints()
					    .stream()
					    .forEach(p -> drawPoint(renderer, p, 5, Color.BLUE));
				}

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
			// Render entity base-point
			Point entityPosition = entity.getPosition()
			    .translate(cameraTranslation);
			drawPoint(renderer, entityPosition, 2, Color.YELLOW);
		}

		// Draw collision points
		collisionPoints.stream()
		    .forEach(p -> drawPoint(renderer, p, 5, Color.RED));

		// Draw camera outline
		drawCameraOutline(renderer);

	}

	public Point getScreenPosition();

	public void setScreenPosition(Point screenPosition);
}

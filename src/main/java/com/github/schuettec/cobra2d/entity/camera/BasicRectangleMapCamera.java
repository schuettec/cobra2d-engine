package com.github.schuettec.cobra2d.entity.camera;

import static java.util.Objects.isNull;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.CollisionMap;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Shape;
import com.github.schuettec.cobra2d.renderer.common.Color;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public class BasicRectangleMapCamera extends BasicRectangleEntity implements Camera {

	private boolean playerControlled;
	private Point screenPosition;

	public BasicRectangleMapCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled) {
		super(worldCoordinates, dimension);
		this.playerControlled = playerControlled;
		this.screenPosition = null;
	}

	public BasicRectangleMapCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled,
	    Point screenPosition) {
		super(worldCoordinates, dimension);
		this.playerControlled = playerControlled;
		this.screenPosition = new Point(0, 0);
	}

	@Override
	public void renderClippingMask(RendererAccess renderer) {
		Point screenTranslation = screenPosition;
		if (isNull(screenPosition)) {
			screenTranslation = new Point(renderer.getWidth() / 2.0 - (getDimension().width / 2.0),
			    renderer.getHeight() / 2.0 - (getDimension().height / 2.0));
		}
		Dimension dimension = getDimension();
		renderer.fillRectangle(screenTranslation.getRoundX() - 1, screenTranslation.getRoundY() - 1, dimension.width + 1,
		    dimension.height + 1, Color.BLACK);
	}

	@Override
	public void render(final RendererAccess renderer, Map map, List<Collision> capturedEntities) {
		Point screenTranslation = screenPosition;
		if (isNull(screenPosition)) {
			screenTranslation = new Point(renderer.getWidth() / 2.0 - (getDimension().width / 2.0),
			    renderer.getHeight() / 2.0 - (getDimension().height / 2.0));
		}

		List<Point> collisionPoints = new LinkedList<>();

		Point position = getPosition();
		Point cameraTranslation = position.scale(-1)
		    .translate(screenTranslation);

		for (Collision collision : capturedEntities) {
			Entity entity = collision.getOpponent();

			// Render entity
			if (entity instanceof Renderable) {
				Renderable renderable = (Renderable) entity;
				renderable.render(renderer, cameraTranslation);
			}

			// Render entity shape and get collision points
			if (entity instanceof Obstacle) {
				Obstacle obstacle = (Obstacle) entity;
				Shape entityShape = obstacle.getCollisionShape(true, true, true)
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

				// Render entity base-point
				Point entityPosition = entity.getPosition()
				    .translate(cameraTranslation);
				drawPoint(renderer, entityPosition, 2, Color.LIGHT_GRAY);
			}
		}

		// Draw collision points
		collisionPoints.stream()
		    .forEach(p -> drawPoint(renderer, p, 5, Color.RED));

		// Draw camera outline.
		PolygonRenderable.renderPolygon(getCollisionShape(true, true, false), renderer, screenTranslation, Color.GREEN);
	}

	private void drawPoint(RendererAccess renderer, Point point, int radius, Color color) {
		renderer.fillCircle(point.getRoundX(), point.getRoundY(), radius, color);
	}

	@Override
	public void update(Map map, Controller controller) {
		if (playerControlled) {
			if (controller.isLeftKeyPressed()) {
				this.moveLeft();
			}
			if (controller.isRightKeyPressed()) {
				this.moveRight();
			}
			if (controller.isUpKeyPressed()) {
				this.moveUp();
			}
			if (controller.isDownKeyPressed()) {
				this.moveDown();
			}
		}
	}

	public void moveLeft() {
		this.translate(new Point(-5, 0));
	}

	public void moveRight() {
		this.translate(new Point(5, 0));
	}

	public void moveDown() {
		this.translate(new Point(0, -5));
	}

	public void moveUp() {
		this.translate(new Point(0, 5));
	}

	public void setScreenPosition(Point point) {
		this.screenPosition = point.clone();
	}

}

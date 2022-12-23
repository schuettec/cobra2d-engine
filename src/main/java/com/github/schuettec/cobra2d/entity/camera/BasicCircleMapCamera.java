package com.github.schuettec.cobra2d.entity.camera;

import static java.util.Objects.isNull;

import java.util.LinkedList;
import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.common.Color;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public class BasicCircleMapCamera extends BasicCircleEntity implements Camera {

	public BasicCircleMapCamera(Point worldCoordinates, double radius, boolean playerControlled) {
		super(worldCoordinates, radius);
		this.playerControlled = playerControlled;
		this.screenPosition = null;
	}

	public BasicCircleMapCamera(Point worldCoordinates, double radius, boolean playerControlled, Point screenPosition) {
		super(worldCoordinates, radius);
		this.playerControlled = playerControlled;
		this.screenPosition = new Point(0, 0);
	}

	private boolean playerControlled;
	private Point screenPosition;

	@Override
	public void renderClippingMask(RendererAccess renderer) {
		Point screenTranslation = screenPosition;
		if (isNull(screenPosition)) {
			screenTranslation = new Point(renderer.getWidth() / 2.0, renderer.getHeight() / 2.0);
		}
		renderer.fillCircle(screenTranslation.getRoundX(), screenTranslation.getRoundY(), Math2D.saveRound(getRadius() + 1),
		    Color.YELLOW);
	}

	@Override
	public void render(final RendererAccess renderer, Map map, List<Collision> capturedEntities) {
		Point screenTranslation = screenPosition;
		if (isNull(screenPosition)) {
			screenTranslation = new Point(renderer.getWidth() / 2.0, renderer.getHeight() / 2.0);
		}

		List<Point> collisionPoints = new LinkedList<>();

		Point position = getPosition();
		Point cameraTranslation = position.scale(-1)
		    .translate(screenTranslation);

		for (Collision collision : capturedEntities) {
			Entity entity = collision.getOpponent();

			if (entity instanceof Renderable) {
				Renderable renderable = (Renderable) entity;
				renderable.render(renderer, cameraTranslation);
				Point entityPosition = renderable.getPosition()
				    .translate(cameraTranslation);
				drawPoint(renderer, entityPosition, Color.BLUE);
			}

			if (entity instanceof Obstacle) {
				if (map.hasCollision(entity)) {
					List<Collision> collisions = map.getCollision(entity);
					collisions.stream()
					    .flatMap(c -> c.getPoints()
					        .stream())
					    .map(p -> p.translate(cameraTranslation))
					    .forEach(p -> collisionPoints.add(p));
				}
			}
		}

		collisionPoints.stream()
		    .forEach(p -> drawPoint(renderer, p, Color.RED));

		CircleRenderable.renderCircle(getCollisionShape(true, true, false), renderer, screenTranslation, Color.GREEN);
	}

	private void drawPoint(RendererAccess renderer, Point point, Color color) {
		renderer.fillCircle(point.getRoundX(), point.getRoundY(), 5, color);
	}

	@Override
	public void update(Controller controller, List<Collision> collisions) {
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

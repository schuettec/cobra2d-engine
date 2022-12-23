package com.github.schuettec.cobra2d.entity.camera;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.RectangleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;
import com.github.schuettec.cobra2d.renderer.common.Color;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public class BasicMapCamera extends BasicRectangleEntity implements RectangleRenderable, Camera {

	public BasicMapCamera(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
	}

	@Override
	public void render(final RendererAccess renderer, Map map, List<Collision> capturedEntities) {
		// RENDERER ACCESS NEEDS CLIPPING FEATURE
		// renderer.setClip(getPosition().getRoundX(), getPosition().getRoundY(), getDimension().width + 1,
		// getDimension().height + 1);

		List<Point> collisionPoints = new LinkedList<>();

		Point cameraTranslation = new Point(0, 0);

		for (Collision collision : capturedEntities) {
			Entity entity = collision.getOpponent();

			if (entity instanceof Renderable) {
				Renderable renderable = (Renderable) entity;
				renderable.render(renderer, cameraTranslation);
				Point entityPosition = renderable.getPosition();
				// .translate(getPosition());
				drawPoint(renderer, entityPosition, Color.BLUE);
			}

			if (entity instanceof Obstacle) {
				if (map.hasCollision(entity)) {
					List<Collision> collisions = map.getCollision(entity);
					collisions.stream()
					    .flatMap(c -> c.getPoints()
					        .stream())
					    // .map(p -> p.translate(getPosition()))
					    .forEach(p -> collisionPoints.add(p));
				}
			}
		}

		collisionPoints.stream()
		    .forEach(p -> drawPoint(renderer, p, Color.RED));

		this.render(renderer, cameraTranslation);
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		Polygon collisionShape = getCollisionShape();
		renderPolygon(collisionShape, renderer, position);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public Color getDrawColor() {
		return Color.GREEN;
	}

	private void drawPoint(RendererAccess renderer, Point point, Color color) {
		renderer.fillCircle(point.getRoundX() - 5, point.getRoundY() - 5, 5, color);
	}

	@Override
	public void update(Controller controller, List<Collision> collisions) {
		// if (controller.isKeyPressed(KeyEvent.VK_LEFT)) {
		// this.moveLeft();
		// }
		// if (controller.isKeyPressed(KeyEvent.VK_RIGHT)) {
		// this.moveRight();
		// }
		// if (controller.isKeyPressed(KeyEvent.VK_UP)) {
		// this.moveUp();
		// }
		// if (controller.isKeyPressed(KeyEvent.VK_DOWN)) {
		// this.moveDown();
		// }
	}

	public void moveLeft() {
		this.getPosition()
		    .translate(-5, 0);
	}

	public void moveRight() {
		this.getPosition()
		    .translate(5, 0);
	}

	public void moveUp() {
		this.getPosition()
		    .translate(0, -5);
	}

	public void moveDown() {
		this.getPosition()
		    .translate(0, 5);
	}

}

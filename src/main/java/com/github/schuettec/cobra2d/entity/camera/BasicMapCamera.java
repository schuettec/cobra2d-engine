package com.github.schuettec.cobra2d.entity.camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;

public class BasicMapCamera extends BasicCircleEntity implements Camera {

	public BasicMapCamera(Point worldCoordinates, double radius) {
		super(worldCoordinates, radius);
		// super(worldCoordinates, new Point(worldCoordinates.getX(), worldCoordinates.getY()),
		// new Point(worldCoordinates.getX() + dimension.getWidth(), worldCoordinates.getY()),
		// new Point(worldCoordinates.getX() + dimension.getWidth(), worldCoordinates.getY() + dimension.getHeight()),
		// new Point(worldCoordinates.getX(), worldCoordinates.getY() + dimension.getHeight()));
	}

	@Override
	public void render(Graphics2D graphics, Map map, List<Collision> capturedEntities) {
		List<Point> collisionPoints = new LinkedList<>();

		// IF SHAPE COLLISION DETECTION WORKS, THE NEXT TWO LINES ARE CORRET:
		// for (Collision c : capturedEntities) {
		// Entity entity = c.getOpponent();
		for (Entity entity : map.getObstacles()) {
			if (entity instanceof Renderable) {
				Renderable renderable = (Renderable) entity;
				renderable.render(graphics, new Point(0, 0));
				Point center = renderable.getPosition();
				// .translate(getPosition());
				drawPoint(graphics, center, Color.BLUE);

				if (entity instanceof BasicCircleEntity) {
					BasicCircleEntity c = (BasicCircleEntity) entity;
					double radius = c.getRadius();
					Point debug = Math2D.getCircle(center, radius, 0);
					drawPoint(graphics, debug, Color.YELLOW);
				}

			}
			if (entity instanceof Obstacle) {
				if (map.hasCollision(entity)) {
					Collision collision = map.getCollision(entity);
					collision.getPoints()
					    .stream()
					    // .map(p -> p.translate(getPosition()))
					    .forEach(p -> collisionPoints.add(p));
				}
			}
		}

		collisionPoints.stream()
		    .forEach(p -> drawPoint(graphics, p, Color.RED));

		this.render(graphics, getPosition());
	}

	private void drawPoint(Graphics2D graphics, Point point, Color color) {
		Color oldColor = graphics.getColor();
		graphics.setColor(color);
		graphics.fillOval(point.getRoundX() - 5, point.getRoundY() - 5, 5, 5);
		graphics.setColor(oldColor);
	}

	@Override
	public void update(Controller controller, List<Collision> collisions) {
		// if (controller.isKeyPressed(KeyEvent.VK_LEFT)) {
		// this.moveLeft();
		// } else if (controller.isKeyPressed(KeyEvent.VK_RIGHT)) {
		// this.moveRight();
		// } else if (controller.isKeyPressed(KeyEvent.VK_UP)) {
		// this.moveUp();
		// } else if (controller.isKeyPressed(KeyEvent.VK_DOWN)) {
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

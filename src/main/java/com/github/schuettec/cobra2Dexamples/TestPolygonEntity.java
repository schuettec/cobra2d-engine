package com.github.schuettec.cobra2Dexamples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicPolygonEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;

public class TestPolygonEntity extends BasicPolygonEntity implements PolygonRenderable, Obstacle, Updatable {

	private static final long serialVersionUID = 1L;

	private boolean playerControlled;

	public TestPolygonEntity(Point worldCoordinates, Dimension dimension, boolean playerControlled) {
		super(worldCoordinates, new Point(0, 0), new Point(dimension.getWidth(), 0),
		    new Point(dimension.getWidth(), dimension.getHeight()), new Point(0, dimension.getHeight()));
		this.playerControlled = playerControlled;
	}

	@Override
	public void render(Graphics2D graphics, Point position) {
		Polygon collisionShape = getCollisionShape();
		renderPolygon(collisionShape, graphics, position);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public Color getDrawColor() {
		return Color.white;
	}

	@Override
	public void update(Controller controller, List<Collision> collisions) {

		if (playerControlled) {
			if (controller.isKeyPressed(KeyEvent.VK_LEFT)) {
				this.moveLeft();
			} else if (controller.isKeyPressed(KeyEvent.VK_RIGHT)) {
				this.moveRight();
			} else if (controller.isKeyPressed(KeyEvent.VK_UP)) {
				this.moveUp();
			} else if (controller.isKeyPressed(KeyEvent.VK_DOWN)) {
				this.moveDown();
			}
		}
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

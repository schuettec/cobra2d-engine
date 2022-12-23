package com.github.schuettec.cobra2Dexamples.moveableShapes;

import java.awt.Dimension;
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
import com.github.schuettec.cobra2d.renderer.common.Color;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public class MoveablePolygonEntity extends BasicPolygonEntity implements PolygonRenderable, Obstacle, Updatable {

	private static final long serialVersionUID = 1L;

	private boolean playerControlled;

	public MoveablePolygonEntity(Point worldCoordinates, Dimension dimension, boolean playerControlled) {
		super(worldCoordinates, new Point(0, 0), new Point(dimension.getWidth(), 0),
		    new Point(dimension.getWidth(), dimension.getHeight()), new Point(0, dimension.getHeight()));
		this.playerControlled = playerControlled;
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
		return Color.WHITE;
	}

	@Override
	public void update(Controller controller, List<Collision> collisions) {

		if (playerControlled) {
			if (controller.isKeyPressed(KeyEvent.VK_LEFT)) {
				this.moveLeft();
			}
			if (controller.isKeyPressed(KeyEvent.VK_RIGHT)) {
				this.moveRight();
			}
			if (controller.isKeyPressed(KeyEvent.VK_UP)) {
				this.moveUp();
			}
			if (controller.isKeyPressed(KeyEvent.VK_DOWN)) {
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

package com.github.schuettec.cobra2Dexamples.moveableShapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Point;

public class MoveableCircleEntity extends BasicCircleEntity implements CircleRenderable, Obstacle, Updatable {

	private static final long serialVersionUID = 1L;

	private boolean playerControlled;

	public MoveableCircleEntity(Point worldCoordinates, double radius, boolean playerControlled) {
		super(worldCoordinates, radius);
		this.playerControlled = playerControlled;

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

	@Override
	public void render(Graphics2D graphics, Point position) {
		Circle collisionShape = getCollisionShape();
		renderCircle(collisionShape, graphics, position);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public Color getDrawColor() {
		return Color.white;
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

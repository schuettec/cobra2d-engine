package com.github.schuettec.cobra2Dexamples;

import java.awt.event.KeyEvent;
import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Point;

public class BallEntity extends BasicCircleEntity implements Obstacle, Updatable {

	private static final long serialVersionUID = 1L;

	private boolean playerControlled;

	public BallEntity(Point worldCoordinates, double radius, boolean playerControlled) {
		super(worldCoordinates, radius);
		this.playerControlled = playerControlled;

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
}

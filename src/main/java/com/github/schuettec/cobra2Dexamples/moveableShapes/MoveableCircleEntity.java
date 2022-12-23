package com.github.schuettec.cobra2Dexamples.moveableShapes;

import java.awt.event.KeyEvent;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.common.Color;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public class MoveableCircleEntity extends BasicCircleEntity implements CircleRenderable, Obstacle, Updatable {

	private static final long serialVersionUID = 1L;

	private boolean playerControlled;

	public MoveableCircleEntity(Point worldCoordinates, double radius, boolean playerControlled) {
		super(worldCoordinates, radius);
		this.playerControlled = playerControlled;

	}

	@Override
	public void update(Map map, Controller controller) {

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
	public void render(final RendererAccess renderer, Point position) {
		Circle collisionShape = getCollisionShapeInWorldCoordinates();
		renderCircle(collisionShape, renderer, position);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public Color getDrawColor() {
		return Color.WHITE;
	}

	public void moveLeft() {
		this.getPosition()
		    .translate(-5, 0);
	}

	public void moveRight() {
		this.getPosition()
		    .translate(5, 0);
	}

	public void moveDown() {
		this.getPosition()
		    .translate(0, -5);
	}

	public void moveUp() {
		this.getPosition()
		    .translate(0, 5);
	}

}

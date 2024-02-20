package com.github.schuettec.cobra2Dexamples.moveableShapes;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicPolygonEntity;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class MoveablePolygonEntity extends BasicPolygonEntity implements PolygonRenderable, Obstacle, Controllable {

	private static final long serialVersionUID = 1L;

	private boolean playerControlled;

	public MoveablePolygonEntity(Point worldCoordinates, Dimension dimension, boolean playerControlled) {
		super(worldCoordinates, new Point(0, 0), new Point(dimension.getWidth(), 0),
				new Point(dimension.getWidth(), dimension.getHeight()), new Point(0, dimension.getHeight()));
		this.playerControlled = playerControlled;
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		Polygon collisionShape = getCollisionShapeInWorldCoordinates();
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
	public void processControllerState(WorldAccess worldAccess, Controller controller) {
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
		this.getPosition().translate(-5, 0);
	}

	public void moveRight() {
		this.getPosition().translate(5, 0);
	}

	public void moveDown() {
		this.getPosition().translate(0, -5);
	}

	public void moveUp() {
		this.getPosition().translate(0, 5);
	}

}

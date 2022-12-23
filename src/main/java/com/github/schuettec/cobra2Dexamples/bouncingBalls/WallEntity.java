package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import java.awt.Dimension;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.common.Color;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public class WallEntity extends BasicRectangleEntity implements PolygonRenderable, Obstacle, Updatable {

	public WallEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		renderPolygon(getCollisionShapeInWorldCoordinates(), renderer, position);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public Color getDrawColor() {
		return Color.GRAY;
	}

	@Override
	public void update(Map map, Controller controller) {
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

	public void moveLeft() {
		double degrees = modulo360(this.getDegrees() + 1);
		setDegrees(degrees);
	}

	private double modulo360(double d) {
		if (d > 360) {
			return (d % 360);
		} else if (d < 0) {
			return 360 + d;
		}
		return d;
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
}

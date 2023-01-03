package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import java.util.Optional;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.CollisionMap;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

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
	public void update(WorldAccess worldAccess, float deltaTime, Controller controller) {

		CollisionMap collisionMap = worldAccess.detectCollision(this, worldAccess.getObstaclesExcept(this), true, true,
		    false);

		Optional<Collision> ballCollision = collisionMap.getCollisions()
		    .stream()
		    .filter(c -> c.getOpponent() instanceof BallEntity)
		    .findFirst();

		if (ballCollision.isEmpty()) {
			if (controller.isPlusKeyPressed()) {
				this.rotateClockwise();
			}
			if (controller.isMinusKeyPressed()) {
				this.rotateCounterClockwise();
			}
		}
	}

	public void rotateCounterClockwise() {
		double degrees = modulo360(this.getDegrees() + 1);
		setDegrees(degrees);
	}

	public void rotateClockwise() {
		double degrees = modulo360(this.getDegrees() - 1);
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
}

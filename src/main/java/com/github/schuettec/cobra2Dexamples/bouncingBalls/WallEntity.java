package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import java.util.Optional;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Collision;
import com.github.schuettec.cobra2d.world.CollisionMap;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class WallEntity extends BasicRectangleEntity implements PolygonRenderable, Obstacle, Updatable, Controllable {

	private int desiredRotation;

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
	public void processControllerState(WorldAccess worldAccess, Controller controller) {
		if (controller.isPlusKeyPressed()) {
			this.desiredRotation = +1;
		}
		if (controller.isMinusKeyPressed()) {
			this.desiredRotation = -1;
		}
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		CollisionMap collisionMap = worldAccess.getCollisions().detectCollision(this,
				HasCollisionShape::getCollisionShapeInWorldCoordinates, worldAccess.getObstaclesExcept(this),
				HasCollisionShape::getCollisionShapeInWorldCoordinates, true, true, false);

		Optional<Collision> ballCollision = collisionMap.getCollisions().stream()
				.filter(c -> c.getOpponent() instanceof BallEntity).findFirst();

		if (ballCollision.isEmpty()) {
			double degrees = modulo360(this.getDegrees() + desiredRotation);
			setDegrees(degrees);
		}
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

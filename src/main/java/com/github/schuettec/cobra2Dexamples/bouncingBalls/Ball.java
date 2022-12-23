package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import java.util.List;
import java.util.Optional;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.Collisions;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.common.Color;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public class Ball extends BasicCircleEntity implements CircleRenderable, Updatable, Obstacle {

	double VELOCITY = 2.0;

	double speed = 0;

	public Ball(Point worldCoordinates, double radius) {
		super(worldCoordinates, radius);
		this.speed = VELOCITY;
		this.setDegrees(Math2D.random(0, 360));
	}

	@Override
	public void update(Controller controller, List<Collision> collisions) {

		Optional<Collision> wallCollision = collisions.stream()
		    .filter(c -> c.getOpponent() instanceof WallEntity)
		    .findFirst();

		if (wallCollision.isPresent()) {
			// Berechne Einfallswinkel.
			Collision collision = wallCollision.get();
			HasCollisionShape opponent = collision.getOpponent();
			Collisions.detectCollision(getCollisionShapeInWorldCoordinates(), opponent.getCollisionShapeInWorldCoordinates(), false);
		} else {
			Point nextPosition = Math2D.getCircle(getPosition(), speed, getDegrees());
			this.setPosition(nextPosition);
		}
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
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

}
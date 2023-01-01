package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.CollisionDetail;
import com.github.schuettec.cobra2d.entity.CollisionMap;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

public class BallEntity extends BasicCircleEntity implements CircleRenderable, Updatable, Obstacle {

	double currentSpeed;

	public BallEntity(Point worldCoordinates, double radius, double speed, double degrees) {
		super(worldCoordinates, radius);
		this.currentSpeed = speed;
		this.setDegrees(degrees);
	}

	@Override
	public void update(Cobra2DWorld map, float deltaTime, Controller controller) {
		double frameSpeed = currentSpeed * deltaTime;

		// Calculate the collision shape at next frame
		Point nextPosition = Math2D.getCircle(getPosition(), frameSpeed, getDegrees());
		Circle nextShape = getCollisionShape(true, true, false).translate(nextPosition);
		CollisionMap collisionMap = map.detectCollision(nextShape, map.getObstaclesExcept(this), true, true, false);

		List<Collision> collisions = collisionMap.getCollisions();
		for (Collision collision : collisions) {
			HasCollisionShape opponent = collision.getOpponent();
			if (opponent instanceof WallEntity) {
				Set<Line> alreadyManagedLines = new HashSet<>();
				for (CollisionDetail c : collision.getCollisionDetails()) {
					// Berechne Einfallswinkel.
					Line opponentLine = c.getOpponentLine();

					if (!alreadyManagedLines.contains(opponentLine)) {
						alreadyManagedLines.add(opponentLine);

						// Point nextPosition = Math2D.getCircle(getPosition(), speed, getDegrees());
						// Line movement = new Line(getPosition(), nextPosition);

						double myAngle = getDegrees();
						double opponentAngle = Math2D.getAngle(opponentLine.getX1(), opponentLine.getX2());

						double normalizedMyAngle = modulo360(myAngle - opponentAngle);

						double newAngle = 360 - normalizedMyAngle;
						double resultAngle = modulo360(newAngle + opponentAngle);
						setDegrees(resultAngle);
					}
				}
			} else {
				// Meh physics engine?
				// double myAngle = getDegrees();
				// double opponentAngle = opponent.getDegrees();
				//
				// double normalizedMyAngle = modulo360(myAngle - opponentAngle);
				// double newAngle = 360 - normalizedMyAngle;
				// double resultAngle = modulo360(newAngle + opponentAngle);
				// setDegrees(resultAngle);
			}
		}

		// // Calculate the collision shape at next frame
		// nextPosition = Math2D.getCircle(getPosition(), frameSpeed, getDegrees());
		// nextShape = getCollisionShape(true, true, false).translate(nextPosition);
		//
		// collisionMap = map.detectCollision(nextShape, map.getObstaclesExcept(this), true, true, false);
		// wallCollision = collisionMap.getCollisions()
		// .stream()
		// .filter(c -> c.getOpponent() instanceof WallEntity)
		// .findFirst();
		//
		// if (wallCollision.isPresent()) {
		// setDegrees(modulo360(getDegrees() + 15));
		// nextPosition = Math2D.getCircle(getPosition(), frameSpeed, getDegrees());
		// this.setPosition(nextPosition);
		// } else {
		nextPosition = Math2D.getCircle(getPosition(), frameSpeed, getDegrees());
		this.setPosition(nextPosition);
		// }
	}

	private double modulo360(double d) {
		if (d > 360) {
			return (d % 360);
		} else if (d < 0) {
			return 360 + d;
		}
		return d;
	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {
		Circle collisionShape = getCollisionShapeInWorldCoordinates();
		renderCircle(collisionShape, renderer, screenTranslation);
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
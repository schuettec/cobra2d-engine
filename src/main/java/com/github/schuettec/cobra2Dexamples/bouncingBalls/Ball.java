package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;

public class Ball extends BasicCircleEntity implements CircleRenderable, Updatable {

	double VELOCITY = 2.0;

	double speed = 0;

	public Ball(Point worldCoordinates, double radius) {
		super(worldCoordinates, radius);
		this.speed = VELOCITY;
		this.setDegrees(Math2D.random(0, 360));
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

	@Override
	public void update(Controller controller, List<Collision> collisions) {
		Point nextPosition = Math2D.getCircle(getPosition(), speed, getDegrees());
		this.setPosition(nextPosition);
	}

}
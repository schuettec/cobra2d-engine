package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;

public class Ball extends BasicCircleEntity implements Updatable {

	double VELOCITY = 2.0;

	double speed = 0;

	public Ball(Point worldCoordinates, double radius) {
		super(worldCoordinates, radius);
		this.speed = VELOCITY;
		this.setDegrees(Math2D.random(0, 360));
	}

	@Override
	public void update(Controller controller, List<Collision> collisions) {
		Point nextPosition = Math2D.getCircle(getPosition(), speed, getDegrees());
		this.setPosition(nextPosition);
	}

}
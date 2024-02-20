package com.github.schuettec.cobra2Dexamples.networking.simpleCar;

import static com.github.schuettec.cobra2d.math.Math2D.getCircle;
import static com.github.schuettec.cobra2d.math.Math2D.normalizeAngle;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class SimpleCarEntity extends TexturedEntity implements Controllable, Updatable {

	private static final int TURNING_SPEED = 200;
	private static final int SPEED = 400;

	float desiredTurning = 0;
	private int desiredSpeed;

	public SimpleCarEntity(String textureId, Point worldCoordinates, Dimension intialDimension, int layer) {
		super(textureId, worldCoordinates, intialDimension, layer);
	}

	@Override
	public void processControllerState(WorldAccess worldAccess, Controller controller) {
		if (controller.isUpKeyPressed()) {
			this.desiredSpeed = SPEED;
		} else if (controller.isDownKeyPressed()) {
			this.desiredSpeed = -SPEED;
		} else {
			this.desiredSpeed = 0;
		}

		if (controller.isLeftKeyPressed()) {
			desiredTurning = TURNING_SPEED;
		} else if (controller.isRightKeyPressed()) {
			desiredTurning = -TURNING_SPEED;
		} else {
			desiredTurning = 0;
		}
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		float frameSpeed = 0;

		float turnSpeed = 0;

		turnSpeed = desiredTurning * deltaTime;

		frameSpeed = desiredSpeed * deltaTime;

		// Calculate the collision shape at next frame
		setDegrees(normalizeAngle(getDegrees() + turnSpeed));
		Point nextPosition = getCircle(getPosition(), frameSpeed, getDegrees());
		setPosition(nextPosition);
	}

}

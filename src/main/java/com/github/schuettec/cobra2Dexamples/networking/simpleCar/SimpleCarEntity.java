package com.github.schuettec.cobra2Dexamples.networking.simpleCar;

import static com.github.schuettec.cobra2d.math.Math2D.getCircle;
import static com.github.schuettec.cobra2d.math.Math2D.normalizeAngle;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class SimpleCarEntity extends TexturedEntity {

	private static final int TURNING_SPEED = 200;
	private static final int SPEED = 400;

	public SimpleCarEntity(String textureId, Point worldCoordinates, Dimension intialDimension, int layer,
	    boolean playerControlled) {
		super(textureId, worldCoordinates, intialDimension, layer, playerControlled);
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime, Controller controller) {
		super.update(worldAccess, deltaTime, controller);

		float frameSpeed = 0;

		float turnSpeed = 0;

		if (controller.isLeftKeyPressed()) {
			turnSpeed = TURNING_SPEED * deltaTime;
		} else if (controller.isRightKeyPressed()) {
			turnSpeed = -TURNING_SPEED * deltaTime;
		} else {
			turnSpeed = 0;
		}
		if (controller.isUpKeyPressed()) {
			frameSpeed = SPEED * deltaTime;
		} else if (controller.isDownKeyPressed()) {
			frameSpeed = -SPEED * deltaTime;
		} else {
			frameSpeed = 0;
		}

		System.out.println("turnSpeed  " + turnSpeed);

		// Calculate the collision shape at next frame
		setDegrees(normalizeAngle(getDegrees() + turnSpeed));
		Point nextPosition = getCircle(getPosition(), frameSpeed, getDegrees());
		setPosition(nextPosition);

		System.out.println(nextPosition);
	}

}

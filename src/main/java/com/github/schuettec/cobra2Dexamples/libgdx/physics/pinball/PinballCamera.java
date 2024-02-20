package com.github.schuettec.cobra2Dexamples.libgdx.physics.pinball;

import static java.util.Objects.nonNull;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class PinballCamera extends BasicRectangleMapCamera {

	private PhysicsBallEntity ball;

	public PinballCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled) {
		super(worldCoordinates, dimension, playerControlled);
	}

	public PinballCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled, Point screenPosition) {
		super(worldCoordinates, dimension, playerControlled, screenPosition);
	}

	@Override
	public void processControllerState(WorldAccess worldAccess, Controller controller) {
		super.processControllerState(worldAccess, controller);

		if (controller.isJustTouched()) {
			if (nonNull(ball)) {
				worldAccess.removeEntity(ball);
			}
			this.ball = new PhysicsBallEntity(new Point(240, 140), 15, 0, 30);
			ball.setPosition(controller.getMousePositionWorldCoordinates());
			worldAccess.spawnEntity(ball);
		}
	}
}

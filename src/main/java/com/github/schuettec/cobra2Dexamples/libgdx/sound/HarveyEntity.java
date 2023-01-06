package com.github.schuettec.cobra2Dexamples.libgdx.sound;

import com.github.schuettec.cobra2Dexamples.libgdx.animation.AnimationEntity;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.camera.InputContext;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class HarveyEntity extends AnimationEntity implements Updatable {

	private static final float ANIMATION_FRAME_DURATION = 0.06f;
	private static final float DEFAULT_STEP_SPEED = 3.25f; // per second
	private static final float DEFAULT_TURNING_SPEED = 0.16f; // per second

	private float stepSpeed;
	private float turningSpeed;

	private float desiredSpeed;

	private boolean running;
	private String headTextureId;
	private Point desiredDiection;

	public HarveyEntity(String headTextureId, String staticAnimationTextureId, String animationTextureId,
	    Point worldCoordinates, Dimension initialDimension, int frameCols, int frameRows, int layer,
	    boolean playerControlled) {
		super(staticAnimationTextureId, animationTextureId, worldCoordinates, initialDimension, frameCols, frameRows,
		    ANIMATION_FRAME_DURATION, layer, playerControlled);
		this.headTextureId = headTextureId;
		this.stepSpeed = DEFAULT_STEP_SPEED;
		this.turningSpeed = DEFAULT_TURNING_SPEED;
	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {
		super.render(renderer, screenTranslation);
		renderHead(renderer, screenTranslation);
	}

	private void renderHead(RendererAccess renderer, Point screenTranslation) {
		Point texturePosition = renderer.getTexturePosition(headTextureId, getPosition(), screenTranslation);
		renderer.drawTexture(headTextureId, 1f, texturePosition.getRoundX(), texturePosition.getRoundY(),
		    (float) getDegrees());
	}

	@Override
	public void processControllerState(Controller controller) {
		this.running = controller.isWKeyPressed() || controller.isSKeyPressed();

		InputContext cameraRelativeInput = controller.getCameraRelativeInput();

		if (controller.isWKeyPressed()) {
			this.stepSpeed = Math.abs(this.stepSpeed);
		}

		if (controller.isSKeyPressed()) {
			this.stepSpeed = -Math.abs(this.stepSpeed);
		}

		if (controller.isAKeyPressed()) {
			desiredSpeed = -turningSpeed;
		} else if (controller.isDKeyPressed()) {
			desiredSpeed = -turningSpeed;
		} else {
			desiredSpeed = 0;
		}

		this.desiredDiection = cameraRelativeInput.getMouseWorldCoordinates();
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		double angle = Math2D.getAngle(getPosition(), desiredDiection);
		setDegrees(angle);
		this.setDegrees(this.getDegrees() - this.turningSpeed * deltaTime);

		if (this.running) {
			final Point newPos = Math2D.getCircle(this.getPosition(), this.stepSpeed, this.getDegrees());
			this.setPosition(newPos);
		} else {
			stateTime = 0;
		}
	}

}

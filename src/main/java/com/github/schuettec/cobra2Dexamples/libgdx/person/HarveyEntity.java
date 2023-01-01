package com.github.schuettec.cobra2Dexamples.libgdx.person;

import com.github.schuettec.cobra2Dexamples.libgdx.animation.AnimationEntity;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.camera.InputContext;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

public class HarveyEntity extends AnimationEntity {

	private static final float ANIMATION_FRAME_DURATION = 0.06f;
	private static final float DEFAULT_STEP_SPEED = 3.25f; // per second
	private static final float DEFAULT_TURNING_SPEED = 0.16f; // per second

	private float stepSpeed;
	private float turningSpeed;

	private boolean running;
	private String headTextureId;

	public HarveyEntity(String headTextureId, String staticAnimationTextureId, String animationTextureId,
	    Point worldCoordinates, int frameCols, int frameRows, int layer, boolean playerControlled) {
		super(staticAnimationTextureId, animationTextureId, worldCoordinates, frameCols, frameRows,
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
		renderer.drawTexture(headTextureId, 1f, (float) texturePosition.getRoundX(), (float) texturePosition.getRoundY(),
		    (float) getDegrees());
	}

	@Override
	public void update(Cobra2DWorld map, float deltaTime, Controller controller) {
		this.running = controller.isWKeyPressed() || controller.isSKeyPressed();

		InputContext cameraRelativeInput = controller.getCameraRelativeInput();
		Point mouseWorldCoordinates = cameraRelativeInput.getMouseWorldCoordinates();
		double angle = Math2D.getAngle(getPosition(), mouseWorldCoordinates);
		setDegrees(angle);

		if (controller.isWKeyPressed()) {
			this.stepSpeed = Math.abs(this.stepSpeed);
		}

		if (controller.isSKeyPressed()) {
			this.stepSpeed = -Math.abs(this.stepSpeed);
		}

		if (controller.isAKeyPressed()) {
			this.setDegrees(this.getDegrees() - this.turningSpeed * deltaTime);
		}

		if (controller.isDKeyPressed()) {
			this.setDegrees(this.getDegrees() + this.turningSpeed * deltaTime);
		}

		if (this.running) {
			final Point newPos = Math2D.getCircle(this.getPosition(), this.stepSpeed, this.getDegrees());
			this.setPosition(newPos);
		} else {
			stateTime = 0;
		}
	}

}
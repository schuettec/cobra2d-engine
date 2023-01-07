package com.github.schuettec.cobra2Dexamples.libgdx.person;

import com.github.schuettec.cobra2Dexamples.libgdx.animation.AnimationEntity;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.state.EntityStateValue;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class HarveyEntity extends AnimationEntity implements Updatable, Controllable {

	private static final float ANIMATION_FRAME_DURATION = 0.06f;
	private static final float DEFAULT_STEP_SPEED = 3.25f; // per second
	private static final float DEFAULT_TURNING_SPEED = 0.16f; // per second

	private float stepSpeed;
	private float turningSpeed;

	@EntityStateValue
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
		if (!running) {
			setStateTime(0);
		}
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
		if (controller.isWKeyPressed()) {
			this.stepSpeed = DEFAULT_STEP_SPEED;
		} else if (controller.isSKeyPressed()) {
			this.stepSpeed = -DEFAULT_STEP_SPEED;
		} else {
			this.stepSpeed = 0;
		}

		this.desiredDiection = controller.getMousePositionWorldCoordinates();
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		double angle = Math2D.getAngle(getPosition(), desiredDiection);
		setDegrees(angle);
		this.setDegrees(this.getDegrees() - this.turningSpeed * deltaTime);

		if (this.running) {
			final Point newPos = Math2D.getCircle(this.getPosition(), this.stepSpeed, this.getDegrees());
			this.setPosition(newPos);
		}
	}

}

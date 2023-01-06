package com.github.schuettec.cobra2Dexamples.libgdx.blending;

import static com.github.schuettec.cobra2d.math.Math2D.getCircle;
import static com.github.schuettec.cobra2d.math.Math2D.normalizeAngle;

import java.util.function.BiFunction;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Parabel;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxExtendedAccess;
import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxRenderable;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class PoliceCarEntity extends TexturedEntity implements LibGdxRenderable, Updatable, Controllable {

	enum State {
		FORWARDS,
		BACKWARDS,
		ROLLOUT,
		BRAKE;
	}

	enum Steering {
		LEFT,
		RIGHT,
		NONE;
	}

	/**
	 * Interval in milliseconds to switch between blue and red lights.
	 */
	private static final float MAX_SPEED = 15f;
	private static final float SECONDS_TO_BRAKE = 1;
	private static final float SECONDS_TO_MAX_SPEED = 2;
	private static final float SECONDS_TO_ROLL_OUT = 4;

	/*
	 * How much steering can be done is based on speed. If the car drives slower, then a smaller turning radius can be
	 * achieved. If the car is driving fast, the steering speed is reduced.
	 */
	private static final float TURN_DEGREES = 45f;
	private static final float MIN_SECONDS_TO_TURN_DEGREES = 0.16f;
	private static final float MAX_SECONDS_TO_TURN_DEGREES = 0.30f;
	private static final Line STEERING_SPEED_FUNCTION = new Line(new Point(0, MIN_SECONDS_TO_TURN_DEGREES),
	    new Point(MAX_SPEED, MAX_SECONDS_TO_TURN_DEGREES));

	/**
	 * Interval in milliseconds to switch between blue and red lights.
	 */
	private static final long LIGHT_SWITCH_INTERVAL = 500;

	/**
	 * The alpha function.
	 */
	private static Parabel alphaParabel = new Parabel(new Point(0, 0), new Point(LIGHT_SWITCH_INTERVAL / 2.0, 1));

	/**
	 * If true, the lights are rendered and switched on.
	 */
	private boolean lightsOn = true;

	/**
	 * Last timestamp the lights switched. In milliseconds.
	 */
	private long lastSwitchTimestamp = 0;
	/**
	 * If true the blue light, otherwise the red light is rendered
	 */
	private boolean blue = true;

	/**
	 * The current alpha value.
	 */
	private float alpha;

	/**
	 * The speed of the car.
	 */
	private float speed;

	/**
	 * True if the car currently brakes.
	 */
	private boolean brake;

	private String redLightTextureId;
	private String blueLightTextureId;
	private String frontLightTextureId;
	private String brakeLightTextureId;
	private String brakeLightColorTextureId;
	private String policeRedAlarmLightTextureId;
	private String policeBlueAlarmLightTextureId;

	private State desiredState;
	private Steering desiredSteering;

	public PoliceCarEntity(String carTextureId, String policeRedAlarmLightTextureId, String policeBlueAlarmLightTextureId,
	    String redLightTextureId, String blueLightTextureId, String frontLightTextureId, String brakeLightTextureId,
	    String brakeLightColorTextureId, Point worldCoordinates, Dimension initialDimension, int layer,
	    boolean playerControlled) {
		super(carTextureId, worldCoordinates, initialDimension, layer, playerControlled);
		this.policeRedAlarmLightTextureId = policeRedAlarmLightTextureId;
		this.policeBlueAlarmLightTextureId = policeBlueAlarmLightTextureId;
		this.redLightTextureId = redLightTextureId;
		this.blueLightTextureId = blueLightTextureId;
		this.frontLightTextureId = frontLightTextureId;
		this.brakeLightTextureId = brakeLightTextureId;
		this.brakeLightColorTextureId = brakeLightColorTextureId;
	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {
		super.render(renderer, screenTranslation);

		Point texturePosition = renderer.getTexturePosition(getTextureId(), getPosition(), screenTranslation);

		if (brake) {
			renderBrakeLightColor(renderer, screenTranslation);
		}

		LibGdxExtendedAccess extendedRenderer = getExtendedRenderer(renderer);
		if (lightsOn) {
			int correction = 17;
			if (blue) {
				renderer.drawTexture(blueLightTextureId, alpha, (float) texturePosition.getRoundX() - correction,
				    (float) texturePosition.getRoundY() - 78, (float) getDegrees());

				extendedRenderer.drawLightTexture(policeBlueAlarmLightTextureId,
				    (float) texturePosition.getRoundX() - correction, (float) texturePosition.getRoundY() - 78,
				    (float) getDegrees(), 1f);

			} else {
				renderer.drawTexture(redLightTextureId, alpha, (float) texturePosition.getRoundX() - correction,
				    (float) texturePosition.getRoundY() - 78, (float) getDegrees());

				extendedRenderer.drawLightTexture(policeRedAlarmLightTextureId,
				    (float) texturePosition.getRoundX() - correction, (float) texturePosition.getRoundY() - 78,
				    (float) getDegrees(), 1f);
			}
		}

		renderFrontLight(renderer, screenTranslation, 10);
		renderFrontLight(renderer, screenTranslation, -10);

		if (brake) {
			renderBrakeLight(renderer, screenTranslation, 0);
		}
	}

	private void renderBrakeLightColor(RendererAccess renderer, Point screenTranslation) {
		Point lightTextureCenter = renderer.getTextureCenter(brakeLightColorTextureId);
		Dimension carTextureDimension = renderer.getTextureDimension(getTextureId());
		Point leftLight = getCircle(getPosition(), -carTextureDimension.getWidth() / 2.0 - 8, normalizeAngle(getDegrees()));
		leftLight = leftLight.translate(screenTranslation)
		    .translate(lightTextureCenter.clone()
		        .scale(-1));
		renderer.drawTexture(brakeLightColorTextureId, 1f, leftLight.getRoundX(), leftLight.getRoundY(), (float) degrees);
	}

	private void renderBrakeLight(RendererAccess renderer, Point screenTranslation, int currentDegrees) {
		LibGdxExtendedAccess extendedRenderer = getExtendedRenderer(renderer);
		Point lightTextureCenter = renderer.getTextureCenter(brakeLightTextureId);
		Dimension carTextureDimension = renderer.getTextureDimension(getTextureId());
		Point leftLight = Math2D.getCircle(getPosition(), -carTextureDimension.getWidth() / 2.0 - 8,
		    Math2D.normalizeAngle(getDegrees() + currentDegrees));
		leftLight = leftLight.translate(screenTranslation)
		    .translate(lightTextureCenter.clone()
		        .scale(-1));

		Dimension lightTextureDimension = renderer.getTextureDimension(brakeLightTextureId);
		extendedRenderer.drawLightTexture(brakeLightTextureId, leftLight.getRoundX(), leftLight.getRoundY(),
		    lightTextureCenter.getRoundX(), lightTextureCenter.getRoundY(), (float) lightTextureDimension.getWidth(),
		    (float) lightTextureDimension.getHeight(), 1, 1, (float) degrees, 0, 0, lightTextureDimension.getRoundWidth(),
		    lightTextureDimension.getRoundHeight(), false, false);
	}

	private void renderFrontLight(RendererAccess renderer, Point screenTranslation, int currentDegrees) {
		LibGdxExtendedAccess extendedRenderer = getExtendedRenderer(renderer);
		Point lightTextureCenter = renderer.getTextureCenter(frontLightTextureId);

		Dimension textureDimension = renderer.getTextureDimension(getTextureId());
		Point leftLight = getCircle(getPosition(), textureDimension.getWidth(),
		    normalizeAngle(getDegrees() + currentDegrees));
		leftLight = leftLight.translate(screenTranslation)
		    .translate(lightTextureCenter.clone()
		        .scale(-1));

		// renderer.fillCircle(leftLight.getRoundX(), leftLight.getRoundY(), 5, Color.YELLOW);

		Dimension lightTextureDimension = renderer.getTextureDimension(frontLightTextureId);
		extendedRenderer.drawLightTexture(frontLightTextureId, leftLight.getRoundX(), leftLight.getRoundY(),
		    lightTextureCenter.getRoundX(), lightTextureCenter.getRoundY(), (float) lightTextureDimension.getWidth(),
		    (float) lightTextureDimension.getHeight(), 1, 1, (float) degrees, 0, 0, lightTextureDimension.getRoundWidth(),
		    lightTextureDimension.getRoundHeight(), false, false);
	}

	@Override
	public void processControllerState(Controller controller) {
		if (controller.isUpKeyPressed()) {
			this.desiredState = State.FORWARDS;
		} else if (controller.isDownKeyPressed()) {
			this.desiredState = State.BACKWARDS;
		} else if (controller.isSpaceKeyPressed()) {
			this.desiredState = State.BRAKE;
		} else {
			this.desiredState = State.ROLLOUT;
		}

		if (controller.isLeftKeyPressed()) {
			this.desiredSteering = Steering.LEFT;
		} else if (controller.isRightKeyPressed()) {
			this.desiredSteering = Steering.RIGHT;
		} else {
			this.desiredSteering = Steering.NONE;
		}
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {

		float acceleration = MAX_SPEED / SECONDS_TO_MAX_SPEED * deltaTime;
		float brake = MAX_SPEED / SECONDS_TO_BRAKE * deltaTime;
		float rollout = MAX_SPEED / SECONDS_TO_ROLL_OUT * deltaTime;

		/**
		 * Calculate the steering speed
		 */
		float turnDegrees = 0;
		if (speed > 0) {
			float steeringSpeed = (float) STEERING_SPEED_FUNCTION.getValue(Math.abs(speed));
			turnDegrees = TURN_DEGREES / steeringSpeed * deltaTime;
			if (!STEERING_SPEED_FUNCTION.isDefined(new Point(speed, steeringSpeed))) {
				turnDegrees = 0;
			}
		}

		long currentTimeMillis = System.currentTimeMillis();

		if (lightsOn) {
			long lightTime = currentTimeMillis - lastSwitchTimestamp;
			this.alpha = (float) alphaParabel.getValue(Math.min(lightTime, LIGHT_SWITCH_INTERVAL));
			if (lightTime > LIGHT_SWITCH_INTERVAL) {
				lastSwitchTimestamp = currentTimeMillis;
				blue = !blue;
			}
		}

		if (State.FORWARDS.equals(desiredState)) {
			// Accelerate
			if (this.speed < 0) {
				this.brake = true;
				this.speed = Math.min(speed + brake, MAX_SPEED);
			} else {
				this.brake = false;
				this.speed = Math.min(speed + acceleration, MAX_SPEED);
			}
		} else if (State.BACKWARDS.equals(desiredState)) {
			if (this.speed > 0) {
				this.brake = true;
				this.speed = Math.max(speed - brake, -MAX_SPEED);
			} else {
				this.brake = false;
				this.speed = Math.max(speed - acceleration, -MAX_SPEED);
			}
		} else if (State.BRAKE.equals(desiredState)) {
			this.brake = true;
			float signum = Math.signum(this.speed) * -1;
			BiFunction<Float, Float, Float> minMax = signum >= 0 ? Math::min : Math::max;
			this.speed = minMax.apply(speed + signum * brake, 0f);
		} else {
			this.brake = false;
			float signum = Math.signum(this.speed) * -1;
			BiFunction<Float, Float, Float> minMax = signum >= 0 ? Math::min : Math::max;
			this.speed = minMax.apply(speed + signum * rollout, 0f);
		}

		if (Steering.LEFT.equals(desiredSteering)) {
			double newDegrees = getDegrees() + turnDegrees;
			setDegrees(Math2D.normalizeAngle(newDegrees));
		} else if (Steering.RIGHT.equals(desiredSteering)) {
			setDegrees(Math2D.normalizeAngle(getDegrees() - turnDegrees));
		}

		Point nextPosition = Math2D.getCircle(getPosition(), speed, getDegrees());
		this.setPosition(nextPosition);
	}

}

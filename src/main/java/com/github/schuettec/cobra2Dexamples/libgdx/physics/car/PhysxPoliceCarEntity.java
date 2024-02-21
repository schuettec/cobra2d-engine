package com.github.schuettec.cobra2Dexamples.libgdx.physics.car;

import static com.github.schuettec.cobra2d.math.Math2D.getCircle;
import static com.github.schuettec.cobra2d.math.Math2D.normalizeAngle;
import static com.github.schuettec.cobra2d.math.Math2D.saveRound;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.entity.skills.state.EntityStateValue;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Parabel;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;
import com.github.schuettec.cobra2d.math.Rectangle;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxExtendedAccess;
import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxRenderable;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class PhysxPoliceCarEntity extends TexturedEntity
		implements LibGdxRenderable, PhysicBody, Updatable, Controllable {

	int torque = 200;
	float m_maxForwardSpeed = 100;
	float m_maxBackwardSpeed = -20;
	float m_maxDriveForce = 150;

	/**
	 * Interval in milliseconds to switch between blue and red lights.
	 */
	private static final long LIGHT_SWITCH_INTERVAL = 500;

	/**
	 * The alpha function.
	 */
	private static Parabel alphaParabel = new Parabel(new Point(0, 0), new Point(LIGHT_SWITCH_INTERVAL / 2.0, 1));

	/**
	 * Last timestamp the lights switched. In milliseconds.
	 */
	private long lastSwitchTimestamp = 0;

	/**
	 * If true, the lights are rendered and switched on.
	 */
	@EntityStateValue
	private boolean lightsOn = true;

	/**
	 * The current alpha value.
	 */
	@EntityStateValue
	private float alpha;

	/**
	 * True if the car currently brakes.
	 */
	@EntityStateValue
	private boolean brake;

	/**
	 * If true the blue light, otherwise the red light is rendered
	 */
	@EntityStateValue
	private boolean blue = true;

	private String redLightTextureId;
	private String blueLightTextureId;
	private String frontLightTextureId;
	private String brakeLightTextureId;
	private String brakeLightColorTextureId;
	private String policeRedAlarmLightTextureId;
	private String policeBlueAlarmLightTextureId;

	/**
	 * Unit conversion: 1 unit in Box2D is 1 Meter in real world. We want to show a
	 * 3cm radius ball on the screen that has 30 Pixels radius.
	 */
	private float renderScaleConversionFactor = 1 / 100f;

	private Body body;
	private BodyDef bodyDef;

	private Dimension dimension;
	private Fixture fixture;

	float desiredSpeed = 0;
	float desiredTorque = 0;

	// if true, the user does not break or accelerate
	boolean roll = true;

	public PhysxPoliceCarEntity(String carTextureId, String policeRedAlarmLightTextureId,
			String policeBlueAlarmLightTextureId, String redLightTextureId, String blueLightTextureId,
			String frontLightTextureId, String brakeLightTextureId, String brakeLightColorTextureId,
			Point worldCoordinates, Dimension dimension, int layer, boolean playerControlled) {
		super(carTextureId, worldCoordinates, dimension, layer);
		this.policeRedAlarmLightTextureId = policeRedAlarmLightTextureId;
		this.policeBlueAlarmLightTextureId = policeBlueAlarmLightTextureId;
		this.redLightTextureId = redLightTextureId;
		this.blueLightTextureId = blueLightTextureId;
		this.frontLightTextureId = frontLightTextureId;
		this.brakeLightTextureId = brakeLightTextureId;
		this.brakeLightColorTextureId = brakeLightColorTextureId;

		this.dimension = dimension;

		createRectangleShape(dimension);
	}

	@Override
	protected void createCollisionShape(RendererAccess renderer) {
		createRectangleShape(dimension);
	}

	@Override
	public Dimension getCollisionShapeDimension() {
		return dimension;
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
		Point leftLight = getCircle(getPosition(), -carTextureDimension.getWidth() / 2.0 - 8,
				normalizeAngle(getDegrees()));
		leftLight = leftLight.translate(screenTranslation).translate(lightTextureCenter.clone().scale(-1));
		renderer.drawTexture(brakeLightColorTextureId, 1f, leftLight.getRoundX(), leftLight.getRoundY(),
				(float) degrees);
	}

	private void renderBrakeLight(RendererAccess renderer, Point screenTranslation, int currentDegrees) {
		LibGdxExtendedAccess extendedRenderer = getExtendedRenderer(renderer);
		Point lightTextureCenter = renderer.getTextureCenter(brakeLightTextureId);
		Dimension carTextureDimension = renderer.getTextureDimension(getTextureId());
		Point leftLight = Math2D.getCircle(getPosition(), -carTextureDimension.getWidth() / 2.0 - 8,
				Math2D.normalizeAngle(getDegrees() + currentDegrees));
		leftLight = leftLight.translate(screenTranslation).translate(lightTextureCenter.clone().scale(-1));

		Dimension lightTextureDimension = renderer.getTextureDimension(brakeLightTextureId);
		extendedRenderer.drawLightTexture(brakeLightTextureId, leftLight.getRoundX(), leftLight.getRoundY(),
				lightTextureCenter.getRoundX(), lightTextureCenter.getRoundY(),
				(float) lightTextureDimension.getWidth(), (float) lightTextureDimension.getHeight(), 1, 1,
				(float) degrees, 0, 0, lightTextureDimension.getRoundWidth(), lightTextureDimension.getRoundHeight(),
				false, false);
	}

	private void renderFrontLight(RendererAccess renderer, Point screenTranslation, int currentDegrees) {
		LibGdxExtendedAccess extendedRenderer = getExtendedRenderer(renderer);
		Point lightTextureCenter = renderer.getTextureCenter(frontLightTextureId);

		Dimension textureDimension = renderer.getTextureDimension(getTextureId());
		Point leftLight = getCircle(getPosition(), textureDimension.getWidth(),
				normalizeAngle(getDegrees() + currentDegrees));
		leftLight = leftLight.translate(screenTranslation).translate(lightTextureCenter.clone().scale(-1));

		// renderer.fillCircle(leftLight.getRoundX(), leftLight.getRoundY(), 5,
		// Color.YELLOW);

		Dimension lightTextureDimension = renderer.getTextureDimension(frontLightTextureId);
		extendedRenderer.drawLightTexture(frontLightTextureId, leftLight.getRoundX(), leftLight.getRoundY(),
				lightTextureCenter.getRoundX(), lightTextureCenter.getRoundY(),
				(float) lightTextureDimension.getWidth(), (float) lightTextureDimension.getHeight(), 1, 1,
				(float) degrees, 0, 0, lightTextureDimension.getRoundWidth(), lightTextureDimension.getRoundHeight(),
				false, false);
	}

	@Override
	public void processControllerState(WorldAccess worldAccess, Controller controller) {
		if (controller.isUpKeyPressed()) {
			desiredSpeed = m_maxForwardSpeed;
			roll = false;
		} else if (controller.isDownKeyPressed()) {
			desiredSpeed = m_maxBackwardSpeed;
			roll = false;
		} else if (controller.isSpaceKeyPressed()) {
			// TODO: Handbrake!!!
			roll = false;
		} else {
			roll = true;
			desiredSpeed = 0;
		}

		if (controller.isLeftKeyPressed()) {
			desiredTorque = torque;
		} else if (controller.isRightKeyPressed()) {
			desiredTorque = -torque;
		} else {
			desiredTorque = 0;
		}

	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		long currentTimeMillis = System.currentTimeMillis();

		if (lightsOn) {
			long lightTime = currentTimeMillis - lastSwitchTimestamp;
			this.alpha = (float) alphaParabel.getValue(Math.min(lightTime, LIGHT_SWITCH_INTERVAL));
			if (lightTime > LIGHT_SWITCH_INTERVAL) {
				lastSwitchTimestamp = currentTimeMillis;
				blue = !blue;
			}
		}

		updateFriction();

		// find current speed in forward direction
		Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0, 1)).cpy();
		float currentSpeed = getForwardVelocity().dot(currentForwardNormal);

		if (roll) {
			brake = false;
		} else {
			// apply necessary force
			float force = 0;
			if (desiredSpeed > currentSpeed) {
				force = m_maxDriveForce;
				this.brake = false;
				body.applyForce(currentForwardNormal.scl(force), body.getWorldCenter(), true);
			} else if (desiredSpeed < currentSpeed) {
				force = -m_maxDriveForce;
				this.brake = currentSpeed > 0;
				body.applyForce(currentForwardNormal.scl(force), body.getWorldCenter(), true);
			}
		}

		body.applyTorque(desiredTorque, true);

		setPositionByPhysicsBody();
		setDegreesByPhysicsBody();

	}

	private void setDegreesByPhysicsBody() {
		double degrees = Math2D.normalizeAngle(Math.toDegrees(body.getAngle()) + 90);
		setDegrees(degrees);
	}

	private void setPositionByPhysicsBody() {
		Vector2 position = body.getPosition();
		int x = saveRound(position.x / renderScaleConversionFactor);
		int y = saveRound(position.y / renderScaleConversionFactor);
		setPosition(x, y);
	}

	private float getSpeed() {
		return body.getLinearVelocity().len();
	}

	Vector2 getLateralVelocity() {
		Vector2 currentRightNormal = body.getWorldVector(new Vector2(1, 0));
		return currentRightNormal.scl(currentRightNormal.dot(body.getLinearVelocity()));
	}

	Vector2 getForwardVelocity() {

		Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0, 1));
		return currentForwardNormal.scl(currentForwardNormal.dot(body.getLinearVelocity()));
	}

	void updateFriction() {
		// lateral linear velocity
		float maxLateralImpulse = 2.5f;
		Vector2 impulse = getLateralVelocity().scl(-body.getMass());
		if (impulse.len() > maxLateralImpulse) {
			impulse = impulse.scl(maxLateralImpulse / impulse.len());
		}
		body.applyLinearImpulse(impulse, body.getWorldCenter(), true);

		// angular velocity
		body.applyAngularImpulse(0.1f * body.getInertia() * -body.getAngularVelocity(), true);

		// forward linear velocity
		Vector2 currentForwardNormal = getForwardVelocity();
		float currentForwardSpeed = currentForwardNormal.len();
		currentForwardNormal.nor();

		// Chris: Evtl. manipulate this for drifting with handbrake. Lower value causes
		// the car to slide with less friction.
		float dragForceFactore = 10;

		float dragForceMagnitude = -dragForceFactore * currentForwardSpeed;

		Vector2 force = currentForwardNormal.scl(dragForceMagnitude);
		body.applyForce(force, body.getWorldCenter(), true);
	}

	@Override
	public void configureBodyDef(BodyDef bodyDef) {
		bodyDef.angle = (float) Math.toRadians(270);
		bodyDef.allowSleep = true;
	}

	@Override
	public void createFixture(Body body) {
		PolygonShape polygonShape = new PolygonShape();

		// Be very careful here: The angle in the physics world is different to
		// renderer/cobra2D-world angle.
		// An angle of 0° in physics engine is "north", in cobra2d/libgdx the angle of
		// 0° is "east".
		// Rotate to match physics angle;
		Polygon collisionShape = getCollisionShape(true, false, false).rotate(90);
		Rectangle huellRect = Math2D.getHuellRect(collisionShape.getPoints());
		Dimension dimension = huellRect.getDimension();
		float width = (float) dimension.getWidth() * renderScaleConversionFactor;
		float height = (float) dimension.getHeight() * renderScaleConversionFactor;
		polygonShape.setAsBox(width / 2.0f, height / 2.0f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.density = 5f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0.5f;
		// Create our fixture and attach it to the body
		this.fixture = body.createFixture(fixtureDef);
		this.body = body;
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public void setBody(Body body) {
		this.body = body;
	}
}

package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import static com.github.schuettec.cobra2d.math.Math2D.saveRound;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg.LegBuilder;
import com.github.schuettec.cobra2Dexamples.walkinganimation.WalkAnimationController;
import com.github.schuettec.cobra2Dexamples.walkinganimation.WalkController;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.RectangleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;
import com.github.schuettec.cobra2d.math.Rectangle;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class PhysicsWalkingEntity extends BasicRectangleEntity
    implements CircleRenderable, RectangleRenderable, PhysicBody, Updatable, Controllable {

	private static final int MAX_STEP = 50;

	private double currentStep = 0;

	private Leg leg1;

	private Leg leg2;

	private boolean shouldLeft;

	private boolean shouldRun;

	private boolean shouldFast;

	private boolean shouldCrouch;

	private WalkController walkController;

	private WalkAnimationController walkAnimationController;
	private StepUpAnimationController stepUpAnimationController;

	private Body body;

	private double forceToApply;

	/**
	 * Unit conversion: 1 unit in Box2D is 1 Meter in real world.
	 * We want to show a
	 * 3cm radius ball on the screen that has 30 Pixels radius.
	 */
	private float renderScaleConversionFactor = 1 / 100f;

	private double radius;

	public PhysicsWalkingEntity(Point worldCoordinates, Dimension dimension, double radius, double forceToApply,
	    double degrees) {
		super(worldCoordinates, dimension);
		this.radius = radius;
		this.forceToApply = forceToApply;
		this.setDegrees(degrees);

		LegBuilder builder = Leg.newLeg()
		    .setRenderDebugPoints(false)
		    .setLegLength(radius)
		    .setMaxStep(MAX_STEP);
		this.leg1 = builder.build();
		this.leg2 = builder.build();
		this.walkController = new WalkController();
		this.walkAnimationController = new WalkAnimationController(MAX_STEP, radius, 97, 40d, 80d, 30d);
		this.stepUpAnimationController = new StepUpAnimationController(MAX_STEP, radius, 97, 40d, 80d, 30d);
	}

	@Override
	public void configureBodyDef(BodyDef bodyDef) {
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 1f;
		bodyDef.angularDamping = 1f;
	}

	@Override
	public Fixture createFixture(Body body) {
		PolygonShape polygonShape = new PolygonShape();
		Polygon collisionShape = getCollisionShape(true, false, false);
		Rectangle huellRect = Math2D.getHuellRect(collisionShape.getPoints());
		Dimension dimension = huellRect.getDimension();
		float width = (float) dimension.getWidth() * renderScaleConversionFactor;
		float height = (float) dimension.getHeight() * renderScaleConversionFactor;
		polygonShape.setAsBox(width / 2.0f, height / 2.0f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.density = 1f;
		fixtureDef.friction = 1f;
		fixtureDef.restitution = 0f;
		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);
		this.body = body;
		return fixture;
	}

	@Override
	public void processControllerState(WorldAccess worldAccess, Controller controller) {

		if (controller.isShiftLeftKeyPressed() || controller.isShiftRightKeyPressed()) {
			this.shouldFast = true;
		} else {
			this.shouldFast = false;
		}

		if (controller.isCtrlLeftKeyPressed() || controller.isCtrlRightKeyPressed()) {
			this.shouldCrouch = true;
		} else {
			this.shouldCrouch = false;
		}

		if (controller.isLeftKeyPressed()) {
			shouldLeft = true;
		}
		if (controller.isRightKeyPressed()) {
			shouldLeft = false;
		}

		if (controller.isLeftKeyPressed() || controller.isRightKeyPressed()) {
			shouldRun = true;
		} else {
			shouldRun = false;
		}
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {

		walkController.updateWalk(this, worldAccess);

		if ((shouldLeft && walkController.isStepUpLeft()) || (!shouldLeft && walkController.isStepUpRight())) {
			if (shouldRun) {
				body.setLinearVelocity(0, 1);
			}
		} else {

			// if run
			Vector2 currentVelocity = this.getBody()
			    .getLinearVelocity();
			if (shouldRun) {
				body.setLinearVelocity((shouldLeft ? -1f : 1f) * (float) forceToApply, currentVelocity.y);
			} else {
				body.setLinearVelocity(0, currentVelocity.y);
			}
		}

		Vector2 position = body.getPosition();
		float radians = body.getAngle();
		double degrees = Math.toDegrees(radians);
		setDegrees(degrees);
		setPosition(saveRound(position.x / renderScaleConversionFactor),
		    saveRound(position.y / renderScaleConversionFactor));
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		// PolygonRenderable.renderPolygon(getCollisionShapeInWorldCoordinates(), renderer, position, Color.MAGENTA);

		// Circle circle = new Circle(new Point(), radius).translate(getPosition());
		// CircleRenderable.renderCircle(circle, renderer, position, Color.MAGENTA);

		if (walkController.isStepUpLeft()) {
			Point point = walkController.getNextStepPointLeft()
			    .clone()
			    .translate(position);
			renderer.fillCircle(point.getFloatX() - 2, point.getFloatY() - 2, 4, Color.CYAN);
		}

		if (walkController.isStepUpRight()) {
			Point point = walkController.getNextStepPointRight()
			    .clone()
			    .translate(position);
			renderer.fillCircle(point.getFloatX() - 2, point.getFloatY() - 2, 4, Color.YELLOW);
		}

		// --- Calculate max point
		Point bodyPosition = getPosition().clone();

		if (walkController.isStepUpLeft() || walkController.isStepUpRight()) {
			stepUpAnimationController.setSourcePoint(bodyPosition);
			Point nextStepPoint = walkController.getNextStepPoint();
			stepUpAnimationController.setTargetPoint(nextStepPoint);

			leg1.calculateStep(bodyPosition, stepUpAnimationController, shouldLeft, 0)
			    .render(renderer, position);

			leg2.calculateStep(bodyPosition, walkAnimationController, shouldLeft, 0)
			    .render(renderer, position);

			setPosition(getPosition().x, getPosition().y + 1);
		} else {

			walkAnimationController.setCrouch(shouldCrouch);
			walkAnimationController.setFast(shouldFast);

			double leg1CurrentStep = 0;
			double leg2CurrentStep = 0;

			if (shouldRun) {
				leg1CurrentStep = (currentStep + (MAX_STEP / 2.)) % MAX_STEP;
				leg2CurrentStep = currentStep;
			}

			leg1.calculateStep(bodyPosition, walkAnimationController, shouldLeft, leg1CurrentStep)
			    .render(renderer, position);

			leg2.calculateStep(bodyPosition, walkAnimationController, shouldLeft, leg2CurrentStep)
			    .render(renderer, position);

			// Step control
			if (shouldRun) {
				currentStep = (currentStep + 1) % MAX_STEP;
			} else {
				currentStep = 0;
			}
		}

		// drawSensorLines(renderer, position);
	}

	private void drawSensorLines(RendererAccess renderer, Point position) {
		Line[] sensorLines = walkController.getSensorLines();

		Point leftSensor1 = sensorLines[0].getX1()
		    .translate(position);
		Point leftSensor2 = sensorLines[0].getX2()
		    .translate(position);
		renderer.drawLine(leftSensor1.getFloatX(), leftSensor1.getFloatY(), leftSensor2.getFloatX(),
		    leftSensor2.getFloatY(), Color.GREEN);

		Point rightSensor1 = sensorLines[1].getX1()
		    .translate(position);
		Point rightSensor2 = sensorLines[1].getX2()
		    .translate(position);
		renderer.drawLine(rightSensor1.getFloatX(), rightSensor1.getFloatY(), rightSensor2.getFloatX(),
		    rightSensor2.getFloatY(), Color.GREEN);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public Color getDrawColor() {
		return Color.GREEN;
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public float getDensity() {
		return 0;
	}

	@Override
	public BodyType getBodyType() {
		return BodyType.DynamicBody;
	}

	@Override
	public void setBody(Body body) {
		this.body = body;
	}
}

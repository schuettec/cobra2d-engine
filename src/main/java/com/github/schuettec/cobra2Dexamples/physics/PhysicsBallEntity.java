package com.github.schuettec.cobra2Dexamples.physics;

import static com.github.schuettec.cobra2d.math.Math2D.saveRound;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.DynamicBody;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

public class PhysicsBallEntity extends BasicCircleEntity implements CircleRenderable, DynamicBody, Updatable {

	private Body body;

	private double forceToApply;

	private Fixture fixture;

	/**
	 * Unit conversion: 1 unit in Box2D is 1 Meter in real world. We want to show a 3cm radius ball on the screen that has
	 * 30 Pixels radius.
	 */
	private double renderScaleConversionFactor = 1000;

	public PhysicsBallEntity(Point worldCoordinates, double radius, double forceToApply, double degrees) {
		super(worldCoordinates, radius);
		this.forceToApply = forceToApply;
		this.setDegrees(degrees);
	}

	@Override
	public BodyDef createBodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(getPosition().getRoundX(), getPosition().getRoundY());
		bodyDef.angle = getRadians();
		bodyDef.angularDamping = 0f;
		bodyDef.linearDamping = 0f;
		return bodyDef;
	}

	@Override
	public void createFixture(Body body) {
		CircleShape shape = new CircleShape();
		float radius = (float) getCollisionShape(true, false, false).getRadius();
		shape.setRadius(radius);
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 1f;
		// Create our fixture and attach it to the body
		this.fixture = body.createFixture(fixtureDef);
		// Apply initial force
		Vector2 vForce = PhysicBody.getDegreesAndForceAsVector(getDegrees(), forceToApply);
		body.applyForce(vForce, new Vector2(), true);
		this.body = body;
	}

	@Override
	public void update(Cobra2DWorld map, float deltaTime, Controller controller) {
		Vector2 position = body.getPosition();
		System.out.println(position);
		float radians = body.getAngle();
		double degrees = Math.toDegrees(radians);
		setDegrees(degrees);
		setPosition(saveRound(position.x), saveRound(position.y));
	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {
		Circle collisionShape = getCollisionShapeInWorldCoordinates().scale(renderScaleConversionFactor);
		renderCircle(collisionShape, renderer, screenTranslation);
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
}

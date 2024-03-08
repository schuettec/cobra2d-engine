package com.github.schuettec.cobra2Dexamples.libgdx.physics.pinball;

import static com.github.schuettec.cobra2d.math.Math2D.saveRound;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class BallEntity extends BasicCircleEntity implements CircleRenderable, PhysicBody, Updatable, Obstacle {

	private Body body;

	private double forceToApply;

	private Fixture fixture;

	/**
	 * Unit conversion: 1 unit in Box2D is 1 Meter in real world. We want to show a
	 * 3cm radius ball on the screen that has 30 Pixels radius.
	 */
	private float renderScaleConversionFactor = 1 / 100f;

	public BallEntity(Point worldCoordinates, double radius, double forceToApply, double degrees) {
		super(worldCoordinates, radius);
		this.forceToApply = forceToApply;
		this.setDegrees(degrees);
	}

	@Override
	public void configureBodyDef(BodyDef bodyDef) {
	}

	@Override
	public Fixture createFixture(Body body) {
		CircleShape shape = new CircleShape();
		double worldRadius = getCollisionShape(true, false, false).getRadius();
		float radius = (float) worldRadius * renderScaleConversionFactor - 0.0125f; // if we do not adjust the radius,
																					// the
		// collision only exists in the physics
		// engine but not in cobra2d. Adjust by
		shape.setRadius(radius);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 2f;
		fixtureDef.friction = 1f;
		fixtureDef.restitution = 0.3f;
		// Create our fixture and attach it to the body
		this.fixture = body.createFixture(fixtureDef);
		// Apply initial force
		Vector2 vForce = PhysicBody.getDegreesAndForceAsVector(getDegrees(), forceToApply);
		body.applyForce(vForce, new Vector2(), true);
		this.body = body;

		return fixture;
	}

	@Override
	public float getDensity() {
		return 0.5f;
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		Vector2 position = body.getPosition();
		float radians = body.getAngle();
		double degrees = Math.toDegrees(radians);
		setDegrees(degrees);
		setPosition(saveRound(position.x / renderScaleConversionFactor),
				saveRound(position.y / renderScaleConversionFactor));
	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {
		Circle collisionShape = getCollisionShapeInWorldCoordinates();
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

	@Override
	public void setBody(Body body) {
		this.body = body;
	}

}
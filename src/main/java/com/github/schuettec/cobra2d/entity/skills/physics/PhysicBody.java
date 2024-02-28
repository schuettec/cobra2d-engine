package com.github.schuettec.cobra2d.entity.skills.physics;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.world.WorldAccess;

public interface PhysicBody extends PhysicObject, Updatable, Obstacle {
	/**
	 * Returns a {@link BodyDef} with the initial entity position and rotation.
	 *
	 * @param bodyType The body type.
	 * @return Returns the body definition.
	 */
	public default BodyDef getBodyDef(BodyType bodyType) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(toPhysicsValue(getPosition().getRoundX()), toPhysicsValue(getPosition().getRoundY()));
		bodyDef.angle = getRadians();
		configureBodyDef(bodyDef);
		return bodyDef;
	}

	/**
	 * This method is used to modify pre-configured body def.
	 *
	 * @param bodyDef The pre-configured body def.
	 */
	public default void configureBodyDef(BodyDef bodyDef) {

	}

	/**
	 * Called by the phsyics engine to create a fixture shape for the specified
	 * body. Instances should cache the pointer to body, to receive physics updates
	 * for the entity.
	 *
	 * @param body The body representing an entity in the physics engine.
	 * @return
	 */
	public default Fixture createFixture(Body body) {
		PolygonShape polygonShape = getBodyFromPolygonCollisionShape();
		Fixture fixture = body.createFixture(polygonShape, getDensity());
		configureFixture(fixture);
		setBody(body);
		return fixture;
	}

	/**
	 * @param body Sets the body as representation in physics environment.
	 */
	public void setBody(Body body);

	/**
	 * This method is used to modify pre-configured fixture.
	 *
	 * @param fixture The pre-configured fixture.
	 */
	public default void configureFixture(Fixture fixture) {
	}

	/**
	 * @return Returns the density of the entity.
	 */
	public default float getDensity() {
		return 5f;
	}

	/**
	 * @return Returns the body type.
	 */
	public default BodyType getBodyType() {
		return BodyType.DynamicBody;
	}

	public static Vector2 getDegreesAsVector(double degrees) {
		return new Vector2((float) cos(toRadians(degrees)), (float) sin(toRadians(degrees)));
	}

	public static Vector2 getDegreesAndForceAsVector(double degrees, double force) {
		return new Vector2((float) (cos(toRadians(degrees)) * force), (float) (sin(toRadians(degrees)) * force));
	}

	public default PolygonShape getBodyFromPolygonCollisionShape() {
		PolygonShape polygonShape = new PolygonShape();
		List<Point> entityPoints = getCollisionShape(false, false, false).getPoints();
		int numberOfPoints = entityPoints.size();
		Vector2[] vertices = new Vector2[numberOfPoints];
		for (int i = 0; i < numberOfPoints; i++) {
			Point ep = entityPoints.get(i);
			vertices[i] = new Vector2();
			vertices[i].x = toPhysicsValue(ep.getX());
			vertices[i].y = toPhysicsValue(ep.getY());
		}
		polygonShape.set(vertices);
		return polygonShape;
	}

	/**
	 * Updates the entities position by the current values computed by the physics
	 * engine.
	 */
	public default void updateEntityPositionByPhysics() {
		Point newPosition = new Point(getBody().getPosition().x, getBody().getPosition().y);
		newPosition = newPosition.scale(getWorldScale());
		this.setPosition(newPosition);
	}

	/**
	 * Updates the entities rotation by the current values computed by the physics
	 * engine.
	 */
	public default void updateEntityRotationByPhysics() {
		float rotation = Math2D.toDegrees(getBody().getAngle());
		this.setDegrees(rotation);
	}

	@Override
	public default void update(WorldAccess worldAccess, float deltaTime) {
		updateEntityPositionByPhysics();
		updateEntityRotationByPhysics();
	}

	/**
	 * @return Returns the body definition for the physics engine.
	 */
	public default BodyDef createBodyDef() {
		BodyDef bodyDef = getBodyDef(getBodyType());
		return bodyDef;
	}

	/**
	 * @return Returns the physics body.
	 */
	public Body getBody();

}

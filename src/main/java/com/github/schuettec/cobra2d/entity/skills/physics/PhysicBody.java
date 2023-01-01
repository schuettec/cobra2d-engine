package com.github.schuettec.cobra2d.entity.skills.physics;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.github.schuettec.cobra2d.entity.skills.Skill;

public interface PhysicBody extends Skill {

	/**
	 * @return Returns the body definition for the physics engine.
	 */
	public BodyDef createBodyDef();

	/**
	 * Called by the phsyics engine to create a fixture shape for the specified body. Instances should cache the pointer
	 * to body, to receive physics updates for the entity.
	 * 
	 * @param body The body representing an entity in the physics engine.
	 */
	public void createFixture(Body body);

	/**
	 * @return Returns the physics body.
	 */
	public Body getBody();

	public static Vector2 getDegreesAsVector(double degrees) {
		return new Vector2((float) cos(toRadians(degrees)), (float) sin(toRadians(degrees)));
	}

	public static Vector2 getDegreesAndForceAsVector(double degrees, double force) {
		return new Vector2((float) (cos(toRadians(degrees)) * force), (float) (sin(toRadians(degrees)) * force));
	}
}

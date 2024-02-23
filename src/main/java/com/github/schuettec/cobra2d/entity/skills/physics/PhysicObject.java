package com.github.schuettec.cobra2d.entity.skills.physics;

import com.github.schuettec.cobra2d.entity.skills.Skill;

/**
 * Main interface for physical object in the engine world.
 */
public interface PhysicObject extends Skill {
	public default float toPhysicsValue(float worldValue) {
		return worldValue * getPhysicsScale();
	}

	public default float toWorldValue(float physicsValue) {
		return physicsValue / getPhysicsScale();
	}

	public default float toPhysicsValue(double worldValue) {
		return (float) (worldValue * getPhysicsScale());
	}

	public default float toWorldValue(double physicsValue) {
		return (float) (physicsValue / getPhysicsScale());
	}

	/**
	 * Unit conversion: 1 unit in Box2D is 1 Meter in real world. We want to show a
	 * 3cm radius ball on the screen that has 30 Pixels radius.
	 */
	public default float getWorldScale() {
		return 100f;
	}

	/**
	 * Unit conversion: 1 unit in Box2D is 1 Meter in real world. We want to show a
	 * 3cm radius ball on the screen that has 30 Pixels radius.
	 */
	public default float getPhysicsScale() {
		return 1 / getWorldScale();
	}
}

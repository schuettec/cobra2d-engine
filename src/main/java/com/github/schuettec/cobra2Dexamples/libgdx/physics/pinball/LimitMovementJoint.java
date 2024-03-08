package com.github.schuettec.cobra2Dexamples.libgdx.physics.pinball;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicJoint;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class LimitMovementJoint implements Updatable, PhysicJoint {

	private String id;

	private PrismaticJoint joint;
	private PhysicBody sensor;
	private PhysicBody moveableBody;

	private long upperTranslationInWorldDistance;

	public LimitMovementJoint(PhysicBody sensor, PhysicBody moveableBody, long upperTranslationInWorldDistance) {
		this.id = createId();
		this.sensor = sensor;
		this.moveableBody = moveableBody;
		this.upperTranslationInWorldDistance = upperTranslationInWorldDistance;

	}

	@Override
	public JointDef createJointDef() {
		PrismaticJointDef jointDef = new PrismaticJointDef();
		jointDef.bodyA = sensor.getBody();
		jointDef.bodyB = moveableBody.getBody();
		jointDef.localAnchorA.set(0f, 0f);
		jointDef.localAnchorB.set(0f, 0f);
		jointDef.referenceAngle = (float) Math.toRadians(moveableBody.getDegrees());
		jointDef.collideConnected = true;
		jointDef.enableLimit = true;
		jointDef.lowerTranslation = 0;
		jointDef.upperTranslation = toPhysicsValue(upperTranslationInWorldDistance);
		return jointDef;
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
	}

	@Override
	public void setJoint(Joint joint) {
		this.joint = (PrismaticJoint) joint;
	}

	@Override
	public Joint getJoint() {
		return joint;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

}

package com.github.schuettec.cobra2Dexamples.libgdx.physics.pinball;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicJoint;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.utils.TimedBoolean;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class RevoluteJointEntity implements Updatable, Controllable, PhysicJoint {

	private String id;

	private RevoluteJoint joint;
	private PhysicBody body1;
	private PhysicBody body2;

	private TimedBoolean leftKeyState;

	public RevoluteJointEntity(PhysicBody body1, PhysicBody body2) {
		this.id = createId();
		this.body1 = body1;
		this.body2 = body2;
		this.leftKeyState = new TimedBoolean();

	}

	@Override
	public JointDef createJointDef() {
		RevoluteJointDef revJointDef = new RevoluteJointDef();
		revJointDef.bodyA = body1.getBody();
		revJointDef.bodyB = body2.getBody();
		revJointDef.localAnchorA.set(0f, 0f);
		revJointDef.localAnchorB.set(0f, 0f);
		revJointDef.referenceAngle = Math2D.toRadians(330);
		revJointDef.collideConnected = false;
		revJointDef.enableLimit = true;
		revJointDef.upperAngle = Math2D.toRadians(61);
		revJointDef.lowerAngle = Math2D.toRadians(0);
		revJointDef.enableMotor = true;
		revJointDef.motorSpeed = 0;
		return revJointDef;
	}

	@Override
	public void processControllerState(WorldAccess worldAccess, Controller controller) {
		if (controller.isLeftKeyPressed()) {
			this.leftKeyState.start();
			joint.setMotorSpeed(240);
		} else {
			this.leftKeyState.reset();
			joint.setMotorSpeed(0);
		}
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
//System.out.println(joint.);
	}

	@Override
	public void setJoint(Joint joint) {
		this.joint = (RevoluteJoint) joint;
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

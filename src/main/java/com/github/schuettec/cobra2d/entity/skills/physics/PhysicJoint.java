package com.github.schuettec.cobra2d.entity.skills.physics;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.github.schuettec.cobra2d.entity.skills.Updatable;

public interface PhysicJoint extends PhysicObject, Updatable {

	JointDef createJointDef();

	void setJoint(Joint joint);

	Joint getJoint();

}

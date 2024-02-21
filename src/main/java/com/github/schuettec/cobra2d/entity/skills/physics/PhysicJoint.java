package com.github.schuettec.cobra2d.entity.skills.physics;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.github.schuettec.cobra2d.entity.skills.Skill;
import com.github.schuettec.cobra2d.entity.skills.Updatable;

public interface PhysicJoint extends Skill, Updatable {

	JointDef createJointDef();

	void setJoint(Joint joint);

	Joint getJoint();

}

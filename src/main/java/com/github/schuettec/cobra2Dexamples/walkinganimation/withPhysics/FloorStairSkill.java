package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Shape;

public interface FloorStairSkill extends HasCollisionShape {

	Shape getStepUpSensorLeft();

	Point getStepUpPointLeft();

	Shape getStepUpSensorRight();

	Point getStepUpPointRight();
}

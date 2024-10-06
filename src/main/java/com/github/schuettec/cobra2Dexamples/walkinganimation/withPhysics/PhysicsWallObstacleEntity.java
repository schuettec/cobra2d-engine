package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;

public class PhysicsWallObstacleEntity
    extends PhysicsObstacleEntity {

  public PhysicsWallObstacleEntity(Point worldCoordinates,
      Dimension dimension) {
    super(worldCoordinates, dimension);
  }

}

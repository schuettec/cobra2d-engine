package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import java.util.Optional;

import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Point;

public class PhysicsWalkFloorEntity
    extends PhysicsObstacleEntity {

  public PhysicsWalkFloorEntity(Point worldCoordinates,
      Dimension dimension) {
    super(worldCoordinates, dimension);
  }

  public Line getWalkLine() {
    Dimension collisionShapeDimension = getCollisionShapeDimension();
    double width = collisionShapeDimension.getWidth();
    double height = collisionShapeDimension.getHeight();
    double widthHalf = width / 2.;
    double heightHalf = height / 2.;
    Point s = new Point(getPosition().x - widthHalf,
        getPosition().y + heightHalf);
    Point e = new Point(getPosition().x + widthHalf,
        getPosition().y + heightHalf);
    return new Line(s, e);
  }

  public Optional<Point> getNextStep(double xPos) {
    Line walkLine = getWalkLine();
    if (walkLine
        .isDefined(new Point(xPos, walkLine.getX2().y))) {
      double value = walkLine.getValue(xPos);
      return Optional.of(new Point(value, walkLine.getX2().y));
    } else {
      return Optional.empty();
    }
  }

}

package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import static java.util.Objects.isNull;

import com.github.schuettec.cobra2Dexamples.walkinganimation.WalkAnimationController;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Point;

public class StepUpAnimationController
    extends WalkAnimationController {

  private Point targetPoint;
  private Point sourcePoint;

  public StepUpAnimationController(double maxStep,
      double legLength, double stepSize, double stepHeight,
      double stepHeightFast, double crouchHeight) {
    super(maxStep, legLength, stepSize, stepHeight,
        stepHeightFast, crouchHeight);

  }

  public Point getSourcePoint() {
    return targetPoint;
  }

  public void setSourcePoint(Point sourcePoint) {
    this.sourcePoint = sourcePoint;
  }

  public Point getTargetPoint() {
    return targetPoint;
  }

  public void setTargetPoint(Point targetPoint) {
    this.targetPoint = targetPoint;
  }

  @Override
  public AnimationResult calculateTargetByStep(
      Point worldCoordinates, double currentStep) {

    if (isNull(sourcePoint)) {
      throw new RuntimeException("Target step point is null!");
    }
    if (isNull(targetPoint)) {
      throw new RuntimeException("Source step point is null!");
    }

    Line interpolation = null;
    // interpolation = new Line(targetPoint, sourcePoint);

    interpolation = new Line(targetPoint, sourcePoint);
    double minX = interpolation.getMinX();
    double maxX = interpolation.getMaxX();

    double x = minX + currentStep / maxStep * (maxX - minX);

    double y = interpolation.getValue(x);

    return new AnimationResult(worldCoordinates, new Point(x, y),
        null);
  }

}

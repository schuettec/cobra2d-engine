package com.github.schuettec.cobra2Dexamples.walkinganimation;

import com.github.schuettec.cobra2d.math.HarmonicOscillation;
import com.github.schuettec.cobra2d.math.HarmonicOscillation.Type;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;

public class WalkAnimationController
    implements LegAnimationController {

  private HarmonicOscillation ellipsisOscilatorX;
  private HarmonicOscillation ellipsisOscilatorY;

  private double stepSize = 250d;
  private double stepHeight = 80d;
  private double crouchHeight = 80d;

  private double radius;
  private boolean crouch = false;

  public WalkAnimationController(double maxStep,
      double legLength, double stepSize, double stepHeight) {
    this.radius = legLength;

    this.stepSize = stepSize;
    this.stepHeight = stepHeight;

    this.ellipsisOscilatorX = new HarmonicOscillation(maxStep,
        stepSize, 0.5d, Type.COSINUS, false);
    this.ellipsisOscilatorY = new HarmonicOscillation(maxStep,
        stepHeight, 0.5d, Type.SINUS, true);
  }

  @Override
  public AnimationResult calculateTargetByStep(
      Point worldCoordinates, double currentStep) {

    double radius = this.radius;
    double stepHeight = this.stepHeight;

    Point hip = worldCoordinates.clone();

    if (crouch) {
      hip.translate(0, -crouchHeight);
      radius = this.radius - crouchHeight;
      stepHeight = crouchHeight
          * (this.stepHeight / this.radius);
    }

    ellipsisOscilatorX.setAmplitude(stepSize);
    ellipsisOscilatorY.setAmplitude(stepHeight);
    double sX = ellipsisOscilatorX.apply(currentStep);
    double sY = ellipsisOscilatorY.apply(currentStep);

    Point positionSchwingungselipseZentrum = Math2D
        .getCircle(hip.clone()
            .translate(0, stepHeight / 2.), radius, 270d);

    Point schwingungsEllipsePunkt = new Point(sX, sY).clone()
        .translate(positionSchwingungselipseZentrum);

    return new AnimationResult(hip,
        normalizePoint(hip, schwingungsEllipsePunkt, radius));
  }

  protected Point normalizePoint(Point hipPosition,
      Point targetPoint, double radius) {
    Point bodyPosition = hipPosition.clone();
    double distance = Math2D.getEntfernung(bodyPosition,
        targetPoint);
    double angle = Math2D.getAngle(bodyPosition, targetPoint);
    if (distance > radius) {
      Point normalizedTarget = Math2D.getCircle(bodyPosition,
          radius, angle);
      return normalizedTarget;
    }
    return targetPoint;
  }

  public boolean isCrouch() {
    return crouch;
  }

  public void setCrouch(boolean crouch) {
    this.crouch = crouch;
  }

  public double getRadius() {
    return radius;
  }

  public void setRadius(double radius) {
    this.radius = radius;
  }

  public double getStepSize() {
    return stepSize;
  }

  public void setStepSize(double ellipsisWalkAnimMaxX) {
    this.stepSize = ellipsisWalkAnimMaxX;
  }

  public double getStepHeight() {
    return stepHeight;
  }

  public void setStepHeight(double ellipsisWalkAnimMaxY) {
    this.stepHeight = ellipsisWalkAnimMaxY;
  }

}

package com.github.schuettec.cobra2Dexamples.walkinganimation;

import java.util.LinkedList;
import java.util.List;

import com.github.schuettec.cobra2d.math.HarmonicOscillation;
import com.github.schuettec.cobra2d.math.HarmonicOscillation.Type;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;

public class WalkAnimationController
    implements LegAnimationController {

  private HarmonicOscillation ellipsisOscilatorX;
  private HarmonicOscillation ellipsisOscilatorY;

  private double maxStep;

  private double stepSize = 250d;
  private double stepHeight = 80d;
  private double stepHeightFast = 130d;
  private double crouchHeight = 80d;

  private double radius;
  private boolean crouch = false;
  private boolean fast = false;

  public WalkAnimationController(double maxStep,
      double legLength, double stepSize, double stepHeight,
      double stepHeightFast) {
    this.maxStep = maxStep;
    this.radius = legLength;

    this.stepSize = stepSize;
    this.stepHeight = stepHeight;
    this.stepHeightFast = stepHeightFast;

    this.ellipsisOscilatorX = new HarmonicOscillation(maxStep,
        stepSize, 0.5d, Type.COSINUS, false);
    this.ellipsisOscilatorY = new HarmonicOscillation(maxStep,
        stepHeight, 0.5d, Type.SINUS, true);
  }

  @Override
  public AnimationResult calculateTargetByStep(
      Point worldCoordinates, double currentStep) {

    double radius = this.radius;
    double stepSize = this.stepSize;
    double stepHeight = this.stepHeight;

    Point hip = worldCoordinates.clone();

    if (crouch) {
      hip.translate(0, -crouchHeight);
      radius = this.radius - crouchHeight;
      stepHeight = crouchHeight
          * (this.stepHeight / this.radius);
    }

    if (fast) {
      if (crouch) {
        stepHeight = this.stepHeight - (this.stepHeight / 3.);
        stepSize = this.stepSize + (this.stepSize / 5.);
      } else {
        stepHeight = this.stepHeightFast;
      }
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

    List<Point> debugTargetPoints = debugTargetPoints(
        positionSchwingungselipseZentrum, ellipsisOscilatorX,
        ellipsisOscilatorY, maxStep);

    return new AnimationResult(hip, schwingungsEllipsePunkt,
        debugTargetPoints);
  }

  private List<Point> debugTargetPoints(Point translation,
      HarmonicOscillation ellipsisOscilatorX,
      HarmonicOscillation ellipsisOscilatorY, double maxStep) {
    List<Point> points = new LinkedList<>();
    for (int i = 0; i < maxStep; i++) {
      points.add(new Point(ellipsisOscilatorX.apply((double) i),
          ellipsisOscilatorY.apply((double) i))
              .translate(translation));
    }
    return points;
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

  public double getStepHeightFast() {
    return stepHeightFast;
  }

  public void setStepHeightFast(double stepHeightFast) {
    this.stepHeightFast = stepHeightFast;
  }

  public double getCrouchHeight() {
    return crouchHeight;
  }

  public void setCrouchHeight(double crouchHeight) {
    this.crouchHeight = crouchHeight;
  }

  public boolean isFast() {
    return fast;
  }

  public void setFast(boolean fast) {
    this.fast = fast;
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

  public HarmonicOscillation getEllipsisOscilatorX() {
    return ellipsisOscilatorX;
  }

  public HarmonicOscillation getEllipsisOscilatorY() {
    return ellipsisOscilatorY;
  }

}

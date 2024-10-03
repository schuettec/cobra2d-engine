package com.github.schuettec.cobra2Dexamples.walkinganimation;

import com.github.schuettec.cobra2Dexamples.moveableShapes.MoveableCircleEntity;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg.LegBuilder;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class WalkingEntity extends MoveableCircleEntity {

  private static final int MAX_STEP = 50;

  private int currentStep = 0;

  private Leg leg1;

  private Leg leg2;

  private Point mousePoint;

  public WalkingEntity(Point worldCoordinates, double radius,
      boolean playerControlled) {
    super(worldCoordinates, radius, playerControlled);
    LegBuilder builder = Leg.newLeg()
        .setLegLength(radius)
        .setWinkelVorschwungOberschenkel(60)
        .setWinkelRückschwungOberschenkel(40)
        .setMaxStep(MAX_STEP);
    this.leg1 = builder.build();
    this.leg2 = builder.build();
  }

  @Override
  public void processControllerState(WorldAccess worldAccess,
      Controller controller) {
    super.processControllerState(worldAccess, controller);
    this.mousePoint = controller
        .getMousePositionWorldCoordinates();
  }

  @Override
  public void render(RendererAccess renderer, Point position) {
    super.render(renderer, position);

    // --- Calculate max point
    Point mousePointNormalized = mousePoint.clone();
    double distance = Math2D.getEntfernung(getPosition(),
        mousePoint);
    double mouseAngle = Math2D.getAngle(getPosition(),
        mousePoint);
    if (distance > getRadius()) {
      mousePointNormalized = Math2D.getCircle(getPosition(),
          getRadius(), mouseAngle);
    }

    Point mousePointScreen = mousePointNormalized.clone()
        .translate(position);
    renderer.drawRectangle(mousePointScreen.getFloatX() - 5,
        mousePointScreen.getFloatY() - 5, 10, 10,
        getDrawColor());

    // -- Schwingungsellipse 1
    double maxX = 250d;
    double maxY = 80d;

    // Leg 1
    {
      double sX = schwingungX(maxX, MAX_STEP, currentStep);
      double sY = schwingungY(maxY, MAX_STEP, currentStep);

      Point positionSchwingungselipseZentrum = Math2D
          .getCircle(getPosition().clone()
              .translate(0, maxY / 2.), getRadius(), 270d);

      Point schwingungsEllipsePunkt = new Point(sX, sY).clone()
          .translate(positionSchwingungselipseZentrum);

      Point schwingungsEllipsePunktScreen = schwingungsEllipsePunkt
          .clone()
          .translate(position);

      renderer.drawRectangle(
          schwingungsEllipsePunktScreen.getFloatX() - 5,
          schwingungsEllipsePunktScreen.getFloatY() - 5, 10, 10,
          getDrawColor());

      // Invers kinematics leg 1
      leg1.berechneWinkel(getPosition(), schwingungsEllipsePunkt) // mousePointNormalized
          .render(renderer, position);
    }

    // Leg 2
    {
      double sX = schwingungX(maxX, MAX_STEP,
          (currentStep + 25) % MAX_STEP);
      double sY = schwingungY(maxY, MAX_STEP,
          (currentStep + 25) % MAX_STEP);

      Point positionSchwingungselipseZentrum = Math2D
          .getCircle(getPosition().clone()
              .translate(0, maxY / 2.), getRadius(), 270d);

      Point schwingungsEllipsePunkt = new Point(sX, sY).clone()
          .translate(positionSchwingungselipseZentrum);

      Point schwingungsEllipsePunktScreen = schwingungsEllipsePunkt
          .clone()
          .translate(position);

      renderer.drawRectangle(
          schwingungsEllipsePunktScreen.getFloatX() - 5,
          schwingungsEllipsePunktScreen.getFloatY() - 5, 10, 10,
          getDrawColor());

      // Invers kinematics leg 1
      leg2.berechneWinkel(getPosition(), schwingungsEllipsePunkt)
          .render(renderer, position);
    }

    // Step control
    currentStep = (currentStep + 1) % MAX_STEP;

  }

  public double schwingungX(double maxX, double maxStep,
      double step) {
    return (maxX / 2.)
        * Math.cos((2 * Math.PI) / maxStep * step + Math.PI)
        + 1d;
  }

  public double schwingungY(double maxY, double maxStep,
      double step) {
    return (maxY / 2.)
        * -Math.sin((2 * Math.PI) / maxStep * step + Math.PI)
        + 1d;
  }

}

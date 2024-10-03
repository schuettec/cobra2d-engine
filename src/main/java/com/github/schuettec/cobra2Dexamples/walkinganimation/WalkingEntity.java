package com.github.schuettec.cobra2Dexamples.walkinganimation;

import static com.github.schuettec.cobra2d.math.Math2D.toDegrees;
import static java.lang.Math.acos;
import static java.lang.Math.pow;

import com.github.schuettec.cobra2Dexamples.moveableShapes.MoveableCircleEntity;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg.LegBuilder;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
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

    leg1.berechneWinkel(getPosition(), mousePointNormalized)
        .render(renderer, position);

    // -- Schwingungsellipse
    double sX = 5 * Math.cos(currentStep);
    double sY = 2 * Math.sin(currentStep);
    Point positionSchwingungselipseZentrum = Math2D
        .getCircle(getPosition(), getRadius(), 270d);
    Point schwingungsEllipsePunkt = new Point(sX, sY)
        .translate(positionSchwingungselipseZentrum)
        .translate(position);

    renderer.drawRectangle(
        schwingungsEllipsePunkt.getFloatX() - 5,
        schwingungsEllipsePunkt.getFloatY() - 5, 10, 10,
        getDrawColor());

    currentStep = (currentStep + 1) % MAX_STEP;

  }

  public void render1(RendererAccess renderer, Point position) {
    super.render(renderer, position);

    Point center = getPosition().clone()
        .translate(position);
    renderer.fillCircle((float) center.x + 5,
        (float) center.y - 5, 10f, Color.RED);

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

    // Berechne Figurlängen
    double verhältnisOberschenkelUnterschenkel = 1.2d;
    double unterschenkelLänge = getRadius()
        / (verhältnisOberschenkelUnterschenkel + 1);
    double oberschenkelLänge = getRadius() - unterschenkelLänge;

    // Berechne Dreieck
    Point start = getPosition().clone();
    Point ziel = mousePointNormalized.clone();
    // Nochmal Distanz d in Weltkoordinaten
    double d = Math2D.getEntfernung(start, ziel);
    double a = d;
    double b = oberschenkelLänge;
    double c = unterschenkelLänge;

    double alpha = acos(
        (pow(b, 2) + pow(c, 2) - pow(a, 2)) / (2. * b * c));
    double beta = acos(
        (pow(a, 2) + pow(c, 2) - pow(b, 2)) / (2. * a * c));
    double gamma = acos(
        (pow(a, 2) + pow(b, 2) - pow(c, 2)) / (2. * a * b));

    double oberschenkelWinkel = toDegrees(gamma) + mouseAngle;
    double unterschenkelwinkel = toDegrees(alpha)
        + oberschenkelWinkel + 180;
    Point p1 = start.clone();
    Point p2 = Math2D.getCircle(start, b, oberschenkelWinkel);
    Point p3 = Math2D.getCircle(p2, c, unterschenkelwinkel);

    Point p1Screen = p1.clone()
        .translate(position);
    Point p2Screen = p2.clone()
        .translate(position);
    Point p3Screen = p3.clone()
        .translate(position);

    // Render legs
    renderer.drawLine(p1Screen.getFloatX(), p1Screen.getFloatY(),
        p2Screen.getFloatX(), p2Screen.getFloatY(),
        Color.CHARTREUSE);

    renderer.drawLine(p2Screen.getFloatX(), p2Screen.getFloatY(),
        p3Screen.getFloatX(), p3Screen.getFloatY(),
        Color.FIREBRICK);

    // Render it
    renderer.drawLine((float) center.x, (float) center.y,
        mousePointNormalized.clone()
            .translate(position)
            .getFloatX(),
        mousePointNormalized.clone()
            .translate(position)
            .getFloatY(),
        Color.CHARTREUSE);

    // --- End Max point

    leg1.calculateStep(getPosition().clone(), currentStep)
        .render(renderer, position);
    leg2.calculateStep(getPosition().clone(),
        currentStep + (MAX_STEP / 2))
        .render(renderer, position);

    currentStep = (currentStep + 1) % MAX_STEP;

  }

}

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

  private boolean left;

  private boolean run;

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

    if (controller.isLeftKeyPressed()) {
      left = true;
      run = true;
    } else {
      left = false;
    }

    if (controller.isRightKeyPressed()) {
      run = true;
    }

    if (!controller.isLeftKeyPressed()
        && !controller.isRightKeyPressed()) {
      run = false;
    }

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

    leg1.calculateStep(getPosition(), left,
        (currentStep + 25) % MAX_STEP)
        .render(renderer, position);

    leg1.calculateStep(getPosition(), left, currentStep)
        .render(renderer, position);

    System.out.println("RUn" + run);
    // Step control
    if (run) {
      currentStep = (currentStep + 1) % MAX_STEP;
    } else {
      currentStep = 0;
    }
  }

}

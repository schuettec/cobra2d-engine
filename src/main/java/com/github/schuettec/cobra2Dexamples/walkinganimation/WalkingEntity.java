package com.github.schuettec.cobra2Dexamples.walkinganimation;

import com.github.schuettec.cobra2Dexamples.moveableShapes.MoveableCircleEntity;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg.LegBuilder;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class WalkingEntity extends MoveableCircleEntity {

  private static final int MAX_STEP = 50;

  private double currentStep = 0;

  private Leg leg1;

  private Leg leg2;

  private Point mousePoint;

  private boolean left;

  private boolean run;

  private boolean fast;

  private boolean crouch;

  private WalkAnimationController walkAnimationController;

  public WalkingEntity(Point worldCoordinates, double radius) {
    super(worldCoordinates, radius, false);
    LegBuilder builder = Leg.newLeg()
        .setLegLength(radius)
        .setMaxStep(MAX_STEP);
    this.leg1 = builder.build();
    this.leg2 = builder.build();
    this.walkAnimationController = new WalkAnimationController(
        MAX_STEP, radius, 250d, 80d, 100d);
  }

  @Override
  public void processControllerState(WorldAccess worldAccess,
      Controller controller) {
    super.processControllerState(worldAccess, controller);
    this.mousePoint = controller
        .getMousePositionWorldCoordinates();

    if (controller.isShiftLeftKeyPressed()
        || controller.isShiftRightKeyPressed()) {
      this.fast = true;
    } else {
      this.fast = false;
    }

    if (controller.isCtrlLeftKeyPressed()
        || controller.isCtrlRightKeyPressed()) {
      this.crouch = true;
    } else {
      this.crouch = false;
    }

    if (controller.isLeftKeyPressed()) {
      left = true;
    }
    if (controller.isRightKeyPressed()) {
      left = false;
    }

    if (controller.isLeftKeyPressed()
        || controller.isRightKeyPressed()) {
      run = true;
    } else {
      run = false;
    }
  }

  @Override
  public void render(RendererAccess renderer, Point position) {
    super.render(renderer, position);

    // --- Calculate max point
    Point bodyPosition = getPosition().clone();

    walkAnimationController.setCrouch(crouch);
    walkAnimationController.setFast(fast);

    double leg1CurrentStep = 0;
    double leg2CurrentStep = 0;

    if (run) {
      leg1CurrentStep = (currentStep + (MAX_STEP / 2.))
          % MAX_STEP;
      leg2CurrentStep = currentStep;
    }

    leg1.calculateStep(bodyPosition, walkAnimationController,
        left, leg1CurrentStep)
        .render(renderer, position);

    leg2.calculateStep(bodyPosition, walkAnimationController,
        left, leg2CurrentStep)
        .render(renderer, position);

    // Step control
    if (run) {
      currentStep = (currentStep + 1) % MAX_STEP;
    } else {
      currentStep = 0;
    }
  }

}

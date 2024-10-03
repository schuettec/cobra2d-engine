package com.github.schuettec.cobra2Dexamples.walkinganimation;

import com.github.schuettec.cobra2Dexamples.moveableShapes.MoveableCircleEntity;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg.LegBuilder;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class WalkingEntity extends MoveableCircleEntity
    implements Updatable {

  private static final int MAX_STEP = 50;

  private double currentStep = 0;

  private Leg leg1;

  private Leg leg2;

  private Point mousePoint;

  private boolean left;

  private boolean run;

  private boolean fast;

  public WalkingEntity(Point worldCoordinates, double radius,
      boolean playerControlled) {
    super(worldCoordinates, radius, playerControlled);
    LegBuilder builder = Leg.newLeg()
        .setLegLength(radius)
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

    if (controller.isShiftLeftKeyPressed()) {
      this.fast = true;
    } else {
      this.fast = false;
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
  public void update(WorldAccess worldAccess, float deltaTime) {
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

    double leg1CurrentStep = 0;
    double leg2CurrentStep = 0;
    if (run) {
      leg1CurrentStep = (currentStep + 25) % MAX_STEP;
      leg2CurrentStep = currentStep;
    }

    leg1.calculateStep(getPosition(), left, leg1CurrentStep)
        .render(renderer, position);

    leg1.calculateStep(getPosition(), left, leg2CurrentStep)
        .render(renderer, position);

    // Step control
    if (run) {
      currentStep = (currentStep + 1) % MAX_STEP;
    } else {
      currentStep = 0;
    }
  }

}

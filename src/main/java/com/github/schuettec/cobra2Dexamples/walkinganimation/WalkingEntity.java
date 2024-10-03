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

  private boolean crouch;

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

    if (controller.isCtrlLeftKeyPressed()) {
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
  public void update(WorldAccess worldAccess, float deltaTime) {
    if (fast) {
      leg1.setEllipsisWalkAnimMaxY(130d);
      leg2.setEllipsisWalkAnimMaxY(130d);
    } else {
      leg1.setEllipsisWalkAnimMaxY(80d);
      leg2.setEllipsisWalkAnimMaxY(80d);
    }
  }

  @Override
  public void render(RendererAccess renderer, Point position) {
    super.render(renderer, position);

    // --- Calculate max point
    Point mousePointNormalized = mousePoint.clone();
    Point bodyPosition = getPosition().clone();

    if (crouch) {
      int crouchTranslate = 80;
      bodyPosition.translate(0, -crouchTranslate);
      leg1.setRadius(leg1.getLegLength() - crouchTranslate - 20);
      leg1.setEllipsisWalkAnimMaxY(30);
      leg2.setRadius(leg2.getLegLength() - crouchTranslate - 20);
      leg2.setEllipsisWalkAnimMaxY(30);
    } else {
      leg1.setRadius(leg1.getLegLength());
      leg2.setRadius(leg2.getLegLength());
    }

    double distance = Math2D.getEntfernung(bodyPosition,
        mousePoint);
    double mouseAngle = Math2D.getAngle(bodyPosition,
        mousePoint);
    if (distance > getRadius()) {
      mousePointNormalized = Math2D.getCircle(bodyPosition,
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

    leg1.calculateStep(bodyPosition, left, leg1CurrentStep)
        .render(renderer, position);

    leg2.calculateStep(bodyPosition, left, leg2CurrentStep)
        .render(renderer, position);

    // Step control
    if (run) {
      currentStep = (currentStep + 1) % MAX_STEP;
    } else {
      currentStep = 0;
    }
  }

}

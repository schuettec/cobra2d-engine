package com.github.schuettec.cobra2Dexamples.walkinganimation;

import com.github.schuettec.cobra2Dexamples.moveableShapes.MoveableCircleEntity;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg.LegBuilder;
import com.github.schuettec.cobra2d.controller.Controller;
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

    Point center = getPosition().translate(position);
    renderer.fillCircle((float) center.x + 5,
        (float) center.y - 5, 10f, Color.RED);
    //
    // leg1.calculateStep(new Point(0, 0), currentStep)
    // .render(renderer, position.clone()
    // .translate(getPosition()));
    // leg2.calculateStep(new Point(0, 0),
    // currentStep + (MAX_STEP / 2))
    // .render(renderer, position.clone()
    // .translate(getPosition()));

    leg1.calculateStep(getPosition().clone(), currentStep)
        .render(renderer, position);
    leg2.calculateStep(getPosition().clone(),
        currentStep + (MAX_STEP / 2))
        .render(renderer, position.clone());

    currentStep = (currentStep + 1) % MAX_STEP;

    // System.out.println(
    // "Pos: " + getPosition() + "Mouse: " + mousePoint);
    //
    // leg1.berechneWinkel(getPosition(), mousePoint)
    // .render(renderer, position);

  }

}

package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import static com.github.schuettec.cobra2d.math.Math2D.saveRound;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg.LegBuilder;
import com.github.schuettec.cobra2Dexamples.walkinganimation.WalkAnimationController;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class PhysicsWalkingEntity extends BasicCircleEntity
    implements CircleRenderable, PhysicBody, Updatable,
    Controllable {

  private static final int MAX_STEP = 50;

  private double currentStep = 0;

  private Leg leg1;

  private Leg leg2;

  private boolean left;

  private boolean run;

  private boolean fast;

  private boolean crouch;

  private WalkAnimationController walkAnimationController;

  private Body body;

  private double forceToApply;

  /**
   * Unit conversion: 1 unit in Box2D is 1 Meter in real world.
   * We want to show a
   * 3cm radius ball on the screen that has 30 Pixels radius.
   */
  private float renderScaleConversionFactor = 1 / 100f;

  public PhysicsWalkingEntity(Point worldCoordinates,
      double radius, double forceToApply, double degrees) {
    super(worldCoordinates, radius);
    this.forceToApply = forceToApply;
    this.setDegrees(degrees);

    LegBuilder builder = Leg.newLeg()
        .setLegLength(radius)
        .setMaxStep(MAX_STEP);
    this.leg1 = builder.build();
    this.leg2 = builder.build();
    this.walkAnimationController = new WalkAnimationController(
        MAX_STEP, radius, 250d, 80d, 100d);
  }

  @Override
  public Fixture createFixture(Body body) {
    CircleShape shape = new CircleShape();
    double worldRadius = getCollisionShape(true, false, false)
        .getRadius();
    float radius = (float) worldRadius
        * renderScaleConversionFactor;
    shape.setRadius(radius);

    // Create a fixture definition to apply our shape to
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 1f;
    fixtureDef.friction = 1f;
    fixtureDef.restitution = 1f;
    // Create our fixture and attach it to the body
    Fixture fixture = body.createFixture(fixtureDef);
    this.body = body;
    return fixture;
  }

  @Override
  public void processControllerState(WorldAccess worldAccess,
      Controller controller) {

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
  public void update(WorldAccess worldAccess, float deltaTime) {

    // if run
    if (run) {
      Vector2 vForce = PhysicBody.getDegreesAndForceAsVector(
          getDegrees(), forceToApply);
      body.applyForce(vForce, new Vector2(), true);
    }

    Vector2 position = body.getPosition();
    float radians = body.getAngle();
    double degrees = Math.toDegrees(radians);
    setDegrees(degrees);
    setPosition(
        saveRound(position.x / renderScaleConversionFactor),
        saveRound(position.y / renderScaleConversionFactor));
  }

  @Override
  public void render(RendererAccess renderer, Point position) {

    CircleRenderable.renderCircle(
        getCollisionShapeInWorldCoordinates(), renderer,
        position, Color.MAGENTA);

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

  @Override
  public int getLayer() {
    return 0;
  }

  @Override
  public Color getDrawColor() {
    return Color.GREEN;
  }

  @Override
  public Body getBody() {
    return body;
  }

  @Override
  public float getDensity() {
    return 0;
  }

  @Override
  public BodyType getBodyType() {
    return BodyType.DynamicBody;
  }

  @Override
  public void setBody(Body body) {
    this.body = body;
  }
}

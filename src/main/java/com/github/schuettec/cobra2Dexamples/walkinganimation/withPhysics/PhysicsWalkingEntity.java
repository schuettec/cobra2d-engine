package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import static com.github.schuettec.cobra2d.math.Math2D.saveRound;
import static java.util.Objects.nonNull;

import java.util.Optional;
import java.util.function.Predicate;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg.LegBuilder;
import com.github.schuettec.cobra2Dexamples.walkinganimation.WalkAnimationController;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.RectangleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;
import com.github.schuettec.cobra2d.math.Rectangle;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Collision;
import com.github.schuettec.cobra2d.world.CollisionDetail;
import com.github.schuettec.cobra2d.world.CollisionMap;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class PhysicsWalkingEntity extends BasicRectangleEntity
    implements CircleRenderable, RectangleRenderable, PhysicBody,
    Updatable, Controllable {

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

  private double radius;

  private Point nextStepPointLeft;
  private Point nextStepPointRight;

  private PhysicsWalkFloorEntity leftFloor;

  private PhysicsWalkFloorEntity rightFloor;

  private PhysicsWalkFloorEntity bottomFloor;

  private StepUpAnimationController stepUpAnimationController;

  private boolean stepUpEvent;

  public PhysicsWalkingEntity(Point worldCoordinates,
      Dimension dimension, double radius, double forceToApply,
      double degrees) {
    super(worldCoordinates, dimension);
    this.radius = radius;
    this.forceToApply = forceToApply;
    this.setDegrees(degrees);

    LegBuilder builder = Leg.newLeg()
        .setLegLength(radius)
        .setMaxStep(MAX_STEP);
    this.leg1 = builder.build();
    this.leg2 = builder.build();
    this.walkAnimationController = new WalkAnimationController(
        MAX_STEP, radius, 97, 40d, 80d, 30d);
    this.stepUpAnimationController = new StepUpAnimationController(
        MAX_STEP, radius, 97, 40d, 80d, 30d);
  }

  @Override
  public void configureBodyDef(BodyDef bodyDef) {
    bodyDef.fixedRotation = true;
    bodyDef.linearDamping = 1f;
    bodyDef.angularDamping = 1f;
  }

  @Override
  public Fixture createFixture(Body body) {
    PolygonShape polygonShape = new PolygonShape();
    Polygon collisionShape = getCollisionShape(true, false,
        false);
    Rectangle huellRect = Math2D
        .getHuellRect(collisionShape.getPoints());
    Dimension dimension = huellRect.getDimension();
    float width = (float) dimension.getWidth()
        * renderScaleConversionFactor;
    float height = (float) dimension.getHeight()
        * renderScaleConversionFactor;
    polygonShape.setAsBox(width / 2.0f, height / 2.0f);

    // Create a fixture definition to apply our shape to
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = polygonShape;
    fixtureDef.density = 1f;
    fixtureDef.friction = 1f;
    fixtureDef.restitution = 0f;
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

    CollisionMap collisionMap = worldAccess.getCollisions()
        .detectCollision(this,
            HasCollisionShape::getCollisionShapeInWorldCoordinates,
            worldAccess.getObstaclesExcept(this),
            HasCollisionShape::getCollisionShapeInWorldCoordinates,
            false, true, false);

    leftFloor = null;
    rightFloor = null;
    bottomFloor = null;

    for (Collision c : collisionMap.getCollisions()) {
      if (c.getOpponent() instanceof PhysicsWalkFloorEntity) {
        PhysicsWalkFloorEntity opponent = (PhysicsWalkFloorEntity) c
            .getOpponent();
        Point oPosition = opponent.getPosition();

        Optional<CollisionDetail> lineParallelX = hasCollisionLineMatching(
            c, Line::isParallelX);
        Optional<CollisionDetail> lineParallelY = hasCollisionLineMatching(
            c, Line::isParallelY);
        if (lineParallelX.isPresent()
            && !hasCollisionLineMatching(c, Line::isParallelY)
                .isPresent()
            && isBottom(oPosition)) {
          bottomFloor = opponent;
        } else if (lineParallelY.isPresent()
            && isLeft(oPosition)) {
          CollisionDetail detail = lineParallelY.get();
          nextStepPointLeft = detail.getIntersection();
          leftFloor = opponent;
          // Reset animation step
          if (!stepUpEvent) {
            currentStep = 0;
            System.out.println("RESET");
          }
          stepUpEvent = true;
        } else if (lineParallelY.isPresent()
            && isRight(oPosition)) {
          CollisionDetail detail = lineParallelY.get();
          nextStepPointRight = detail.getIntersection();
          rightFloor = opponent;
          // Reset animation step
          if (!stepUpEvent) {
            currentStep = 0;
            System.out.println("RESET");
          }
          stepUpEvent = true;
        }
      }
    }

    // if run
    Vector2 currentVelocity = this.getBody()
        .getLinearVelocity();
    if (run) {
      body.setLinearVelocity(
          (left ? -1f : 1f) * (float) forceToApply,
          currentVelocity.y);
    } else {
      body.setLinearVelocity(0, currentVelocity.y);
    }

    Vector2 position = body.getPosition();
    float radians = body.getAngle();
    double degrees = Math.toDegrees(radians);
    setDegrees(degrees);
    setPosition(
        saveRound(position.x / renderScaleConversionFactor),
        saveRound(position.y / renderScaleConversionFactor));
  }

  private Optional<CollisionDetail> hasCollisionLineMatching(
      Collision c, Predicate<Line> predicate) {
    return c.getCollisionDetails()
        .stream()
        .filter(detail -> {
          Line entityLine = detail.getEntityLine();
          if (nonNull(entityLine)) {
            if (predicate.test(entityLine)) {
              return true;
            } else {
              return false;
            }
          } else {
            return false;
          }
        })
        .findFirst();
  }

  private boolean isLeft(Point oPosition) {
    // opponent position must be lover than entity.
    double myX = getPosition().x;
    if (oPosition.x <= myX) {
      return true;
    } else {
      return false;
    }
  }

  private boolean isRight(Point oPosition) {
    double myX = getPosition().x;
    if (oPosition.x >= myX) {
      return true;
    } else {
      return false;
    }
  }

  private boolean isBottom(Point oPosition) {
    double myY = getPosition().y;
    double myHeight = getDimension(false, false).getHeight();
    if (oPosition.y <= myY + myHeight) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void render(RendererAccess renderer, Point position) {
    PolygonRenderable.renderPolygon(
        getCollisionShapeInWorldCoordinates(), renderer,
        position, Color.MAGENTA);

    Circle circle = new Circle(new Point(), radius)
        .translate(getPosition());
    CircleRenderable.renderCircle(circle, renderer, position,
        Color.MAGENTA);

    if (nonNull(leftFloor)) {
      Point point = nextStepPointLeft.clone()
          .translate(position);
      renderer.fillCircle(point.getFloatX() - 2,
          point.getFloatY() - 2, 4, Color.CYAN);
    }

    if (nonNull(rightFloor)) {
      Point point = nextStepPointRight.clone()
          .translate(position);
      renderer.fillCircle(point.getFloatX() - 2,
          point.getFloatY() - 2, 4, Color.YELLOW);
    }

    if (nonNull(bottomFloor)) {
      Point point = bottomFloor.getPosition()
          .clone()
          .translate(position);
      renderer.fillCircle(point.getFloatX() - 2,
          point.getFloatY() - 2, 4, Color.RED);
    }

    // --- Calculate max point
    Point bodyPosition = getPosition().clone();

    if (stepUpEvent && run) {
      Point unterschenkelEnde = leg1
          .calculateStep(bodyPosition, walkAnimationController,
              left, 0)
          .unterschenkelEnde();
      stepUpAnimationController
          .setSourcePoint(unterschenkelEnde);
      if (left && nonNull(leftFloor)) {
        stepUpAnimationController
            .setTargetPoint(nextStepPointLeft);
        System.out.println("Animate to left");
      } else if (!left && nonNull(rightFloor)) {
        stepUpAnimationController
            .setTargetPoint(nextStepPointRight);
        System.out.println("Animate to right");
      }

      leg1.calculateStep(bodyPosition, stepUpAnimationController,
          left, currentStep)
          .render(renderer, position);
      leg2.calculateStep(bodyPosition, walkAnimationController,
          left, 0)
          .render(renderer, position);
      currentStep = (currentStep + 1) % MAX_STEP;
      System.out.println("STep up and run " + currentStep);
      if (currentStep == MAX_STEP) {
        stepUpEvent = false;
        System.out.println("EVENT ENDE");
      }
    } else {

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

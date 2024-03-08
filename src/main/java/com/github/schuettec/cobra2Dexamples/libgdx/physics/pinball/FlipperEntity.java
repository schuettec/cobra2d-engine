package com.github.schuettec.cobra2Dexamples.libgdx.physics.pinball;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.utils.TimedBoolean;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class FlipperEntity extends BasicRectangleEntity
		implements PolygonRenderable, Updatable, PhysicBody, Controllable {

	private static final float DOWN_TORQUE = 100f;
	private static final float UP_TORQUE = 200f;
	private static final double MIN_FLIPPER_DEGREES = 330d;
	private static final double MAX_FLIPPER_DEGREES = 390d;

	private Body body;

	private TimedBoolean leftKeyState;

	/**
	 * The pivot point of the flipper shaft. This is the point the flipper shaft
	 * will be rotated by.
	 */
	private Point pivotPointTranslation = new Point(0.5, 0);
	private Fixture fixture;

	private float density = 1f;
	private BodyType bodyType = BodyType.DynamicBody;

	public FlipperEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
		moveCollisionShapeByPivotPoint(pivotPointTranslation);
		this.leftKeyState = new TimedBoolean();
	}

	@Override
	public void configureFixture(Fixture fixture) {
		PhysicBody.super.configureFixture(fixture);
		fixture.setFriction(0.1f);
		fixture.setRestitution(0f);
	}

	/**
	 * Translates the entitie's polygon collision shape by the pivot point to have
	 * flipper shaft rotation point at (0|0).
	 *
	 * @param pivotPoint The pivot Point translation
	 */
	private void moveCollisionShapeByPivotPoint(Point pivotPoint) {
		Dimension collisionShapeDimension = getCollisionShapeDimension();
		polygon.translate(
				pivotPoint.clone().scale(collisionShapeDimension.getWidth(), collisionShapeDimension.getHeight()));
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		PhysicBody.super.update(worldAccess, deltaTime);
		float currentFlipperAngle = Math2D.toDegrees(body.getAngle());
		System.out.println(currentFlipperAngle);
		if (leftKeyState.isTrue()) {
			if (currentFlipperAngle >= MAX_FLIPPER_DEGREES) {
				System.out.println("ICh hör uff");
				body.setFixedRotation(true);
			} else {
				System.out.println("Ich geb Gas");
				body.applyTorque(UP_TORQUE, true);
				body.setFixedRotation(false);
			}
		} else {
			body.setFixedRotation(false);
			if (currentFlipperAngle > MIN_FLIPPER_DEGREES) {
				System.out.println("Downforce");
				body.applyTorque(-DOWN_TORQUE, true);
			} else {
				System.out.println("Sleep");
				body.setFixedRotation(true);
			}
		}

	}

	@Override
	public void processControllerState(WorldAccess worldAccess, Controller controller) {
		if (controller.isLeftKeyPressed()) {
			this.leftKeyState.start();
		} else {
			this.leftKeyState.reset();
		}
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		renderPolygon(getCollisionShapeInWorldCoordinates(), renderer, position);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public Color getDrawColor() {
		return Color.GRAY;
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public float getDensity() {
		return density;
	}

	@Override
	public BodyType getBodyType() {
		return bodyType;
	}

	@Override
	public void setBody(Body body) {
		this.body = body;
	}

}

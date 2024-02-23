package com.github.schuettec.cobra2Dexamples.libgdx.physics.pinball;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.github.schuettec.cobra2Dexamples.bouncingBalls.BallEntity;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.CollisionMap;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class BallHitSensorEntity extends BasicRectangleEntity implements PolygonRenderable, Updatable, PhysicBody {

	private Body body;

	private Fireable fireable;
	private boolean wasFired;

	public BallHitSensorEntity(Point worldCoordinates, Dimension dimension, Fireable fireable) {
		super(worldCoordinates, dimension);
		this.fireable = fireable;
	}

	@Override
	public void configureFixture(Fixture fixture) {
		fixture.setSensor(true);
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		CollisionMap collisionMap = worldAccess.getCollisions().detectCollision(this,
				HasCollisionShape::getCollisionShapeInWorldCoordinates, worldAccess.getObstaclesExcept(this),
				HasCollisionShape::getCollisionShapeInWorldCoordinates, false, false, false);
		boolean isCollidingWithBall = collisionMap.getCollisions().stream()
				.filter(c -> c.getEntity() instanceof BallEntity).findFirst().isPresent();
		if (isCollidingWithBall) {
			if (!wasFired) {
				System.out.println("FIred");
				fireable.fire();
			}
			this.wasFired = true;
		} else {
			this.wasFired = false;
		}
	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {
		renderPolygon(getCollisionShapeInWorldCoordinates(), renderer, screenTranslation);
	}

	@Override
	public BodyType getBodyType() {
		return BodyType.StaticBody;
	}

	@Override
	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public Color getDrawColor() {
		return Color.WHITE;
	}

}

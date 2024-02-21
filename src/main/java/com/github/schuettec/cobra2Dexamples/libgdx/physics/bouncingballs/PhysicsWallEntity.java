package com.github.schuettec.cobra2Dexamples.libgdx.physics.bouncingballs;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class PhysicsWallEntity extends BasicRectangleEntity implements PolygonRenderable, PhysicBody, Updatable {

	/**
	 * Unit conversion: 1 unit in Box2D is 1 Meter in real world. We want to show a
	 * 3cm radius ball on the screen that has 30 Pixels radius.
	 */
	private static final float toRenderScale = 100f;
	private static float toPhysxFactor = 1 / toRenderScale;

	private Body body;
	private Fixture fixture;

	public PhysicsWallEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		Point newPosition = new Point(body.getPosition().x, body.getPosition().y);
		newPosition = newPosition.scale(toRenderScale);
		this.setPosition(newPosition);
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
		return 5f;
	}

	@Override
	public BodyType getBodyType() {
		return BodyType.StaticBody;
	}

	@Override
	public void setBody(Body body) {
		this.body = body;
	}

}

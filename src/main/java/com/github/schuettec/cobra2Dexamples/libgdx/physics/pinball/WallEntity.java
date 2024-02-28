package com.github.schuettec.cobra2Dexamples.libgdx.physics.pinball;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class WallEntity extends BasicRectangleEntity implements PolygonRenderable, PhysicBody, Updatable {

	/**
	 * Unit conversion: 1 unit in Box2D is 1 Meter in real world. We want to show a
	 * 3cm radius ball on the screen that has 30 Pixels radius.
	 */
	private static final float toRenderScale = 100f;
	private static float toPhysxFactor = 1 / toRenderScale;

	private Body body;

	public WallEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
	}

	@Override
	public BodyDef createBodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(getPosition().getRoundX() * toPhysxFactor, getPosition().getRoundY() * toPhysxFactor);
		bodyDef.angle = getRadians();
		return bodyDef;
	}

	@Override
	public Fixture createFixture(Body body) {
		PolygonShape polygonShape = new PolygonShape();
		double width = getCollisionShapeDimension().getWidth();
		double height = getCollisionShapeDimension().getHeight();
		float phWidth = (float) width * toPhysxFactor;
		float phHeight = (float) height * toPhysxFactor;
		polygonShape.setAsBox(phWidth / 2.0f, phHeight / 2.0f);
		Fixture fixture = body.createFixture(polygonShape, 5.0f);
		this.body = body;
		return fixture;
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
	public void setBody(Body body) {
		this.body = body;
	}

}

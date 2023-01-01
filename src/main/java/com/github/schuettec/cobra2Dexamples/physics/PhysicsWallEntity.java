package com.github.schuettec.cobra2Dexamples.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.physics.StaticBody;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class PhysicsWallEntity extends BasicRectangleEntity implements PolygonRenderable, StaticBody {

	private Body body;

	public PhysicsWallEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
	}

	@Override
	public BodyDef createBodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(getPosition().getRoundX(), getPosition().getRoundY());
		bodyDef.angle = getRadians();
		return bodyDef;
	}

	@Override
	public void createFixture(Body body) {
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox((float) getDimension().getWidth() / 2.0f, (float) getDimension().getHeight() / 2.0f);
		body.createFixture(polygonShape, 5.0f);
		this.body = body;
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

}

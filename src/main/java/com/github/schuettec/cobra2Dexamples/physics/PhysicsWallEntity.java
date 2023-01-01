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

	/**
	 * Unit conversion: 1 unit in Box2D is 1 Meter in real world. We want to show a 3cm radius ball on the screen that has
	 * 30 Pixels radius.
	 */
	private float renderScaleConversionFactor = 1 / 100f;

	private Body body;

	public PhysicsWallEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
	}

	@Override
	public BodyDef createBodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(getPosition().getRoundX() * renderScaleConversionFactor,
		    getPosition().getRoundY() * renderScaleConversionFactor);
		bodyDef.angle = getRadians();
		return bodyDef;
	}

	@Override
	public void createFixture(Body body) {
		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox((float) getDimension().getWidth() * renderScaleConversionFactor / 2.0f,
		    (float) getDimension().getHeight() * renderScaleConversionFactor / 2.0f);
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

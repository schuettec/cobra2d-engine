package com.github.schuettec.cobra2Dexamples.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.StaticBody;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

public class PhysicsWallEntity extends BasicRectangleEntity implements PolygonRenderable, StaticBody, Updatable {

	/**
	 * Unit conversion: 1 unit in Box2D is 1 Meter in real world. We want to show a 3cm radius ball on the screen that has
	 * 30 Pixels radius.
	 */
	private static final float toRenderScale = 100f;
	private static float toPhysxFactor = 1 / toRenderScale;

	private Body body;

	public PhysicsWallEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
	}

	@Override
	public BodyDef createBodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(getPosition().getRoundX() * toPhysxFactor, getPosition().getRoundY() * toPhysxFactor);
		System.out.println("Wall entity (world): x=" + getPosition().getRoundX() + ", y=" + getPosition().getRoundY());
		System.out.println("Wall entity (physx): x=" + bodyDef.position.x + ", y=" + bodyDef.position.y);
		bodyDef.angle = getRadians();
		return bodyDef;
	}

	@Override
	public void createFixture(Body body) {
		PolygonShape polygonShape = new PolygonShape();
		double width = getDimension().getWidth();
		double height = getDimension().getHeight();
		float phWidth = (float) width * toPhysxFactor;
		float phHeight = (float) height * toPhysxFactor;
		System.out.println("Wall entity (world): width=" + width + " height=" + height);
		System.out.println("Wall entity (physx): width=" + phWidth + " height=" + phHeight);
		polygonShape.setAsBox(phWidth / 2.0f, phHeight / 2.0f);

		for (int i = 0; i < polygonShape.getVertexCount(); i++) {
			Vector2 vertex = new Vector2();
			polygonShape.getVertex(i, vertex);
			System.out.println(i + " " + vertex);
		}

		body.createFixture(polygonShape, 5.0f);
		this.body = body;
	}

	@Override
	public void update(Cobra2DWorld map, float deltaTime, Controller controller) {
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

}

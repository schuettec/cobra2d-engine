package com.github.schuettec.cobra2Dexamples.libgdx.physics.pinball;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
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

public class HammerEntity extends BasicRectangleEntity implements PolygonRenderable, PhysicBody, Updatable, Fireable {

	public HammerEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
	}

	private Body body;

	private boolean fire;

	@Override
	public void configureBodyDef(BodyDef bodyDef) {
		PhysicBody.super.configureBodyDef(bodyDef);
		bodyDef.gravityScale = 0.1f;
	}

	@Override
	public void configureFixture(Fixture fixture) {
		PhysicBody.super.configureFixture(fixture);
		fixture.setDensity(1f);
		fixture.setFriction(1f);
		fixture.setRestitution(0f);
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		PhysicBody.super.update(worldAccess, deltaTime);

		// Fire!!
		if (fire) {
			System.out.println("Fire");
			// Get the force vector in current object orientation.
			// Scale the world normal by the desired force (scl-Method)
			this.body.applyForceToCenter(new Vector2(1500, 0), true);
			// Reset fire flag
			this.fire = false;
		} else {
			this.body.applyForceToCenter(new Vector2(-1500, 0), true);
		}
	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {
		renderPolygon(getCollisionShapeInWorldCoordinates(), renderer, screenTranslation);
	}

	@Override
	public void fire() {
		this.fire = true;
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
		return Color.ORANGE;
	}

}

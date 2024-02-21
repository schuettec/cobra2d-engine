package com.github.schuettec.cobra2Dexamples.libgdx.physics.pinball;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class AnchorEntity extends BasicRectangleEntity implements PolygonRenderable, Updatable, PhysicBody {

	private Body body;

	public AnchorEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
	}

	@Override
	public BodyType getBodyType() {
		return BodyType.StaticBody;
	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {
		renderPolygon(getCollisionShapeInWorldCoordinates(), renderer, screenTranslation);
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

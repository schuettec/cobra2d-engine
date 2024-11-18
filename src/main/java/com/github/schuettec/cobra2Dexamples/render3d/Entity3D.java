package com.github.schuettec.cobra2Dexamples.render3d;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class Entity3D extends BasicCircleEntity implements CircleRenderable, Updatable, Obstacle {

	private AssetManager assetManager;

	public Entity3D(Point worldCoordinates, double radius) {
		super(worldCoordinates, radius);

		this.assetManager = new AssetManager();
		assetManager.load("my_model.g3db", Model.class);
		assetManager.finishLoading();
		model = assetManager.get("my_model.g3db", Model.class);
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {

	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {
		Circle collisionShape = getCollisionShapeInWorldCoordinates();
		renderCircle(collisionShape, renderer, screenTranslation);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public Color getDrawColor() {
		return Color.WHITE;
	}

}
package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import com.github.schuettec.cobra2d.entity.BasicPolygonEntity;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.common.Color;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public class WallEntity extends BasicPolygonEntity implements PolygonRenderable, Obstacle {

	public WallEntity(Point worldCoordinates, Point[] points) {
		super(worldCoordinates, points);
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		renderPolygon(getCollisionShape(), renderer, position);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public Color getDrawColor() {
		return Color.GRAY;
	}

}

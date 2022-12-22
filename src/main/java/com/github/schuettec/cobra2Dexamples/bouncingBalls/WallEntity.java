package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.schuettec.cobra2d.entity.BasicPolygonEntity;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.math.Point;

public class WallEntity extends BasicPolygonEntity implements PolygonRenderable, Obstacle {

	public WallEntity(Point worldCoordinates, Point[] points) {
		super(worldCoordinates, points);
	}

	@Override
	public void render(Graphics2D graphics, Point position) {
		renderPolygon(getCollisionShape(), graphics, position);
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

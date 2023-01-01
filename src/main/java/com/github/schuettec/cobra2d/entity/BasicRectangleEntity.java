package com.github.schuettec.cobra2d.entity;

import java.util.List;

import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;
import com.github.schuettec.cobra2d.math.Rectangle;

public class BasicRectangleEntity extends BasicPolygonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BasicRectangleEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, Math2D.getRectangle(dimension.getWidth(), dimension.getHeight()));
	}

	protected void createRectangleShape(Dimension dimension) {
		createCollisionShape(Math2D.getRectangle(dimension));
	}

	public Dimension getDimension() {
		Polygon collisionShape = getCollisionShape(true, true, false);
		List<Point> points = collisionShape.getPoints();
		Rectangle huellRect = Math2D.getHuellRect(points);
		return huellRect.getDimension();
	}

}
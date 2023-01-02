package com.github.schuettec.cobra2d.entity;

import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;

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

}
package com.github.schuettec.cobra2d.entity;

import java.awt.Dimension;

import com.github.schuettec.cobra2d.math.Point;

public class BasicRectangleEntity extends BasicPolygonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dimension dimension;

	public BasicRectangleEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, new Point(0, 0), new Point(dimension.getWidth(), 0),
		    new Point(dimension.getWidth(), dimension.getHeight()), new Point(0, dimension.getHeight()));
		this.dimension = dimension;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

}
package com.github.schuettec.cobra2d.entity;

import java.awt.Dimension;
import java.awt.Rectangle;

import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;

public class BasicRectangleEntity extends BasicPolygonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BasicRectangleEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, Math2D.getRectangle(dimension));
	}

	public Dimension getDimension() {
		Rectangle huellRect = Math2D.getHuellRect(getCollisionShape(true, true, false).getPoints());
		return new Dimension(huellRect.width, huellRect.height);
	}

}
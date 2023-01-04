package com.github.schuettec.cobra2d.math;

import java.io.Serializable;

public class Rectangle implements Serializable {

	private Point point;
	private Dimension dimension;

	public Rectangle() {
		this(new Point(), new Dimension());
	}

	public Rectangle(Point point, Dimension dimension) {
		this.point = point;
		this.dimension = dimension;
	}

	public Point getPoint() {
		return point.clone();
	}

	public Dimension getDimension() {
		return dimension.clone();
	}

	@Override
	public String toString() {
		return "Rectangle [point=" + point + ", dimension=" + dimension + "]";
	}

	public double getX() {
		return point.getX();
	}

	public double getY() {
		return point.getY();
	}

	public double getWidth() {
		return dimension.getWidth();
	}

	public double getHeight() {
		return dimension.getHeight();
	}

}

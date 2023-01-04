package com.github.schuettec.cobra2d.math;

import static com.github.schuettec.cobra2d.math.Math2D.saveRound;

import java.io.Serializable;

public class Dimension implements Cloneable, Serializable {

	private double width;
	private double height;

	public Dimension() {
		this.width = 0;
		this.height = 0;
	}

	public Dimension(double width, double height) {
		super();
		this.width = width;
		this.height = height;
	}

	public Dimension half() {
		return new Dimension(width / 2.0d, height / 2.0d);
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	@Override
	public String toString() {
		return "Dimension [width=" + width + ", height=" + height + "]";
	}

	public int getRoundWidth() {
		return saveRound(getWidth());
	}

	public int getRoundHeight() {
		return saveRound(getHeight());
	}

	@Override
	public Dimension clone() {
		return new Dimension(width, height);
	}

}

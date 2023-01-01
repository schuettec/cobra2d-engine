package com.github.schuettec.cobra2d.math;

import static com.github.schuettec.cobra2d.math.Math2D.saveRound;

public class Dimension implements Cloneable {

	private double width;
	private double height;

	public Dimension(double width, double height) {
		super();
		this.width = width;
		this.height = height;
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

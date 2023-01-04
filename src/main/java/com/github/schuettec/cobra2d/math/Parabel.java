package com.github.schuettec.cobra2d.math;

import java.io.Serializable;

/**
 * @author Chris Defines a parabel that can be initialized with three arguments:
 *         A point where the peak of the parabel
 */
public class Parabel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Point expectedValue;

	protected Point peak;
	protected double a;

	public Parabel(final Point expectedValue, final Point peak) {
		super();

		if (expectedValue.x - peak.x == 0)
			throw new IllegalStateException("ExpectedValue.x - Peak.x cannot be 0.");

		this.peak = peak;
		this.setExpectedValue(expectedValue);
	}

	public double getValue(final double x) {
		return this.a * Math.pow(x - this.peak.x, 2) + this.peak.y;
	}

	public Point getPeak() {
		return this.peak;
	}

	public void setPeak(final Point peak) {
		this.peak = peak;
		this.calculateA();
	}

	private void calculateA() {
		this.a = (this.expectedValue.y - this.peak.y) / Math.pow(this.expectedValue.x - this.peak.x, 2);
	}

	public Point getExpectedValue() {
		return this.expectedValue;
	}

	public void setExpectedValue(final Point expectedValue) {
		this.expectedValue = expectedValue;
		this.calculateA();
	}

	@Override
	public String toString() {
		return "Parabel: f(x) = " + this.a + "* (x - " + this.peak.x + ")² + " + this.peak.y + ")";
	}
}

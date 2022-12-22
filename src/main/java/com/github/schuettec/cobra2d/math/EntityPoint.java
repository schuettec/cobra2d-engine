
package com.github.schuettec.cobra2d.math;

/**
 * An {@link EntityPoint} represents a point in world coordinates.
 *
 * @author schuettec
 *
 */
public class EntityPoint implements Shape {

	private static final Point ORIGIN = new Point(0, 0);

	private double radius = 0;

	private double degrees = 0.0;

	/**
	 * Holds the position of this entity point as {@link Point}. This field is
	 * final. This way the instance will not change and we can expose the point to
	 * the outside. Avoids permanent synchronization of variables.
	 */
	private final Point coordinates;

	public EntityPoint(int x, int y) {
		coordinates = new Point(x, y);
		setByPosition(coordinates);
	}

	public EntityPoint(Point point) {
		coordinates = point.clone();
		setByPosition(coordinates);
	}

	public EntityPoint(final double degrees, final double radius) {
		coordinates = new Point(0, 0);
		setByCircle(degrees, radius);
	}

	public void setByPosition(final Point coordinates) {
		this.degrees = Math2D.getAngle(ORIGIN, coordinates);
		this.radius = Math2D.getEntfernung(ORIGIN, coordinates);
		this.coordinates.setLocation(coordinates);
	}

	public void setByCircle(double degrees, double radius) {
		this.degrees = degrees;
		this.radius = radius;

		Point newPoint = Math2D.getCircle(ORIGIN, radius, degrees);
		this.coordinates.setLocation(newPoint);
	}

	public Point getCoordinates() {
		return this.coordinates;
	}

	public double getDegrees() {
		return this.degrees;
	}

	public double getRadius() {
		return this.radius;

	}

	@Override
	public EntityPoint clone() {
		return new EntityPoint(degrees, radius);
	}

	public EntityPoint rotate(double degrees) {
		setByCircle(this.degrees + degrees, this.radius);
		return this;
	}

	public EntityPoint translate(Point translation) {
		Point newPoint = new Point(coordinates);
		newPoint.translate(translation.x, translation.y);
		setByPosition(newPoint);
		return this;
	}

	public EntityPoint scale(double scaleFactor) {
		setByCircle(this.degrees, this.radius * scaleFactor);
		return this;
	}

}

package com.github.schuettec.cobra2d.entity;

import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Point;

/**
 * Represents an abstract obstacle in the world that manages a circle as
 * collision hull.
 *
 * @author schuettec
 */
public class BasicCircleEntity extends BasicEntity implements HasCollisionShape {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Holds the collision shape of this entity.
	 */
	protected Circle circle;

	public BasicCircleEntity(Point worldCoordinates, double radius) {
		super(worldCoordinates);
		// Keep the collision shape in a local coordinate system. Do not use the world coordinates for the origin shape.
		this.circle = new Circle(new Point(0, 0), radius);
	}

	public Circle getCollisionShapeInWorldCoordinates() {
		return getCollisionShape(true, true, true);
	}

	@Override
	public Circle getCollisionShape(boolean applyScaling, boolean applyRotation, boolean applyWorldCoordinates) {
		Circle clone = circle.clone();
		if (applyScaling) {
			clone = clone.scale(getScale());
		}

		if (applyRotation) {
			clone = clone.rotate(getDegrees());
		}
		if (applyWorldCoordinates) {
			clone = clone.translate(worldCoordinates);
		}
		return clone;
	}

	public double getRadius() {
		return circle.getRadius();
	}

}

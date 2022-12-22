package com.github.schuettec.cobra2d.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Point;

/**
 * Represents an abstract obstacle in the world that manages a circle as
 * collision hull.
 *
 * @author schuettec
 */
public class BasicCircleEntity extends BasicEntity implements CircleRenderable, HasCollisionShape {

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
		this.circle = new Circle(new Point(0, 0), radius);
	}

	public Circle getCollisionShape() {
		return circle.clone()
		    .scale(scaling)
		    .rotate(degrees)
		    .translate(worldCoordinates);
	}

	public double getRadius() {
		return circle.getRadius();
	}

	@Override
	public void render(Graphics2D graphics, Point position) {
		Circle collisionShape = getCollisionShape();
		renderCircle(collisionShape, graphics, position);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public Color getDrawColor() {
		return Color.white;
	}

}

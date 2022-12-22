package com.github.schuettec.cobra2d.entity;

import java.util.ArrayList;
import java.util.List;

import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.math.EntityPoint;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;

/**
 * Represents an abstract obstacle in the world. This is the most possible
 * abstract class for obstacle entities implementing common functions.
 *
 * <p>
 * This class assumes that the collision hull for an entity is always
 * represented by a polygon. Therefore a list of entity points and collision
 * lines is managed by this object. Shapes that do not need a polygon for
 * collision detection (like circles - they can do it more efficiently) can
 * either override affected methods or can implement the skill interfaces
 * {@link Obstacle} and {@link Entity} themselves.
 * <p>
 *
 *
 *
 * @author schuettec
 */
public class BasicPolygonEntity extends BasicEntity implements HasCollisionShape {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected Polygon polygon;

	public BasicPolygonEntity(Point worldCoordinates, EntityPoint... entityPoints) {
		super(worldCoordinates);
		this.polygon = new Polygon(entityPoints);
	}

	public BasicPolygonEntity(Point worldCoordinates, Point... points) {
		super(worldCoordinates);

		List<EntityPoint> entityPoints = new ArrayList<EntityPoint>(points.length);
		for (Point p : points) {
			entityPoints.add(new EntityPoint(p));
		}

		this.polygon = new Polygon(entityPoints);
	}

	@Override
	public Polygon getCollisionShape() {
		return polygon.clone()
		    .scale(scaling)
		    .rotate(degrees)
		    .translate(worldCoordinates);
	}

}

package com.github.schuettec.cobra2d.entity;

import java.util.ArrayList;
import java.util.List;

import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.math.EntityPoint;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;

public class BasicPolygonEntity extends BasicEntity implements HasCollisionShape {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected Polygon polygon;

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

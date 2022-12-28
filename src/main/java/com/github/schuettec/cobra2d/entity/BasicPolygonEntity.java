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

	public BasicPolygonEntity(Point worldCoordinates, Point... localPoints) {
		super(worldCoordinates);
		createCollisionShape(localPoints);
	}

	public BasicPolygonEntity(Point worldCoordinates, List<EntityPoint> entityPoints) {
		super(worldCoordinates);
		this.polygon = new Polygon(entityPoints);
	}

	protected void createCollisionShape(List<EntityPoint> entityPoints) {
		this.polygon = new Polygon(entityPoints);
	}

	protected void createCollisionShape(Point... localPoints) {
		List<EntityPoint> entityPoints = new ArrayList<EntityPoint>(localPoints.length);
		for (Point p : localPoints) {
			entityPoints.add(new EntityPoint(p));
		}
		this.polygon = new Polygon(entityPoints);
	}

	@Override
	public Polygon getCollisionShapeInWorldCoordinates() {
		return getCollisionShape(true, true, true);
	}

	@Override
	public Polygon getCollisionShape(boolean applyScaling, boolean applyRotation, boolean applyWorldCoordinates) {
		Polygon clone = polygon.clone();
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

}

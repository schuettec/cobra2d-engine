package com.github.schuettec.cobra2d.entity.skills;

import java.util.List;

import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Rectangle;
import com.github.schuettec.cobra2d.math.Shape;

public interface HasCollisionShape extends Entity {
	/**
	 * @return Returns the {@link Shape} representing the collision shape of
	 *         this entity <b>in world coordinates</b>. Therefore any scaling, translation or rotating operations must be
	 *         applied. Implementations must make sure that the
	 *         {@link Shape} is translated to world coordinates and reflects all
	 *         properties like scaling etc.
	 */
	public Shape getCollisionShapeInWorldCoordinates();

	/**
	 * Returns the collision shape of this entity applied with the specified operations.
	 * If all arguments are <code>false</code> the local, original collision shape is returned.
	 * 
	 * @param applyScaling Applies any scaling set by the current entity.
	 * @param applyRotation Applies the rotation of the current entity.
	 * @param applyWorldCoordinates Applies world coordinates.
	 * @return Returns the shape.
	 */
	public Shape getCollisionShape(boolean applyScaling, boolean applyRotation, boolean applyWorldCoordinates);

	/**
	 * @return Returns the collision shape dimension for the initial collision shape without rotation and translation in
	 *         world coordinates. Only the scale factor is applied.
	 */
	default Dimension getCollisionShapeDimension() {
		return getDimension(true, false);
	}

	/**
	 * @return Returns the collision shape dimension for the initial collision shape without scaling, rotation and
	 *         translation in
	 *         world coordinates.
	 */
	default Dimension getDimension(boolean applyScaling, boolean applyRotation) {
		Shape collisionShape = getCollisionShape(applyScaling, applyRotation, false);
		List<Point> points = collisionShape.getPoints();
		Rectangle huellRect = Math2D.getHuellRect(points);
		return huellRect.getDimension();
	}
}

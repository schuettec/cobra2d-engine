package com.github.schuettec.cobra2d.entity.skills;

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
	 * @param applyScaling Applies any scaling set by the current entity.
	 * @param applyRotation Applies the rotation of the current entity.
	 * @param applyWorldCoordinates Applies world coordinates.
	 * @return Returns the shape.
	 */
	public Shape getCollisionShape(boolean applyScaling, boolean applyRotation, boolean applyWorldCoordinates);
}

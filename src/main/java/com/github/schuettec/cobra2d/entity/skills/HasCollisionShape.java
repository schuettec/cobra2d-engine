package com.github.schuettec.cobra2d.entity.skills;

import com.github.schuettec.cobra2d.math.Shape;

public interface HasCollisionShape extends Entity {
	/**
	 * @return Returns the {@link Shape} representing the collision shape of
	 *         this camera. Implementations must make sure that the
	 *         {@link Shape} is translated to world coordinates and reflects all
	 *         properties like scaling etc.
	 */
	public Shape getCollisionShape();
}

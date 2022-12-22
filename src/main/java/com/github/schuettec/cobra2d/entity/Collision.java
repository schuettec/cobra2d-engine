package com.github.schuettec.cobra2d.entity;

import java.util.List;

import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.math.Point;

/**
 * Instances of this class represent a collision of two {@link Obstacle}s. This
 * class is the data transfer object for collisions detected by the {@link Map}.
 *
 * @author Chris
 *
 */
public class Collision {

	/**
	 * This is the entity this object uses for its hash code.
	 */
	protected Entity entity;
	/**
	 * This is the collision opponent.
	 */
	protected Entity opponent;
	/**
	 * This is the list of collision points.
	 */
	protected List<Point> points;
	/**
	 * If <code>true</code> the collision represents all collision points of the to
	 * entities colliding. If <code>false</code> only the first collision point is
	 * resolved.
	 */
	private boolean allCollisionPointsResolved;

	protected Collision(Entity entity, Entity opponent, List<Point> points, boolean all) {
		super();
		this.allCollisionPointsResolved = all;
		this.entity = entity;
		this.opponent = opponent;
		this.points = points;
	}

	/**
	 * @return Returns the first collision as {@link Point} that was reported for
	 *         the colliding entities. If this object does not have any collision
	 *         points this call returns <code>null</code>.
	 */
	public Point getFirstCollision() {
		if (points.isEmpty()) {
			return null;
		} else {
			return points.get(0);
		}
	}

	public Entity getEntity() {
		return entity;
	}

	public Entity getOpponent() {
		return opponent;
	}

	public List<Point> getPoints() {
		return points;
	}

	/**
	 * @return If <code>true</code> the collision represents all collision points of
	 *         the to entities colliding. If <code>false</code> only the first
	 *         collision point is resolved.
	 */
	public boolean isAllCollisionPointsResolved() {
		return allCollisionPointsResolved;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Collision other = (Collision) obj;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		return true;
	}

}

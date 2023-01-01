package com.github.schuettec.cobra2d.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

/**
 * Instances of this class represent a collision of two {@link Obstacle}s. This
 * class is the data transfer object for collisions detected by the {@link Cobra2DWorld}.
 *
 * @author Chris
 *
 */
public class Collision {

	/**
	 * This is the entity this object uses for its hash code.
	 */
	protected HasCollisionShape entity;
	/**
	 * This is the collision opponent.
	 */
	protected HasCollisionShape opponent;
	/**
	 * This is the list of collision points.
	 */
	protected List<CollisionDetail> collisionDetails;
	/**
	 * If <code>true</code> the collision represents all collision points of the to
	 * entities colliding. If <code>false</code> only the first collision point is
	 * resolved.
	 */
	private boolean allCollisionPointsResolved;

	protected Collision(HasCollisionShape entity, HasCollisionShape opponent, List<CollisionDetail> collisionDetails,
	    boolean all) {
		super();
		this.allCollisionPointsResolved = all;
		this.entity = entity;
		this.opponent = opponent;
		this.collisionDetails = collisionDetails;
	}

	public List<CollisionDetail> getCollisionDetails() {
		return new LinkedList<>(collisionDetails);
	}

	/**
	 * @return Returns the first collision as {@link CollisionDetail} that was reported for
	 *         the colliding entities. If this object does not have any collision
	 *         points this call returns <code>null</code>.
	 */
	public CollisionDetail getFirstCollisionDetail() {
		if (collisionDetails.isEmpty()) {
			return null;
		} else {
			return collisionDetails.get(0);
		}
	}

	/**
	 * @return Returns the first collision as {@link Point} that was reported for
	 *         the colliding entities. If this object does not have any collision
	 *         points this call returns <code>null</code>.
	 */
	public Point getFirstCollision() {
		if (collisionDetails.isEmpty()) {
			return null;
		} else {
			return collisionDetails.get(0)
			    .getIntersection();
		}
	}

	public HasCollisionShape getEntity() {
		return entity;
	}

	public HasCollisionShape getOpponent() {
		return opponent;
	}

	public List<Point> getPoints() {
		return collisionDetails.stream()
		    .map(cd -> cd.getIntersection())
		    .collect(Collectors.toList());
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

package com.github.schuettec.cobra2d.entity;

import static java.util.Objects.nonNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.schuettec.cobra2d.entity.skills.Entity;

/**
 * This is an internal implementation to map {@link Entity} objects to
 * {@link Collision} objects by their identity hash. This is used to easily
 * answer the bidirectional collision request.
 *
 * @author Chris
 *
 */
public class CollisionMap implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Holds all collisions unidirectional.
	 */
	protected List<Collision> flatList;
	/**
	 * Holds all collision bidirectional.
	 */
	protected Map<Integer, List<Collision>> collisions;

	public CollisionMap() {
		this.flatList = new LinkedList<>();
		this.collisions = new Hashtable<>();
	}

	public int size() {
		return flatList.size();
	}

	public boolean isEmpty() {
		return flatList.isEmpty();
	}

	public void addCollisionsUnidirectional(Collision collision) {
		if (nonNull(collision)) {
			_addCollision(collision);
		}
	}

	public void addCollisionsBidirectional(Collision collision, Collision reverse) {
		if (nonNull(collision)) {
			_addCollision(collision);
		}
		if (nonNull(reverse)) {
			_addCollision(reverse);
		}
	}

	private void _addCollision(Collision collision) {
		Entity entity = collision.getEntity();
		int identityHash = System.identityHashCode(entity);
		if (collisions.containsKey(identityHash)) {
			collisions.get(identityHash)
			    .add(collision);
		} else {
			List<Collision> collisons = new LinkedList<>();
			collisions.put(identityHash, collisons);
		}
		flatList.add(collision);
	}

	public boolean hasCollision(Entity entity) {
		int identityHash = System.identityHashCode(entity);
		if (collisions.containsKey(identityHash)) {
			return !collisions.get(identityHash)
			    .isEmpty();
		} else {
			return false;
		}
	}

	public List<Collision> getCollision(Entity entity) {
		int identityHash = System.identityHashCode(entity);
		return collisions.get(identityHash);
	}

	public void clearCollisions() {
		flatList.clear();
		collisions.clear();
	}

	public List<Collision> getCollisions() {
		return Collections.unmodifiableList(flatList);
	}
}

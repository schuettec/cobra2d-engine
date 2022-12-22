package com.github.schuettec.cobra2d.entity;

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
	protected Map<Integer, Collision> collisions;

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

	public void addCollisionsBidirectional(Collision collision, Collision reverse) {
		addCollisionUnidirectional(collision);
		_addCollision(collision);
		_addCollision(reverse);
	}

	private void _addCollision(Collision collision) {
		Entity entity = collision.getEntity();
		int identityHash = System.identityHashCode(entity);
		collisions.put(identityHash, collision);
	}

	public void addCollisionUnidirectional(Collision collision) {
		this.flatList.add(collision);
		_addCollision(collision);
	}

	public boolean hasCollision(Entity entity) {
		int identityHash = System.identityHashCode(entity);
		return collisions.containsKey(identityHash);
	}

	public Collision getCollision(Entity entity) {
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

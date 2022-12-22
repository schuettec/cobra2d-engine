package com.github.schuettec.cobra2d.entity.skills;

import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.Collision;

/**
 * This interface describes an updateable object. This object provides an update
 * method that is called by the rendering framework for every new frame.
 * 
 * @author Chris
 *
 */
public interface Updatable extends Entity {

	/**
	 * Called by the rendering framework to signal that the object should update for
	 * the new frame.
	 * 
	 * @param collisions If this entity is also an {@link Obstacle}, the list of collisions contains the entities
	 *        colliding with this one. Otherwise the collisions list is empty.
	 */
	public void update(Controller controller, List<Collision> collisions);
}

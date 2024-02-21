package com.github.schuettec.cobra2d.entity.skills;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.world.WorldAccess;

/**
 * Entity skill to receive controller updates before updating as an
 * {@link Updatable}.
 */
public interface Controllable extends Skill {

	/**
	 * Called by the engine to let this entity process the controller input. The
	 * engine takes care, that this method is called before
	 * {@link Updatable#update(com.github.schuettec.cobra2d.world.WorldAccess, float)}.
	 *
	 * @param worldAccess The game world access object.
	 *
	 * @param controller  The controller state.
	 */
	public void processControllerState(WorldAccess worldAccess, Controller controller);

}

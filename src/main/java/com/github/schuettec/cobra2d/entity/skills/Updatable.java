package com.github.schuettec.cobra2d.entity.skills;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.map.Map;

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
	 * @param map The map.
	 * @param controller The controller for user inputer.
	 */
	public void update(Map map, Controller controller);
}

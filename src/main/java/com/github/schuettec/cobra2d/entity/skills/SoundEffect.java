package com.github.schuettec.cobra2d.entity.skills;

import java.util.Optional;

import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.renderer.SoundAccess;

/**
 * An entity skill that emits sound effects.
 */
public interface SoundEffect extends Entity {

	/**
	 * @return Returns the sound range collision shape. Used by cameras to determine the stereo panorama based on
	 *         camera distance and position.
	 */
	public Circle getSoundRangeInWorldCoordinates();

	/**
	 * Updates the sound parameter relative to the specified entity. Used to apply pan and volumen and other effect based
	 * on the position of the listener.
	 * 
	 * @param soundAccess
	 * @param relativeTo The listener entity (optional)
	 */
	public void updateSound(SoundAccess soundAccess, Optional<Entity> relativeTo);

}

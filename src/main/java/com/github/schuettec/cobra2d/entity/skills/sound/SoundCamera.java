package com.github.schuettec.cobra2d.entity.skills.sound;

import java.util.List;

import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.SoundEffect;
import com.github.schuettec.cobra2d.math.Shape;
import com.github.schuettec.cobra2d.renderer.SoundAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

/**
 * Ability of a camera to playback sounds.
 */
public interface SoundCamera extends Camera {

	Shape getSoundRangeInWorlCoordinates();

	/**
	 * Signals {@link SoundEffect} entities to update sound parameters and playback.
	 * 
	 * @param soundAccess The access to the sound system.
	 * @param map The map
	 * @param capturedSoundEffects The captured sound effects in the camera's reach.
	 */
	void playback(final SoundAccess soundAccess, final Cobra2DWorld map, final List<SoundEffect> capturedSoundEffects);

}

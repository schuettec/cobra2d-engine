package com.github.schuettec.cobra2d.renderer;

public interface Sound {
	/**
	 * Plays the sound. If the sound is already playing, it will be played again, concurrently.
	 * 
	 * @return the id of the sound instance if successful, or -1 on failure.
	 */
	public long play();

	/**
	 * Plays the sound. If the sound is already playing, it will be played again, concurrently.
	 * 
	 * @param volume the volume in the range [0,1]
	 * @return the id of the sound instance if successful, or -1 on failure.
	 */
	public long play(float volume);

	/**
	 * Plays the sound. If the sound is already playing, it will be played again, concurrently.
	 * 
	 * @param volume the volume in the range [0,1]
	 * @param pitch the pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and
	 *        2.0
	 * @param pan panning in the range -1 (full left) to 1 (full right). 0 is center position.
	 * @return the id of the sound instance if successful, or -1 on failure.
	 */
	public long play(float volume, float pitch, float pan);

	/**
	 * Plays the sound, looping. If the sound is already playing, it will be played again, concurrently.
	 * 
	 * @return the id of the sound instance if successful, or -1 on failure.
	 */
	public long loop();

	/**
	 * Plays the sound, looping. If the sound is already playing, it will be played again, concurrently. You need to stop
	 * the
	 * sound via a call to {@link #stop(long)} using the returned id.
	 * 
	 * @param volume the volume in the range [0, 1]
	 * @return the id of the sound instance if successful, or -1 on failure.
	 */
	public long loop(float volume);

	/**
	 * Plays the sound, looping. If the sound is already playing, it will be played again, concurrently. You need to stop
	 * the
	 * sound via a call to {@link #stop(long)} using the returned id.
	 * 
	 * @param volume the volume in the range [0,1]
	 * @param pitch the pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and
	 *        2.0
	 * @param pan panning in the range -1 (full left) to 1 (full right). 0 is center position.
	 * @return the id of the sound instance if successful, or -1 on failure.
	 */
	public long loop(float volume, float pitch, float pan);

	/** Stops playing all instances of this sound. */
	public void stop();

	/** Pauses all instances of this sound. */
	public void pause();

	/** Resumes all paused instances of this sound. */
	public void resume();

	/** Releases all the resources. */
	public void dispose();

	/**
	 * Stops the sound instance with the given id as returned by {@link #play()} or {@link #play(float)}. If the sound is
	 * no
	 * longer playing, this has no effect.
	 * 
	 * @param soundId the sound id
	 */
	public void stop(long soundId);

	/**
	 * Pauses the sound instance with the given id as returned by {@link #play()} or {@link #play(float)}. If the sound is
	 * no
	 * longer playing, this has no effect.
	 * 
	 * @param soundId the sound id
	 */
	public void pause(long soundId);

	/**
	 * Resumes the sound instance with the given id as returned by {@link #play()} or {@link #play(float)}. If the sound
	 * is not
	 * paused, this has no effect.
	 * 
	 * @param soundId the sound id
	 */
	public void resume(long soundId);

	/**
	 * Sets the sound instance with the given id to be looping. If the sound is no longer playing this has no effect.s
	 * 
	 * @param soundId the sound id
	 * @param looping whether to loop or not.
	 */
	public void setLooping(long soundId, boolean looping);

	/**
	 * Changes the pitch multiplier of the sound instance with the given id as returned by {@link #play()} or
	 * {@link #play(float)}. If the sound is no longer playing, this has no effect.
	 * 
	 * @param soundId the sound id
	 * @param pitch the pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and
	 *        2.0
	 */
	public void setPitch(long soundId, float pitch);

	/**
	 * Changes the volume of the sound instance with the given id as returned by {@link #play()} or {@link #play(float)}.
	 * If the
	 * sound is no longer playing, this has no effect.
	 * 
	 * @param soundId the sound id
	 * @param volume the volume in the range 0 (silent) to 1 (max volume).
	 */
	public void setVolume(long soundId, float volume);

	/**
	 * Sets the panning and volume of the sound instance with the given id as returned by {@link #play()} or
	 * {@link #play(float)}.
	 * If the sound is no longer playing, this has no effect. Note that panning only works for mono sounds, not for stereo
	 * sounds!
	 * 
	 * @param soundId the sound id
	 * @param pan panning in the range -1 (full left) to 1 (full right). 0 is center position.
	 * @param volume the volume in the range [0,1].
	 */
	public void setPan(long soundId, float pan, float volume);
}

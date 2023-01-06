package com.github.schuettec.cobra2d.renderer.libgdx;

import com.github.schuettec.cobra2d.renderer.Sound;

public class SoundAdapter implements Sound {

	private com.badlogic.gdx.audio.Sound soundDelegate;

	public SoundAdapter(com.badlogic.gdx.audio.Sound soundGdx) {
		this.soundDelegate = soundGdx;
	}

	@Override
	public long play() {
		return soundDelegate.play();
	}

	@Override
	public long play(float volume) {
		return soundDelegate.play(volume);
	}

	@Override
	public long play(float volume, float pitch, float pan) {
		return soundDelegate.play(volume, pitch, pan);
	}

	@Override
	public long loop() {
		return soundDelegate.loop();
	}

	@Override
	public long loop(float volume) {
		return soundDelegate.loop(volume);
	}

	@Override
	public long loop(float volume, float pitch, float pan) {
		return soundDelegate.loop(volume, pitch, pan);
	}

	@Override
	public void stop() {
		soundDelegate.stop();
	}

	@Override
	public void pause() {
		soundDelegate.pause();
	}

	@Override
	public void resume() {
		soundDelegate.resume();
	}

	@Override
	public void dispose() {
		soundDelegate.dispose();
	}

	@Override
	public void stop(long soundId) {
		soundDelegate.stop(soundId);
	}

	@Override
	public void pause(long soundId) {
		soundDelegate.pause(soundId);
	}

	@Override
	public void resume(long soundId) {
		soundDelegate.resume(soundId);
	}

	@Override
	public void setLooping(long soundId, boolean looping) {
		soundDelegate.setLooping(soundId, looping);
	}

	@Override
	public void setPitch(long soundId, float pitch) {
		soundDelegate.setPitch(soundId, pitch);
	}

	@Override
	public void setVolume(long soundId, float volume) {
		soundDelegate.setVolume(soundId, volume);
	}

	@Override
	public void setPan(long soundId, float pan, float volume) {
		soundDelegate.setPan(soundId, pan, volume);
	}

}

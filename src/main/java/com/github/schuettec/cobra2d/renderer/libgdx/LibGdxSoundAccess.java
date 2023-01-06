package com.github.schuettec.cobra2d.renderer.libgdx;

import com.github.schuettec.cobra2d.renderer.Sound;
import com.github.schuettec.cobra2d.renderer.SoundAccess;

public class LibGdxSoundAccess implements SoundAccess {

	private LibGdxRenderer libGdxRenderer;

	public LibGdxSoundAccess(LibGdxRenderer libGdxRenderer) {
		this.libGdxRenderer = libGdxRenderer;
	}

	@Override
	public Sound getSound(String soundRessourceId) {
		com.badlogic.gdx.audio.Sound soundGdx = libGdxRenderer.getSound(soundRessourceId);
		Sound sound = new SoundAdapter(soundGdx);
		return sound;
	}

}

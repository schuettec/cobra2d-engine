package com.github.schuettec.cobra2d.renderer.libgdx;

import com.badlogic.gdx.graphics.Texture;
import com.github.schuettec.cobra2d.renderer.RendererResource;

public class LibGdxResource implements RendererResource {

	private Texture texture;

	public LibGdxResource(Texture texture) {
		this.texture = texture;
	}

	@Override
	public void dispose() {
		texture.dispose();
	}

	@Override
	public Texture getResource() {
		return texture;
	}

}

package com.github.schuettec.cobra2d.renderer.j2d;

import java.awt.image.VolatileImage;

import com.github.schuettec.cobra2d.renderer.RendererResource;

public class Java2DRendererResource implements RendererResource {

	private VolatileImage texture;

	public Java2DRendererResource(VolatileImage texture) {
		super();
		this.texture = texture;
	}

	public VolatileImage getTexture() {
		return texture;
	}

	@Override
	public void dispose() {
		texture.flush();
		this.texture = null;
	}

	@Override
	public VolatileImage getResource() {
		return texture;
	}

}

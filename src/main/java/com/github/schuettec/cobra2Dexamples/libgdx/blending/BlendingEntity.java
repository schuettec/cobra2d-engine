package com.github.schuettec.cobra2Dexamples.libgdx.blending;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class BlendingEntity extends TexturedEntity {

	public BlendingEntity(String textureId, Point worldCoordinates, int layer, boolean playerControlled) {
		super(textureId, worldCoordinates, layer, playerControlled);
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		super.render(renderer, position);
	}

}

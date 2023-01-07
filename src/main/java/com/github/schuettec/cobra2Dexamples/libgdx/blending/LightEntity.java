package com.github.schuettec.cobra2Dexamples.libgdx.blending;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxExtendedAccess;

public class LightEntity extends TexturedEntity {

	private static final long serialVersionUID = 1L;

	public LightEntity(String textureId, Point worldCoordinates, Dimension initialDimension, int layer,
	    boolean playerControlled) {
		super(textureId, worldCoordinates, initialDimension, layer);
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		Dimension textureDimension = renderer.getTextureDimension(getTextureId());
		/*
		 * Without the next translation, the texture is rendered at the entities center point,
		 * so we have to translate by the half of the texture dimension
		 */
		Point texturePosition = renderer.getTexturePosition(getTextureId(), getPosition(), position);
		Point textureCenter = renderer.getTextureCenter(getTextureId());
		LibGdxExtendedAccess extendedRenderer = renderer.extendedRenderer(LibGdxExtendedAccess.class);

		extendedRenderer.drawLightTexture(getTextureId(), texturePosition.getRoundX(), texturePosition.getRoundY(),
		    textureCenter.getRoundX(), textureCenter.getRoundY(), (float) textureDimension.getWidth(),
		    textureDimension.getRoundHeight(), (float) this.getScale(), (float) this.getScale(), (float) getDegrees(), 0, 0,
		    textureDimension.getRoundWidth(), textureDimension.getRoundHeight(), false, false);

	}

}

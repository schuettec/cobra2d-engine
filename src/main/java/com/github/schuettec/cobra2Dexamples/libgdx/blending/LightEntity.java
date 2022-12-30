package com.github.schuettec.cobra2Dexamples.libgdx.blending;

import java.awt.Dimension;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxExtendedAccess;

public class LightEntity extends TexturedEntity {

	private static final long serialVersionUID = 1L;

	public LightEntity(String textureId, Point worldCoordinates, int layer, boolean playerControlled) {
		super(textureId, worldCoordinates, layer, playerControlled);
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		Dimension textureDimension = renderer.getTextureDimension(textureId);
		createRectangleShape(textureDimension);
		/*
		 * Without the next translation, the texture is rendered at the entities center point,
		 * so we have to translate by the half of the texture dimension
		 */
		Point texturePosition = renderer.getTexturePosition(textureId, getPosition(), position);
		Point textureCenter = renderer.getTextureCenter(textureId);
		LibGdxExtendedAccess extendedRenderer = renderer.extendedRenderer(LibGdxExtendedAccess.class);

		extendedRenderer.drawLightTexture(textureId, 1f, (float) texturePosition.getRoundX(),
		    (float) texturePosition.getRoundY(), textureCenter.getRoundX(), textureCenter.getRoundY(),
		    (float) textureDimension.width, (float) textureDimension.height, (float) this.getScale(),
		    (float) this.getScale(), (float) getDegrees(), 0, 0, textureDimension.width, textureDimension.height, false,
		    false);

	}

}

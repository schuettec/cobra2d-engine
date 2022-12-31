package com.github.schuettec.cobra2d.renderer.libgdx;

import java.awt.Dimension;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.schuettec.cobra2d.math.Point;

/**
 * Class providing libGDX specific rendering functions.
 */
public class LibGdxExtendedAccess {

	private LibGdxRenderer renderer;

	public LibGdxExtendedAccess(LibGdxRenderer renderer) {
		this.renderer = renderer;
	}

	public void drawLightTexture(String textureId, float x, float y, float degrees) {
		Point textureCenter = renderer.getRendererAccess()
		    .getTextureCenter(textureId);
		Dimension textureDimension = renderer.getRendererAccess()
		    .getTextureDimension(textureId);
		drawLightTexture(textureId, x, y, textureCenter.getRoundX(), textureCenter.getRoundY(),
		    (float) textureDimension.width, (float) textureDimension.height, (float) 1, 1, (float) degrees, 0, 0,
		    textureDimension.width, textureDimension.height, false, false);
	}

	public void drawLightTexture(String textureId, float x, float y, float degrees, float scale) {
		Point textureCenter = renderer.getRendererAccess()
		    .getTextureCenter(textureId);
		Dimension textureDimension = renderer.getRendererAccess()
		    .getTextureDimension(textureId);
		drawLightTexture(textureId, x, y, textureCenter.getRoundX(), textureCenter.getRoundY(),
		    (float) textureDimension.width, (float) textureDimension.height, (float) scale, scale, (float) degrees, 0, 0,
		    textureDimension.width, textureDimension.height, false, false);
	}

	public void drawLightTexture(String textureId, float x, float y, float originX, float originY, float width,
	    float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight,
	    boolean flipX, boolean flipY) {
		SpriteBatch spriteRenderer = renderer.getSpriteRenderer();
		spriteRenderer.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_SRC_ALPHA);
		renderer.getRendererAccess()
		    .drawTexture(textureId, 1f, x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY,
		        srcWidth, srcHeight, flipX, flipY);
		spriteRenderer.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

}

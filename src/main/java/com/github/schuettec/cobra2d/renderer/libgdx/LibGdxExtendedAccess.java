package com.github.schuettec.cobra2d.renderer.libgdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Class providing libGDX specific rendering functions.
 */
public class LibGdxExtendedAccess {

	private LibGdxRenderer renderer;

	public LibGdxExtendedAccess(LibGdxRenderer renderer) {
		this.renderer = renderer;
	}

	public void drawLightTexture(String textureId, float alpha, float x, float y, float originX, float originY, float width,
	    float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight,
	    boolean flipX, boolean flipY) {
		SpriteBatch spriteRenderer = renderer.getSpriteRenderer();
		spriteRenderer.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_SRC_ALPHA);
		renderer.getRendererAccess()
		    .drawTexture(textureId, alpha, x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY,
		        srcWidth, srcHeight, flipX, flipY);
		spriteRenderer.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

}

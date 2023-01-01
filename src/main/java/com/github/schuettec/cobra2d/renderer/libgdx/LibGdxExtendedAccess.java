package com.github.schuettec.cobra2d.renderer.libgdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.schuettec.cobra2d.math.Dimension;
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
		    (float) textureDimension.getWidth(), (float) textureDimension.getHeight(), (float) 1, 1, (float) degrees, 0, 0,
		    textureDimension.getRoundWidth(), textureDimension.getRoundHeight(), false, false);
	}

	public void drawLightTexture(String textureId, float x, float y, float degrees, float scale) {
		Point textureCenter = renderer.getRendererAccess()
		    .getTextureCenter(textureId);
		Dimension textureDimension = renderer.getRendererAccess()
		    .getTextureDimension(textureId);
		drawLightTexture(textureId, x, y, textureCenter.getRoundX(), textureCenter.getRoundY(),
		    (float) textureDimension.getWidth(), (float) textureDimension.getHeight(), scale, scale, (float) degrees, 0, 0,
		    textureDimension.getRoundWidth(), textureDimension.getRoundHeight(), false, false);
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

	public Texture getTexture(String textureId) {
		return renderer.getTexture(textureId);
	}

	public void drawTextureRegion(TextureRegion textureRegion, float alpha, float x, float y, float degrees) {
		Point textureCenter = new Point(textureRegion.getRegionWidth() / 2.0, textureRegion.getRegionHeight() / 2.0);

		SpriteBatch spriteRenderer = renderer.getSpriteRenderer();
		spriteRenderer.begin();
		com.badlogic.gdx.graphics.Color color = spriteRenderer.getColor();
		spriteRenderer.setColor(color.r, color.g, color.b, alpha);
		spriteRenderer.draw(textureRegion, x, y, (float) textureCenter.getRoundX(), (float) textureCenter.getRoundY(),
		    (float) textureRegion.getRegionWidth(), (float) textureRegion.getRegionHeight(), 1f, 1f, (float) degrees);
		spriteRenderer.end();
	}

}

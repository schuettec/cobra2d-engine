package com.github.schuettec.cobra2d.renderer.libgdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class LibGdxRendererAccess implements RendererAccess {

	private LibGdxRenderer renderer;
	private LibGdxExtendedAccess extendedRenderer;

	public LibGdxRendererAccess(LibGdxRenderer renderer) {
		this.renderer = renderer;
		this.extendedRenderer = new LibGdxExtendedAccess(renderer);
	}

	@Override
	public void drawTexture(String textureId, float alpha, float x, float y, float degrees) {
		Point textureCenter = getTextureCenter(textureId);
		Dimension textureDimension = getTextureDimension(textureId);
		drawTexture(textureId, alpha, x, y, textureCenter.getRoundX(), textureCenter.getRoundY(),
		    (float) textureDimension.getWidth(), (float) textureDimension.getHeight(), (float) 1, 1, (float) degrees, 0, 0,
		    textureDimension.getRoundWidth(), textureDimension.getRoundHeight(), false, false);
	}

	@Override
	public void drawTexture(String textureId, float alpha, float x, float y, float degrees, float scale) {
		Point textureCenter = getTextureCenter(textureId);
		Dimension textureDimension = getTextureDimension(textureId);
		drawTexture(textureId, alpha, x, y, textureCenter.getRoundX(), textureCenter.getRoundY(),
		    (float) textureDimension.getWidth(), (float) textureDimension.getHeight(), (float) scale, scale,
		    (float) degrees, 0, 0, textureDimension.getRoundWidth(), textureDimension.getRoundHeight(), false, false);
	}

	@Override
	public void drawTexture(String textureId, float alpha, float x, float y, float originX, float originY, float width,
	    float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight,
	    boolean flipX, boolean flipY) {
		SpriteBatch spriteRenderer = renderer.getSpriteRenderer();
		Texture texture = renderer.getTexture(textureId);
		spriteRenderer.begin();
		com.badlogic.gdx.graphics.Color color = spriteRenderer.getColor();
		spriteRenderer.setColor(color.r, color.g, color.b, alpha);
		spriteRenderer.draw(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY, srcWidth,
		    srcHeight, flipX, flipY);
		spriteRenderer.end();
	}

	@Override
	public void drawCircle(float x, float y, float radius, Color color) {
		ShapeRenderer shapeRenderer = renderer.getShapeRenderer();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(color.toLibGdxColor());
		shapeRenderer.circle(x, y, radius, 100);
		shapeRenderer.end();
	}

	@Override
	public void drawLine(float x1, float y1, float x2, float y2, Color color) {
		ShapeRenderer shapeRenderer = renderer.getShapeRenderer();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.line(x1, y1, x2, y2, color.toLibGdxColor(), color.toLibGdxColor());
		shapeRenderer.end();
	}

	@Override
	public void drawOval(float x1, float y1, float width, float height, Color color) {
		ShapeRenderer shapeRenderer = renderer.getShapeRenderer();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(color.toLibGdxColor());
		shapeRenderer.ellipse(x1, y1, width, height, 100);
		shapeRenderer.end();
	}

	@Override
	public void fillOval(float x1, float y1, float width, float height, Color color) {
		ShapeRenderer shapeRenderer = renderer.getShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color.toLibGdxColor());
		shapeRenderer.ellipse(x1, y1, width, height, 100);
		shapeRenderer.end();
	}

	@Override
	public void fillCircle(float x, float y, float radius, Color color) {
		ShapeRenderer shapeRenderer = renderer.getShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color.toLibGdxColor());
		shapeRenderer.circle(x, y, radius, 100);
		shapeRenderer.end();
	}

	@Override
	public void drawRectangle(float x, float y, float width, float height, Color color) {
		ShapeRenderer shapeRenderer = renderer.getShapeRenderer();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(color.toLibGdxColor());
		shapeRenderer.rect(x, y, width, height);
		shapeRenderer.end();
	}

	@Override
	public void fillRectangle(float x, float y, float width, float height, Color color) {
		ShapeRenderer shapeRenderer = renderer.getShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color.toLibGdxColor());
		shapeRenderer.rect(x, y, width, height);
		shapeRenderer.end();
	}

	@Override
	public int getWidth() {
		return renderer.getResolutionX();
	}

	@Override
	public int getHeight() {
		return renderer.getResolutionY();
	}

	@Override
	public Dimension getTextureDimension(String textureId) {
		Texture texture = renderer.getTexture(textureId);
		int width = texture.getWidth();
		int height = texture.getHeight();
		return new Dimension(width, height);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R extendedRenderer(Class<R> rendererType) {
		return (R) extendedRenderer;
	}

}

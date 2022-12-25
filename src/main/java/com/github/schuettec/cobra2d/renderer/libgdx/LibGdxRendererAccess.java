package com.github.schuettec.cobra2d.renderer.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.github.schuettec.cobra2d.renderer.common.Color;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public class LibGdxRendererAccess implements RendererAccess {

	private LibGdxRenderer renderer;

	public LibGdxRendererAccess(LibGdxRenderer renderer) {
		this.renderer = renderer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R getNativeRenderer(Class<R> rendererType) {
		return (R) renderer;
	}

	@Override
	public void drawTexture() {
		SpriteBatch spriteRenderer = renderer.getSpriteRenderer();
		Texture texture = new Texture(Gdx.files.internal("image.png"));
		TextureRegion region = new TextureRegion(texture, 20, 20, 50, 50);
		Sprite sprite = new Sprite(texture, 20, 20, 50, 50);
		sprite.setPosition(100, 10);
		sprite.setColor(0, 0, 1, 1);
		spriteRenderer.begin();
		spriteRenderer.setColor(1, 0, 0, 1);
		spriteRenderer.draw(texture, 10, 10);
		spriteRenderer.setColor(0, 1, 0, 1);
		spriteRenderer.draw(region, 50, 10);
		sprite.draw(spriteRenderer);
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

}

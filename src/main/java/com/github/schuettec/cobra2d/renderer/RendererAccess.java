package com.github.schuettec.cobra2d.renderer;

import static com.github.schuettec.cobra2d.math.Math2D.saveRound;

import java.awt.Dimension;

import com.github.schuettec.cobra2d.math.Point;

/**
 * A common interface that gives access to general drawing and rendering functions across platforms.
 */
public interface RendererAccess {

	/**
	 * Returns the current renderer as expected type. Note that the renderer type depends on the engine's configuration.
	 * Not all entities may support all renderers.
	 * 
	 * @param <R> The expected renderer type.
	 * @param rendererType The expected renderer type.
	 * @return Returns the renderer.
	 */
	<R> R extendedRenderer(Class<R> rendererType);

	void drawCircle(float x, float y, float radius, Color color);

	void fillCircle(float x, float y, float radius, Color color);

	void drawLine(float x1, float y1, float x2, float y2, Color color);

	void drawOval(float x1, float y1, float width, float height, Color color);

	void fillOval(float x1, float y1, float width, float height, Color color);

	void drawRectangle(float x, float y, float width, float height, Color color);

	void fillRectangle(float x, float y, float width, float height, Color color);

	int getWidth();

	int getHeight();

	void drawTexture(String textureId, float alpha, float x, float y, float originX, float originY, float width,
	    float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight,
	    boolean flipX, boolean flipY);

	void drawTexture(String textureId, float alpha, float roundX, float roundY, float degrees);

	void drawTexture(String textureId, float alpha, float roundX, float roundY, float degrees, float scale);

	Dimension getTextureDimension(String textureId);

	default Point getTextureCenter(String textureId) {
		Dimension textureDimension = getTextureDimension(textureId);
		return new Point(saveRound(textureDimension.width / 2.0), saveRound(textureDimension.height / 2.0));
	}
}

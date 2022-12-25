package com.github.schuettec.cobra2d.renderer.common;

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
	public <R> R getNativeRenderer(Class<R> rendererType);

	public void drawCircle(float x, float y, float radius, Color color);

	public void fillCircle(float x, float y, float radius, Color color);

	public void drawLine(float x1, float y1, float x2, float y2, Color color);

	public void drawOval(float x1, float y1, float width, float height, Color color);

	public void fillOval(float x1, float y1, float width, float height, Color color);

	public void drawRectangle(float x, float y, float width, float height, Color color);

	public void fillRectangle(float x, float y, float width, float height, Color color);

	public int getWidth();

	public int getHeight();

	public void drawTexture();

}

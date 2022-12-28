package com.github.schuettec.cobra2d.renderer.j2d;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.VolatileImage;

import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class Java2DRendererAccess implements RendererAccess {

	private WindowRenderer renderer;

	public Java2DRendererAccess(WindowRenderer renderer) {
		this.renderer = renderer;

	}

	@Override
	public void drawTexture(String imageId, float x, float y, float originX, float originY, float width, float height,
	    float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX,
	    boolean flipY) {
		throw new IllegalStateException("Not implemented");
	}

	@Override
	public void drawRectangle(float x, float y, float width, float height, Color color) {
		Graphics2D g = renderer.getGraphics();
		java.awt.Color oldColor = g.getColor();
		g.setColor(color.toAwtColor());
		g.drawRect(Math2D.saveRound(x), Math2D.saveRound(y), Math2D.saveRound(width), Math2D.saveRound(height));
		g.setColor(oldColor);
	}

	@Override
	public void fillRectangle(float x, float y, float width, float height, Color color) {
		Graphics2D g = renderer.getGraphics();
		java.awt.Color oldColor = g.getColor();
		g.setColor(color.toAwtColor());
		g.fillRect(Math2D.saveRound(x), Math2D.saveRound(y), Math2D.saveRound(width), Math2D.saveRound(height));
		g.setColor(oldColor);
	}

	@Override
	public void drawCircle(float x, float y, float radius, Color color) {
		drawOval(x, y, radius, radius, color);
	}

	@Override
	public void drawLine(float x1, float y1, float x2, float y2, Color color) {
		Graphics2D g = renderer.getGraphics();
		java.awt.Color oldColor = g.getColor();
		g.setColor(color.toAwtColor());
		g.drawLine(Math2D.saveRound(x1), Math2D.saveRound(y1), Math2D.saveRound(x2), Math2D.saveRound(y2));
		g.setColor(oldColor);
	}

	@Override
	public void drawOval(float x1, float y1, float width, float height, Color color) {
		int drawPositionX = Math2D.saveRound(x1 - width);
		int drawPositionY = Math2D.saveRound(y1 - height);
		int adjustedWidth = Math2D.saveRound(2 * width);
		int adjustedHeight = Math2D.saveRound(2 * height);
		Graphics2D g = renderer.getGraphics();
		java.awt.Color oldColor = g.getColor();
		g.setColor(color.toAwtColor());
		g.drawOval(drawPositionX, drawPositionY, adjustedWidth, adjustedHeight);
		g.setColor(oldColor);
	}

	@Override
	public void fillOval(float x1, float y1, float width, float height, Color color) {
		int drawPositionX = Math2D.saveRound(x1 - width);
		int drawPositionY = Math2D.saveRound(y1 - height);
		int adjustedWidth = Math2D.saveRound(2 * width);
		int adjustedHeight = Math2D.saveRound(2 * height);
		Graphics2D g = renderer.getGraphics();
		java.awt.Color oldColor = g.getColor();
		g.setColor(color.toAwtColor());
		g.fillOval(drawPositionX, drawPositionY, adjustedWidth, adjustedHeight);
		g.setColor(oldColor);
	}

	@Override
	public void fillCircle(float x, float y, float radius, Color color) {
		fillOval(x, y, radius, radius, color);
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
		VolatileImage volatileImage = renderer.getTexture(textureId);
		return new Dimension(volatileImage.getWidth(), volatileImage.getHeight());
	}

	@Override
	public <R> R extendedRenderer(Class<R> rendererType) {
		throw new IllegalAccessError("Not yet implemented.");
	}

}

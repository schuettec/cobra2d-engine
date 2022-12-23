package com.github.schuettec.cobra2d.renderer.j2d;

import java.awt.Graphics2D;

import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.renderer.common.Color;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public class Java2DRendererAccess implements RendererAccess {

	private Graphics2D renderer;

	public Java2DRendererAccess(Graphics2D renderer) {
		this.renderer = renderer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R getNativeRenderer(Class<R> rendererType) {
		return (R) renderer;
	}

	@Override
	public void drawCircle(float x, float y, float radius, Color color) {
		drawOval(x, y, radius, radius, color);
	}

	@Override
	public void drawLine(float x1, float y1, float x2, float y2, Color color) {
		java.awt.Color oldColor = renderer.getColor();
		renderer.setColor(color.toAwtColor());
		renderer.drawLine(Math2D.saveRound(x1), Math2D.saveRound(y1), Math2D.saveRound(x2), Math2D.saveRound(y2));
		renderer.setColor(oldColor);
	}

	@Override
	public void drawOval(float x1, float y1, float width, float height, Color color) {
		int drawPositionX = Math2D.saveRound(x1 - width);
		int drawPositionY = Math2D.saveRound(y1 - height);
		int adjustedWidth = Math2D.saveRound(2 * width);
		int adjustedHeight = Math2D.saveRound(2 * height);

		java.awt.Color oldColor = renderer.getColor();
		renderer.setColor(color.toAwtColor());
		renderer.drawOval(drawPositionX, drawPositionY, adjustedWidth, adjustedHeight);
		renderer.setColor(oldColor);
	}

	@Override
	public void fillOval(float x1, float y1, float width, float height, Color color) {
		int drawPositionX = Math2D.saveRound(x1 - width);
		int drawPositionY = Math2D.saveRound(y1 - height);
		int adjustedWidth = Math2D.saveRound(2 * width);
		int adjustedHeight = Math2D.saveRound(2 * height);

		java.awt.Color oldColor = renderer.getColor();
		renderer.setColor(color.toAwtColor());
		renderer.fillOval(drawPositionX, drawPositionY, adjustedWidth, adjustedHeight);
		renderer.setColor(oldColor);
	}

	@Override
	public void fillCircle(float x, float y, float radius, Color color) {
		fillOval(x, y, radius, radius, color);
	}

}

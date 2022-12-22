package com.github.schuettec.cobra2d.entity.skills;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;

public interface CircleRenderable extends Renderable {

	default void renderCircle(Circle circle, final Graphics2D graphics, final Point position) {
		Color oldColor = graphics.getColor();
		graphics.setColor(getDrawColor());
		// Translate world coordinates to screen coordinates
		Circle circleOnScreen = circle.clone()
		    .translate(position);

		int drawPositionX = Math2D.saveRound(circleOnScreen.getPosition()
		    .getRoundX() - circle.getRadius());
		int drawPositionY = Math2D.saveRound(circleOnScreen.getPosition()
		    .getRoundY() - circle.getRadius());

		int adjustedRadius = Math2D.saveRound(2 * circle.getRadius());
		graphics.drawOval(drawPositionX, drawPositionY, adjustedRadius, adjustedRadius);
		graphics.setColor(oldColor);
	}

	/**
	 * Returns a foreground color, the shape should be rendered in.
	 */
	Color getDrawColor();
}

package com.github.schuettec.cobra2d.entity.skills;

import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.common.Color;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public interface CircleRenderable extends Renderable {

	default void renderCircle(Circle circle, final RendererAccess renderer, final Point position) {
		// Translate world coordinates to screen coordinates
		Circle circleOnScreen = circle.clone()
		    .translate(position);
		renderer.drawCircle(circleOnScreen.getPosition()
		    .getRoundX(),
		    circleOnScreen.getPosition()
		        .getRoundY(),
		    (float) circle.getRadius(), getDrawColor());
	}

	/**
	 * Returns a foreground color, the shape should be rendered in.
	 */
	Color getDrawColor();
}

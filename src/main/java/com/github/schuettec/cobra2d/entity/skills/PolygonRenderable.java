package com.github.schuettec.cobra2d.entity.skills;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;

public interface PolygonRenderable extends Renderable {

	default void renderPolygon(Polygon polygon, final Graphics2D graphics, final Point position) {
		Color oldColor = graphics.getColor();
		graphics.setColor(getDrawColor());
		// Translate world coordinates to screen coordinates
		Polygon polygonOnScreen = polygon.clone()
		    .translate(position);

		List<Line> lines = polygonOnScreen.getLines();
		for (Line l : lines) {
			graphics.drawLine(l.getX1()
			    .getRoundX(),
			    l.getX1()
			        .getRoundY(),
			    l.getX2()
			        .getRoundX(),
			    l.getX2()
			        .getRoundY());
		}
		graphics.setColor(oldColor);
	}

	/**
	 * Returns a foreground color, the shape should be rendered in.
	 */
	Color getDrawColor();
}

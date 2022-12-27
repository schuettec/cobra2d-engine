package com.github.schuettec.cobra2d.entity.skills;

import java.util.List;

import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public interface PolygonRenderable extends Renderable {
	public static void renderPolygon(Polygon polygon, final RendererAccess renderer, final Point position, Color color) {
		// Translate world coordinates to screen coordinates
		Polygon polygonOnScreen = polygon.clone()
		    .translate(position);

		List<Line> lines = polygonOnScreen.getLines();
		for (Line l : lines) {
			renderer.drawLine(l.getX1()
			    .getRoundX(),
			    l.getX1()
			        .getRoundY(),
			    l.getX2()
			        .getRoundX(),
			    l.getX2()
			        .getRoundY(),
			    color);
		}
	}

	default void renderPolygon(Polygon polygon, final RendererAccess renderer, final Point position) {
		renderPolygon(polygon, renderer, position, getDrawColor());
	}

	/**
	 * Returns a foreground color, the shape should be rendered in.
	 */
	Color getDrawColor();
}

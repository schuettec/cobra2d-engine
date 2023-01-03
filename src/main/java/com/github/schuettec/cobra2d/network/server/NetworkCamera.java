package com.github.schuettec.cobra2d.network.server;

import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;

public class NetworkCamera extends BasicRectangleMapCamera {

	private static final long serialVersionUID = 1L;

	public NetworkCamera(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension, false, new Point(0, 0));
	}

}

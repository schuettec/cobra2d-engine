package com.github.schuettec.cobra2d.network.server;

import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;

public class ServerCamera extends BasicRectangleMapCamera {

	private static final long serialVersionUID = 1L;

	public ServerCamera(Entity playerEntity, Dimension cameraDimension) {
		super(playerEntity.getPosition(), cameraDimension, false, new Point(0, 0));
	}

}

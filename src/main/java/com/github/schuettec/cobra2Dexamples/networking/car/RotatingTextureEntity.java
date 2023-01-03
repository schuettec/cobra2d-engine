package com.github.schuettec.cobra2Dexamples.networking.car;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class RotatingTextureEntity extends TexturedEntity {

	public RotatingTextureEntity(String textureId, Point worldCoordinates, Dimension intialDimension, int layer,
	    boolean playerControlled) {
		super(textureId, worldCoordinates, intialDimension, layer, playerControlled);
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime, Controller controller) {
		super.update(worldAccess, deltaTime, controller);
		setDegrees(Math2D.normalizeAngle(getDegrees() + 5));

	}

}

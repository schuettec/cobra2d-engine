package com.github.schuettec.cobra2Dexamples.textureRendering;

import static com.github.schuettec.cobra2d.math.Math2D.normalizeAngle;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;

public class RotatableTextureEntity extends TexturedEntity implements Controllable {
	boolean playerControlled;

	public RotatableTextureEntity(String textureId, Point worldCoordinates, Dimension intialDimension, int layer,
	    boolean playerControlled) {
		super(textureId, worldCoordinates, intialDimension, layer);
		this.playerControlled = playerControlled;
	}

	@Override
	public void processControllerState(Controller controller) {
		if (playerControlled) {
			if (controller.isLeftKeyPressed()) {
				this.rotateLeft();
			}
			if (controller.isRightKeyPressed()) {
				this.rotateRight();
			}
		}
	}

	public void rotateLeft() {
		setDegrees(normalizeAngle(getDegrees() - 5));
	}

	public void rotateRight() {
		setDegrees(normalizeAngle(getDegrees() + 5));
	}
}

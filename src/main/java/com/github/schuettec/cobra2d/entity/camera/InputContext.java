package com.github.schuettec.cobra2d.entity.camera;

import com.github.schuettec.cobra2d.math.Point;

/**
 * Input information relative to the capturing camera. Example: The mouse pointer information is calculated relative to
 * the active camera.
 */
public class InputContext {

	private Point mouseWorldCoordinates;

	public InputContext() {
		super();
	}

	public InputContext(Point mouseWorldCoordinates) {
		this.mouseWorldCoordinates = mouseWorldCoordinates;
	}

	public Point getMouseWorldCoordinates() {
		return mouseWorldCoordinates.clone();
	}

}

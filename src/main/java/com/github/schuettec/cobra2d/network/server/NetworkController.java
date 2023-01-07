package com.github.schuettec.cobra2d.network.server;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxController;

public class NetworkController extends LibGdxController {
	private Set<Integer> keysPressed = ConcurrentHashMap.newKeySet();

	private Point mouseWorldCoordinates = new Point();

	public NetworkController(Camera cameraForInput) {
		super(cameraForInput);
	}

	@Override
	public Point getMousePositionOnScreen() {
		throw new UnsupportedOperationException("Screen coordinates are not supported by the network controller.");
	}

	@Override
	public Point getMousePositionWorldCoordinates() {
		return mouseWorldCoordinates;
	}

	public void setMousePositionWorldCoordinates(Point mouseWorldCoordinates) {
		this.mouseWorldCoordinates = mouseWorldCoordinates;
	}

	public void setKeyPressed(int keyCode) {
		keysPressed.add(keyCode);
	}

	@Override
	public boolean isKeyPressed(int keyCode) {
		return keysPressed.contains(keyCode);
	}

	/**
	 * Clears the state of the keys
	 */
	public void clearState() {
		keysPressed.clear();
	}

	public void setKeyPressed(int keyCode, boolean pressed) {
		if (pressed) {
			keysPressed.add(keyCode);
		} else {
			keysPressed.remove(keyCode);
		}
	}

}

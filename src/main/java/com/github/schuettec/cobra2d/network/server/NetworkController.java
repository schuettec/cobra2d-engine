package com.github.schuettec.cobra2d.network.server;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxController;

public class NetworkController extends LibGdxController {

	private Set<Integer> keysPressed = ConcurrentHashMap.newKeySet();

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

}

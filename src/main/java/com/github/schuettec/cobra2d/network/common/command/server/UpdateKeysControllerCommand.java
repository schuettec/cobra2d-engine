
package com.github.schuettec.cobra2d.network.common.command.server;

import com.github.schuettec.cobra2d.world.WorldAccess;

public class UpdateKeysControllerCommand implements ServerCommand<PlayerAccess> {

	private int keyCode;
	private boolean pressed;

	public UpdateKeysControllerCommand() {
	}

	public UpdateKeysControllerCommand(int keyCode, boolean pressed) {
		this.keyCode = keyCode;
		this.pressed = pressed;
	}

	public static UpdateKeysControllerCommand ofKeyCode(int keyCode, boolean pressed) {
		return new UpdateKeysControllerCommand(keyCode, pressed);
	}

	@Override
	public void perform(WorldAccess worldAccess, PlayerAccess playerAccess) {
		playerAccess.updateKeyState(keyCode, pressed);
	}

	public int getKeyCode() {
		return keyCode;
	}

	public boolean isPressed() {
		return pressed;
	}

}

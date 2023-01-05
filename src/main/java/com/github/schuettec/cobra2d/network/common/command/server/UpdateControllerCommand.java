
package com.github.schuettec.cobra2d.network.common.command.server;

import com.github.schuettec.cobra2d.world.WorldAccess;

public class UpdateControllerCommand implements ServerCommand<PlayerAccess> {

	private int keyCode;

	public UpdateControllerCommand() {
	}

	public UpdateControllerCommand(int keyCode) {
		this.keyCode = keyCode;
	}

	public static UpdateControllerCommand ofKeyCode(int keyCode) {
		return new UpdateControllerCommand(keyCode);
	}

	@Override
	public void perform(WorldAccess worldAccess, PlayerAccess playerAccess) {
		playerAccess.updateKeyState(keyCode);
	}

	public int getKeyCode() {
		return keyCode;
	}

}

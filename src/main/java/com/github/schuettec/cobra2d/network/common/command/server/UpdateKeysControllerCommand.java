
package com.github.schuettec.cobra2d.network.common.command.server;

import com.github.schuettec.cobra2d.world.WorldAccess;

public class UpdateKeysControllerCommand implements ServerCommand<PlayerAccess> {

	private int keyCode;

	public UpdateKeysControllerCommand() {
	}

	public UpdateKeysControllerCommand(int keyCode) {
		this.keyCode = keyCode;
	}

	public static UpdateKeysControllerCommand ofKeyCode(int keyCode) {
		return new UpdateKeysControllerCommand(keyCode);
	}

	@Override
	public void perform(WorldAccess worldAccess, PlayerAccess playerAccess) {
		playerAccess.updateKeyState(keyCode);
	}

	public int getKeyCode() {
		return keyCode;
	}

}

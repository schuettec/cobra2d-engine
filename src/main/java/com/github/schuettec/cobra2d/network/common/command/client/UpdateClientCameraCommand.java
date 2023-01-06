package com.github.schuettec.cobra2d.network.common.command.client;

import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.network.client.ClientAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class UpdateClientCameraCommand implements ClientCommand {

	private Point playerCameraPosition;

	public UpdateClientCameraCommand() {
		super();
	}

	public UpdateClientCameraCommand(Point playerCameraPosition) {
		super();
		this.playerCameraPosition = playerCameraPosition;
	}

	@Override
	public void perform(WorldAccess worldAccess, ClientAccess clientAccess) {
		Camera clientCamera = clientAccess.getClientCamera();
		clientCamera.setPosition(playerCameraPosition);
	}

	@Override
	public String toString() {
		return "UpdateClientCamera [playerCameraPosition=" + playerCameraPosition + "]";
	}

}

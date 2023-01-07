package com.github.schuettec.cobra2d.network.common.command.client;

import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.network.client.ClientAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class UpdateClientCameraCommand implements ClientCommand {

	private String followEntityId;
	private Point playerCameraPosition;

	public UpdateClientCameraCommand() {
		super();
	}

	public UpdateClientCameraCommand(Point playerCameraPosition, String followEntityId) {
		super();
		this.playerCameraPosition = playerCameraPosition;
		this.followEntityId = followEntityId;
	}

	@Override
	public void perform(WorldAccess worldAccess, ClientAccess clientAccess) {
		Camera clientCamera = clientAccess.getClientCamera();
		clientCamera.setPosition(playerCameraPosition);
		clientCamera.setFollowEntityId(followEntityId);
	}

	@Override
	public String toString() {
		return "UpdateClientCamera [playerCameraPosition=" + playerCameraPosition + "]";
	}

}

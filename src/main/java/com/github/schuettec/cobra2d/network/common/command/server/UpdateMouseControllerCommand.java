
package com.github.schuettec.cobra2d.network.common.command.server;

import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class UpdateMouseControllerCommand implements ServerCommand<PlayerAccess> {

	private Point mouseWorldCoordinates;

	public UpdateMouseControllerCommand() {
		super();
	}

	public UpdateMouseControllerCommand(Point mouseWorldCoordinates) {
		super();
		this.mouseWorldCoordinates = mouseWorldCoordinates;
	}

	public static UpdateMouseControllerCommand ofMousePosition(Point mouseWorldCoordinates) {
		return new UpdateMouseControllerCommand(mouseWorldCoordinates);
	}

	@Override
	public void perform(WorldAccess worldAccess, PlayerAccess playerAccess) {
		playerAccess.setMousePositionWorldCoordinates(mouseWorldCoordinates);
	}

	public Point getMouseWorldCoordinates() {
		return mouseWorldCoordinates;
	}

}

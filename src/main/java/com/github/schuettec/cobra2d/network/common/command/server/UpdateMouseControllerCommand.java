
package com.github.schuettec.cobra2d.network.common.command.server;

import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class UpdateMouseControllerCommand implements ServerCommand<PlayerAccess> {

	private Point mousePositionOnScreen;

	public UpdateMouseControllerCommand() {
		super();
	}

	public UpdateMouseControllerCommand(Point mousePositionOnScreen) {
		super();
		this.mousePositionOnScreen = mousePositionOnScreen;
	}

	public static UpdateMouseControllerCommand ofMousePosition(Point mousePositionOnScreen) {
		return new UpdateMouseControllerCommand(mousePositionOnScreen);
	}

	@Override
	public void perform(WorldAccess worldAccess, PlayerAccess playerAccess) {
		playerAccess.setMousePositionOnScreen(mousePositionOnScreen);
	}

	public Point getMousePositionOnScreen() {
		return mousePositionOnScreen;
	}

}

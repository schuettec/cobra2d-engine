package com.github.schuettec.cobra2d.network.common.command.server;

import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.network.server.Player;

public class BasicPlayerAccess implements PlayerAccess {

	protected Player player;

	public BasicPlayerAccess() {
		super();
	}

	@Override
	public void disconnectPlayer() {
		player.getConnection()
		    .close();
	}

	@Override
	public final void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public void updateKeyState(int keyCode, boolean pressed) {
		player.getNetworkController()
		    .setKeyPressed(keyCode, pressed);
	}

	@Override
	public void setMousePositionOnScreen(Point mousePositionOnScreen) {
		player.getNetworkController()
		    .setMousePositionOnScreen(mousePositionOnScreen);
	}

}

package com.github.schuettec.cobra2d.network.common.command.server;

import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.network.server.NetworkController;
import com.github.schuettec.cobra2d.network.server.Player;

public interface PlayerAccess {

	/**
	 * Sets the player object, the game server created for this {@link PlayerAccess}.
	 * 
	 * @param player The player object.
	 */
	void setPlayer(Player player);

	/**
	 * Disconnects the player from the server game.
	 */
	void disconnectPlayer();

	/**
	 * Updates the key stated to status 'pressed' in the {@link NetworkController}.
	 * 
	 * @param keyCode The keycode.
	 * @param pressed
	 */
	void updateKeyState(int keyCode, boolean pressed);

	/**
	 * Sets the mouse position on screen for this player.
	 * 
	 * @param mousePositionOnScreen The mouse position on screen.
	 */
	void setMousePositionOnScreen(Point mousePositionOnScreen);

}

package com.github.schuettec.cobra2d.network.common.command.server;

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
	 */
	void updateKeyState(int keyCode);

}

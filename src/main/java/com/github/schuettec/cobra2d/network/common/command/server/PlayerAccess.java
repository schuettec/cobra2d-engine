package com.github.schuettec.cobra2d.network.common.command.server;

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

}

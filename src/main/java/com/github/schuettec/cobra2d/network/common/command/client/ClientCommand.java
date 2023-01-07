package com.github.schuettec.cobra2d.network.common.command.client;

import java.io.Serializable;

import com.github.schuettec.cobra2d.network.client.ClientAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public interface ClientCommand extends Serializable {

	/**
	 * @return Returns <code>true</code> if this command must be send via tcp to ensure the client to receive it.
	 */
	default boolean isTCP() {
		return false;
	}

	public void perform(WorldAccess worldAccess, ClientAccess clientAccess);

}

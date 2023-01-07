package com.github.schuettec.cobra2d.network.common.command.server;

import java.io.Serializable;

import com.github.schuettec.cobra2d.world.WorldAccess;

public interface ServerCommand<P extends PlayerAccess> extends Serializable {

	/**
	 * @return Returns <code>true</code> if this command must be send via tcp to ensure the server to receive it.
	 */
	default boolean isTCP() {
		return false;
	}

	public void perform(WorldAccess worldAccess, P playerAccess);

}

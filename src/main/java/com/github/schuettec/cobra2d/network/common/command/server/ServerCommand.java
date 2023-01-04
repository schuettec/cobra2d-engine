package com.github.schuettec.cobra2d.network.common.command.server;

import java.io.Serializable;

import com.github.schuettec.cobra2d.world.WorldAccess;

public interface ServerCommand<P extends PlayerAccess> extends Serializable {

	public void perform(WorldAccess worldAccess, P playerAccess);

}

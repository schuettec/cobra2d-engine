package com.github.schuettec.cobra2d.network.common.command.client;

import java.io.Serializable;

import com.github.schuettec.cobra2d.network.client.ClientAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public interface ClientCommand extends Serializable {

	public void perform(WorldAccess worldAccess, ClientAccess clientAccess);

}

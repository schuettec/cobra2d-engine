package com.github.schuettec.cobra2d.network.common.command.client;

import com.github.schuettec.cobra2d.network.client.ClientAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public interface ClientCommand {

	public void perform(WorldAccess worldAccess, ClientAccess clientAccess);

}

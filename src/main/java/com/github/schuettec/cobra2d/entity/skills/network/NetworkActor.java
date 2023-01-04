package com.github.schuettec.cobra2d.entity.skills.network;

import java.util.List;

import com.github.schuettec.cobra2d.network.common.command.server.ServerCommand;

/**
 * This skill defines the ability to specify remote commands for the remote player when acting in a network game.
 */
public interface NetworkActor {

	public List<ServerCommand> getRemotePlayerCommands();
}

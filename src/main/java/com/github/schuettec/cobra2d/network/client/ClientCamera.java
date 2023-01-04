package com.github.schuettec.cobra2d.network.client;

import java.util.LinkedList;
import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.entity.skills.network.NetworkActor;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.network.common.command.server.ServerCommand;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class ClientCamera extends BasicRectangleMapCamera implements NetworkActor {

	protected List<ServerCommand> serverCommands;

	public ClientCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled, Point screenPosition) {
		super(worldCoordinates, dimension, playerControlled, screenPosition);
		this.serverCommands = new LinkedList<>();
	}

	public ClientCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled) {
		super(worldCoordinates, dimension, playerControlled);
		this.serverCommands = new LinkedList<>();
	}

	@Override
	public List<ServerCommand> getRemotePlayerCommands() {
		return serverCommands;
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime, Controller controller) {
		updateServerCommands(this.serverCommands, worldAccess, deltaTime, controller);
	}

	protected void updateServerCommands(List<ServerCommand> serverCommands, WorldAccess worldAccess, float deltaTime,
	    Controller controller) {

	}

}

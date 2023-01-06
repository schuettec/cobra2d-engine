package com.github.schuettec.cobra2d.network.client;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.network.NetworkActor;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.network.common.command.server.ServerCommand;
import com.github.schuettec.cobra2d.network.common.command.server.UpdateControllerCommand;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;
import com.github.schuettec.cobra2d.world.Collision;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class ClientCamera extends BasicRectangleMapCamera implements NetworkActor, Updatable {

	public Set<Integer> keyCodesToListen = new HashSet<>();

	protected List<ServerCommand> serverCommands;

	public ClientCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled, Point screenPosition) {
		super(worldCoordinates, dimension, playerControlled, screenPosition);
		this.serverCommands = new LinkedList<>();
	}

	public ClientCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled) {
		super(worldCoordinates, dimension, playerControlled);
		this.serverCommands = new LinkedList<>();
	}

	public void addKeyCodeToListen(int keyCode) {
		keyCodesToListen.add(keyCode);
	}

	@Override
	public List<ServerCommand> getRemotePlayerCommands() {
		return serverCommands;
	}

	@Override
	public void render(RendererAccess renderer, Cobra2DWorld map, List<Collision> capturedEntities) {
		centerOnScreen(renderer);
		capturedEntities = sortByLayer(capturedEntities);
		for (Collision collision : capturedEntities) {
			Entity entity = collision.getOpponent();
			Point worldToScreenTranslation = getWorldToScreenTranslation();
			// Render entity
			if (entity instanceof Renderable) {
				Renderable renderable = (Renderable) entity;
				renderable.render(renderer, worldToScreenTranslation);
			}
		}
		drawCameraOutline(renderer);
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime, Controller controller) {
		updateServerCommands(this.serverCommands, worldAccess, deltaTime, controller);
	}

	protected void updateServerCommands(List<ServerCommand> serverCommands, WorldAccess worldAccess, float deltaTime,
	    Controller controller) {
		serverCommands.clear();
		keyCodesToListen.stream()
		    .filter(controller::isKeyPressed)
		    .map(UpdateControllerCommand::ofKeyCode)
		    .forEach(serverCommands::add);

	}

}

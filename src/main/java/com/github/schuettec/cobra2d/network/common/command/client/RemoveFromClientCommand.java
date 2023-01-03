package com.github.schuettec.cobra2d.network.common.command.client;

import com.github.schuettec.cobra2d.network.client.ClientAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class RemoveFromClientCommand implements ClientCommand {

	private String entityId;

	public RemoveFromClientCommand(String entityId) {
		super();
		this.entityId = entityId;
	}

	@Override
	public void perform(WorldAccess worldAccess, ClientAccess clientAccess) {
		worldAccess.getEntityById(entityId)
		    .ifPresent(e -> {
			    worldAccess.removeEntity(e);
		    });
	}

	public String getEntityId() {
		return entityId;
	}

	@Override
	public String toString() {
		return "RemoveFromClientCommand [entityId=" + entityId + "]";
	}

}

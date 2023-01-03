package com.github.schuettec.cobra2d.network.common.command.client;

import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.network.client.ClientAccess;
import com.github.schuettec.cobra2d.network.data.EntityState;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class UpdateClientCommand implements ClientCommand {

	private String entityClass;
	private EntityState entityState;

	public UpdateClientCommand(String entityClass, EntityState entityState) {
		super();
		this.entityState = entityState;
		this.entityClass = entityClass;
	}

	@Override
	public void perform(WorldAccess worldAccess, ClientAccess clientAccess) {
		String entityId = entityState.getId();
		worldAccess.getEntityById(entityId)
		    .ifPresentOrElse(e -> {
			    clientAccess.writeEntityState(entityState, e);
		    }, () -> {
			    Entity entity = clientAccess.createEntity(entityClass);
			    clientAccess.writeEntityState(entityState, entity);
			    worldAccess.entityAdded(entity);
		    });
	}

	public String getEntityClass() {
		return entityClass;
	}

	public EntityState getEntityState() {
		return entityState;
	}

	public void setEntityState(EntityState entityState) {
		this.entityState = entityState;
	}

	public String getEntityId() {
		return entityState.getId();
	}

	@Override
	public String toString() {
		return "UpdateCommand [entityState=" + entityState + "]";
	}

}

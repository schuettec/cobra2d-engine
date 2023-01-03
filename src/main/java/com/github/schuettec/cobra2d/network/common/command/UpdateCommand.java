package com.github.schuettec.cobra2d.network.common.command;

import com.github.schuettec.cobra2d.network.data.EntityState;

public class UpdateCommand {

	private long entityId;
	private EntityState entityState;

	public UpdateCommand() {
		super();
	}

	public UpdateCommand(long entityId, EntityState entityState) {
		super();
		this.entityId = entityId;
		this.entityState = entityState;
	}

	public EntityState getEntityState() {
		return entityState;
	}

	public void setEntityState(EntityState entityState) {
		this.entityState = entityState;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	@Override
	public String toString() {
		return "UpdateCommand [entityId=" + entityId + ", entityState=" + entityState + "]";
	}

}

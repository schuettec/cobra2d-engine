package com.github.schuettec.cobra2d.network.client;

import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.network.data.EntityState;

/**
 * Interface providing method the server can call.
 */
public interface ClientAccess {

	void writeEntityState(EntityState entityState, Entity entity);

	Entity createEntity(String entityClass);

}

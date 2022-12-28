package com.github.schuettec.cobra2d.world;

import com.github.schuettec.cobra2d.entity.skills.Entity;

public interface WorldListener {

	public void entityAdded(final Entity entity);

	public void entityRemoved(final Entity entity);

	public void beforeUpdate();

	public void afterUpdate();

}

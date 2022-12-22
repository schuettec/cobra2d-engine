package com.github.schuettec.cobra2d.map;

import com.github.schuettec.cobra2d.entity.skills.Entity;

public interface MapListener {

	public void entityAdded(final Entity entity);

	public void entityRemoved(final Entity entity);

	public void beforeUpdate();

	public void afterUpdate();

}

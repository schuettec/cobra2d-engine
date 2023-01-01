package com.github.schuettec.cobra2d.world;

import com.github.schuettec.cobra2d.entity.skills.Entity;

public interface WorldListener {

	default void entityAdded(final Entity entity) {
	}

	default void entityRemoved(final Entity entity) {
	}
}

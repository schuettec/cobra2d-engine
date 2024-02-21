package com.github.schuettec.cobra2d.world;

import com.github.schuettec.cobra2d.entity.skills.Skill;

public interface WorldListener {

	default void entityAdded(final Skill entity) {
	}

	default void entityRemoved(final Skill entity) {
	}

	default void beforeUpdate() {

	}

	default void afterUpdate() {

	}
}

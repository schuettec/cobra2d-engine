package com.github.schuettec.cobra2d.entity.skills;

import java.io.Serializable;
import java.util.Optional;

/**
 * 
 * Base interface for skill interfaces.
 * 
 * @author schuettec
 *
 */
public interface Skill extends Serializable {

	/**
	 * Checks if the specified entity has this Skill.
	 * 
	 * @param entity
	 *        The entity to check.
	 * @return Returns <code>true</code> if the specified {@link Entity} has this
	 *         skill.
	 */
	public static <S extends Skill> boolean hasSkill(Class<S> skill, Entity entity) {
		return skill.isInstance(entity);
	}

	/**
	 * Checks if the specified entity has this Skill.
	 * 
	 * @param entity
	 *        The entity to check.
	 * @return Returns <code>true</code> if the specified {@link Entity} has this
	 *         skill.
	 */
	public static <S extends Skill> Optional<S> asSkill(Class<S> skill, Entity entity) {
		if (hasSkill(skill, entity)) {
			return Optional.of(skill.cast(entity));
		} else {
			return Optional.empty();
		}
	}
}

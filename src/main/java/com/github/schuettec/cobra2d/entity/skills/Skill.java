package com.github.schuettec.cobra2d.entity.skills;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * Base interface for skill interfaces.
 *
 * @author schuettec
 *
 */
public interface Skill extends Serializable {

	/**
	 * @return Returns the id of this entity.
	 */
	public String getId();

	/**
	 * Sets an id.
	 *
	 * @param id The id.
	 */
	public void setId(String id);

	/**
	 * Creates a new id.
	 *
	 * @return Returns a new id;
	 */
	public default String createId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Checks if the specified entity has this Skill.
	 *
	 * @param entity The entity to check.
	 * @return Returns <code>true</code> if the specified {@link Entity} has this
	 *         skill.
	 */
	public static <S extends Skill> boolean hasSkill(Class<S> skill, Skill entity) {
		return skill.isInstance(entity);
	}

	/**
	 * Checks if the specified entity has this Skill.
	 *
	 * @param entity The entity to check.
	 * @return Returns <code>true</code> if the specified {@link Entity} has this
	 *         skill.
	 */
	public static <S extends Skill> Optional<S> asSkill(Class<S> skill, Skill entity) {
		if (hasSkill(skill, entity)) {
			return Optional.of(skill.cast(entity));
		} else {
			return Optional.empty();
		}
	}
}

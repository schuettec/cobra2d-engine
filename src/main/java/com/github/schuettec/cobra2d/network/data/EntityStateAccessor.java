package com.github.schuettec.cobra2d.network.data;

import java.io.Serializable;
import java.util.List;

import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.network.external.reflection.ReflectiveField;

/**
 * A stateful class that holds the mechanics of reading the entity state. Note: The entity state is read/written very
 * often during network gameplay, so this class must work very efficient.
 */
public class EntityStateAccessor {

	private List<ReflectiveField> fields;

	public EntityStateAccessor(List<ReflectiveField> fields) {
		super();
		this.fields = fields;
	}

	public <E extends Entity> EntityState readEntityState(E entity) {
		EntityState state = new EntityState();
		for (ReflectiveField f : fields) {
			state.addAttributeValue(f.hashCode(), f.getFieldValue(entity));
		}
		return state;
	}

	public <E extends Entity> void writeEntityState(EntityState state, E entity) {
		for (ReflectiveField f : fields) {
			Serializable value = state.getAttributeValue(f.hashCode());
			f.setFieldValue(entity, value);
		}
	}

}

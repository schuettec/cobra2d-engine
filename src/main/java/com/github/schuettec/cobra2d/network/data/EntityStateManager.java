package com.github.schuettec.cobra2d.network.data;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.Skill;
import com.github.schuettec.cobra2d.entity.skills.state.EntityStateValue;
import com.github.schuettec.cobra2d.network.external.reflection.ReflectionProvider;
import com.github.schuettec.cobra2d.network.external.reflection.ReflectiveField;
import com.github.schuettec.cobra2d.network.external.reflection.javase.JavaReflectionProvider;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;
import com.github.schuettec.cobra2d.world.WorldListener;

/**
 * Class that manages reading and writing of the entity state values from and to
 * an entity.
 */
public class EntityStateManager implements WorldListener {

	private static final ReflectionProvider reflectionProvider = new JavaReflectionProvider();

	private Map<Class<? extends Skill>, EntityStateAccessor> accessorsByEntityType;

	private Map<String, Class<? extends Skill>> registeredTypes;

	public EntityStateManager(Cobra2DWorld world) {
		this.accessorsByEntityType = new Hashtable<>();
		this.registeredTypes = new Hashtable<>();
		registerEntities(world);
		world.addWorldListener(this);
	}

	private void registerEntities(Cobra2DWorld world) {
		/*
		 * Register all entities that have an attribute marked as entity state, because
		 * all entities with a marked state can be transferred during network play, even
		 * if they are not updated repeatedly.
		 */
		world.getAllEntities().stream().forEach(e -> {
			registerEntity(entityType(e));
		});
	}

	@Override
	public void entityAdded(Skill entity) {
		registerEntity(entityType(entity));
	}

	public <S extends Skill> void registerEntity(Class<S> entityType) {
		List<ReflectiveField> allInheritedFields = reflectionProvider
				.getAllInheritedFieldsAnnotatedWith(EntityStateValue.class, entityType);
		if (!allInheritedFields.isEmpty()) {
			EntityStateAccessor entityStateAccessor = new EntityStateAccessor(allInheritedFields);
			accessorsByEntityType.put(entityType, entityStateAccessor);
			registeredTypes.put(entityType.getName(), entityType);
		}
	}

	public boolean isRegistered(String entityClass) {
		return registeredTypes.containsKey(entityClass);
	}

	public <E extends Entity> boolean isRegistered(E entity) {
		return registeredTypes.containsKey(entityType(entity).getName());
	}

	public <S extends Skill> EntityState readEntityState(S entity) {
		Class<? extends Skill> entityType = entityType(entity);
		EntityStateAccessor entityAccessor = entityAccessor(entityType);
		return entityAccessor.readEntityState(entity);
	}

	public <S extends Skill> void writeEntityState(EntityState state, S entity) {
		Class<? extends Skill> entityType = entityType(entity);
		EntityStateAccessor entityAccessor = entityAccessor(entityType);
		entityAccessor.writeEntityState(state, entity);
	}

	private EntityStateAccessor entityAccessor(Class<? extends Skill> entityType) {
		if (accessorsByEntityType.containsKey(entityType)) {
			return accessorsByEntityType.get(entityType);
		} else {
			throw new RuntimeException("Attempt to read state of entity with an unknown type: " + entityType.getName());
		}
	}

	private <S extends Skill> Class<? extends Skill> entityType(S entity) {
		return entity.getClass();
	}

	public Class<? extends Skill> getEntityClass(String entityClass) {
		return registeredTypes.get(entityClass);
	}
}

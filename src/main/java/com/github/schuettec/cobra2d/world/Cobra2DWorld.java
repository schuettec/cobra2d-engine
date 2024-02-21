package com.github.schuettec.cobra2d.world;

import static com.github.schuettec.cobra2d.entity.skills.Skill.asSkill;
import static java.util.Objects.isNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Skill;
import com.github.schuettec.cobra2d.entity.skills.SoundEffect;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.network.NetworkActor;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicJoint;
import com.github.schuettec.cobra2d.entity.skills.sound.SoundCamera;

/**
 * This is the map data structure. This class makes all {@link Entity} objects
 * accessible that are currently existing in the world. The {@link Cobra2DWorld}
 * also manages all the collisions for the current frame.
 *
 *
 * <p>
 * <b>Note: The map is intended to be used in the rendering thread or at least
 * in one thread managing all the entities calls. Therefore this
 * {@link Cobra2DWorld} is not thread safe.</b>
 * </p>
 *
 * @author Chris
 *
 */
public class Cobra2DWorld {

	private static final float TIME_STEP = 1 / 165f;

	private static final int VELOCITY_ITERATIONS = 6;

	private static final int POSITION_ITERATIONS = 2;

	protected Map<String, Skill> allEntities;

	protected Set<Obstacle> obstacles;
	protected Set<Controllable> controllable;
	protected Set<Updatable> updateables;
	protected Set<Renderable> renderables;
	protected Set<Camera> cameras;
	protected Set<PhysicBody> physicBodies;
	protected Set<PhysicJoint> physicJoints;
	protected Set<SoundEffect> soundEffects;

	protected Set<NetworkActor> networkActors;

	private transient List<WorldListener> listeners;

	/**
	 * Holds the listeners that are notified if an entity with the specified skill
	 * was added/removed.
	 */
	protected Map<Class<? extends Skill>, WorldListener> listenersBySkills = new Hashtable<>();

	private Map<Camera, List<Collision>> cameraCollisionMap;
	private Map<SoundCamera, List<Collision>> soundCollisionMap;

	private boolean calculateFullCameraCollisionPoints = false;

	private World physicsWorld;
	private float accumulator = 0;

	private Cobra2DEngine engine;

	private boolean updateWorld;

	private Collisions collisions;

	private WorldAccess worldAccess;

	public Cobra2DWorld(Cobra2DEngine engine, boolean updateWorld) {
		this.engine = engine;
		this.updateWorld = updateWorld;

		if (isUpdateWorld()) {
			// Create a default physics world with zero gravity
			this.physicsWorld = new World(new Vector2(), true);
		}

		this.allEntities = new Hashtable<String, Skill>();
		this.obstacles = new HashSet<>();
		this.updateables = new HashSet<>();
		this.controllable = new HashSet<>();
		this.renderables = new HashSet<>();
		this.physicBodies = new HashSet<>();
		this.physicJoints = new HashSet<>();
		this.soundEffects = new HashSet<>();
		this.networkActors = new HashSet<>();
		this.cameras = new HashSet<>();
		this.cameraCollisionMap = new Hashtable<>();
		this.soundCollisionMap = new Hashtable<>();
		this.listeners = new LinkedList<>();
		this.worldAccess = new WorldAccess(this);
		this.collisions = new Collisions();

		if (isUpdateWorld()) {
			this.listenersBySkills.put(PhysicBody.class, new WorldListener() {
				@Override
				public void entityAdded(Skill entity) {
					PhysicBody physicBody = (PhysicBody) entity;
					BodyDef bodyDef = physicBody.createBodyDef();
					Body body = physicsWorld.createBody(bodyDef);
					physicBody.createFixture(body);
				}

				@Override
				public void entityRemoved(Skill entity) {
					PhysicBody physicBody = (PhysicBody) entity;
					Body body = physicBody.getBody();
					physicsWorld.destroyBody(body);
				}

			});
			this.listenersBySkills.put(PhysicJoint.class, new WorldListener() {
				@Override
				public void entityAdded(Skill entity) {
					PhysicJoint physicJoint = (PhysicJoint) entity;
					JointDef jointDef = physicJoint.createJointDef();
					Joint joint = physicsWorld.createJoint(jointDef);
					physicJoint.setJoint(joint);
				}

				@Override
				public void entityRemoved(Skill entity) {
					PhysicJoint physicJoint = (PhysicJoint) entity;
					Joint joint = physicJoint.getJoint();
					physicsWorld.destroyJoint(joint);
				}

			});
		}

		this.listenersBySkills.put(Renderable.class, engine.getRenderer());
	}

	public Optional<Skill> getEntityById(String entityId) {
		return Optional.ofNullable(allEntities.get(entityId));
	}

	public void addWorldListener(final WorldListener listener) {
		this.listeners.add(listener);
	}

	public void removeWorldListener(final WorldListener listener) {
		this.listeners.remove(listener);
	}

	public void setGravity(float xForce, float yForce) {
		this.physicsWorld.setGravity(new Vector2(xForce, yForce));
	}

	public void addEntity(Skill... entities) {
		for (Skill e : entities) {
			addEntity(e);
		}
	}

	public void addEntity(Skill entity) {
		this.allEntities.put(entity.getId(), entity);
		addEntityBySkill(entity);
	}

	private void addEntityBySkill(Skill entity) {
		addOnDemand(Camera.class, this.cameras, entity);
		addOnDemand(Obstacle.class, this.obstacles, entity);
		addOnDemand(Controllable.class, this.controllable, entity);
		addOnDemand(Updatable.class, this.updateables, entity);
		addOnDemand(Renderable.class, this.renderables, entity);
		addOnDemand(PhysicBody.class, this.physicBodies, entity);
		addOnDemand(PhysicJoint.class, this.physicJoints, entity);
		addOnDemand(SoundEffect.class, this.soundEffects, entity);
		addOnDemand(NetworkActor.class, this.networkActors, entity);
	}

	public void removeEntity(Skill... entities) {
		for (Skill e : entities) {
			removeEntity(e);
		}
	}

	public void removeEntity(Skill entity) {
		this.allEntities.remove(entity);
		removeEntityBySkill(entity);
	}

	private <S extends Skill> void removeEntityBySkill(S entity) {
		removeOnDemand(Obstacle.class, this.obstacles, entity);
		removeOnDemand(Controllable.class, this.controllable, entity);
		removeOnDemand(Updatable.class, this.updateables, entity);
		removeOnDemand(Renderable.class, this.renderables, entity);
		removeOnDemand(Camera.class, this.cameras, entity);
		removeOnDemand(PhysicBody.class, this.physicBodies, entity);
		removeOnDemand(PhysicJoint.class, this.physicJoints, entity);
		removeOnDemand(SoundEffect.class, this.soundEffects, entity);
		removeOnDemand(NetworkActor.class, this.networkActors, entity);
	}

	private <S extends Skill> void addOnDemand(Class<S> skillType, Set<S> obstacles, Skill entity) {
		Optional<S> asSkill = asSkill(skillType, entity);
		if (asSkill.isPresent()) {
			obstacles.add(asSkill.get());
			notifyAddBySkill(skillType, entity);
		}
		notifyAdded(entity);
	}

	private Optional<WorldListener> skillWorldlistener(Class<? extends Skill> skillType) {
		return Optional.ofNullable(listenersBySkills.get(skillType));
	}

	private <S extends Skill> void removeOnDemand(Class<S> skillType, Set<S> obstacles, Skill entity) {
		Optional<S> asSkill = asSkill(skillType, entity);
		if (asSkill.isPresent()) {
			obstacles.remove(asSkill.get());
			notifyRemovedBySkill(skillType, entity);
		}
		notifyRemoved(entity);
	}

	public void update(float requestedFps, float deltaTime) {
		notifyBeforeUpdate();

		updateControllables();

		if (isUpdateWorld()) {
			updateWorld(deltaTime);
		}

		updateCameras(deltaTime);

		notifyAfterUpdate();
	}

	private void updateControllables() {
		for (Controllable c : controllable) {
			Controller controller = engine.getRenderer().getControllerForEntity(c);
			c.processControllerState(worldAccess, controller);
		}
	}

	private void updateCameras(float deltaTime) {
		cameraCollisionMap.clear();
		soundCollisionMap.clear();
		// Detect all collisions in the set of renderables with cameras
		for (Camera camera : cameras) {
			// // If the world update is disabled, perform a dedicated camera update.
			// // Otherwise the cameras will show nothing if not updated.
			if (!isUpdateWorld()) {
				updateEntity(deltaTime, camera);
			}
			List<Collision> cameraCollisions = getCameraCollisions(camera);
			cameraCollisionMap.put(camera, cameraCollisions);
			if (camera instanceof SoundCamera) {
				List<Collision> soundCollisions = getSoundCollisions(camera);
				soundCollisionMap.put((SoundCamera) camera, soundCollisions);
			}
		}
	}

	private List<Collision> getSoundCollisions(Camera soundCamera) {
		CollisionMap soundCollisions = getCollisions().detectCollision((SoundCamera) soundCamera,
				SoundCamera::getSoundRangeInWorlCoordinates, soundEffects, SoundEffect::getSoundRangeInWorldCoordinates,
				false, false, false);
		List<Collision> cameraCollisions = soundCollisions.getCollisions();
		return cameraCollisions;
	}

	private List<Collision> getCameraCollisions(Camera camera) {
		CollisionMap map = getCollisions().detectCollision(camera, Camera::getCollisionShapeInWorldCoordinates,
				renderables, Renderable::getCollisionShapeInWorldCoordinates, false, calculateFullCameraCollisionPoints,
				false);
		List<Collision> cameraCollisions = map.getCollisions();
		return cameraCollisions;
	}

	private void updateWorld(float deltaTime) {
		doPhysicsStep(deltaTime);
		for (Updatable updatable : updateables) {
			updateEntity(deltaTime, updatable);
		}
	}

	private void updateEntity(float deltaTime, Updatable updatable) {
		updatable.update(worldAccess, deltaTime);
	}

	private void doPhysicsStep(float deltaTime) {
		// fixed time step
		// max frame time to avoid spiral of death (on slow devices)
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= TIME_STEP) {
			physicsWorld.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			accumulator -= TIME_STEP;
		}
	}

	public List<Collision> getCameraCollision(Camera camera) {
		List<Collision> result = cameraCollisionMap.get(camera);
		if (isNull(result)) {
			return Collections.emptyList();
		} else {
			return result;
		}
	}

	public List<SoundEffect> getSoundCollision(Camera camera) {
		List<Collision> result = soundCollisionMap.get(camera);
		if (isNull(result)) {
			return Collections.emptyList();
		} else {
			return result.stream().map(Collision::getOpponent).map(e -> (SoundEffect) e).collect(Collectors.toList());
		}
	}

	public Set<Skill> getAllEntities() {
		return new HashSet<>(allEntities.values());
	}

	public Set<Obstacle> getObstacles() {
		return new HashSet<>(obstacles);
	}

	public Set<Updatable> getUpdateables() {
		return new HashSet<>(updateables);
	}

	public Set<NetworkActor> getNetworkActors() {
		return new HashSet<>(networkActors);
	}

	public Set<Renderable> getRenderables() {
		return new HashSet<>(renderables);
	}

	public Set<Camera> getCameras() {
		return new HashSet<>(cameras);
	}

	public boolean isCalculateFullCameraCollisionPoints() {
		return calculateFullCameraCollisionPoints;
	}

	public void setCalculateFullCameraCollisionPoints(boolean calculateFullCameraCollisionPoints) {
		this.calculateFullCameraCollisionPoints = calculateFullCameraCollisionPoints;
	}

	/**
	 * @param enties T The entities to remove in the result.
	 * @return Returns the obstacles except the specified entities.
	 */
	public Set<? extends HasCollisionShape> getObstaclesExcept(Entity... enties) {
		Set<? extends HasCollisionShape> clone = new HashSet<>(this.obstacles);
		clone.removeAll(Set.of(enties));
		return clone;
	}

	public boolean isUpdateWorld() {
		return updateWorld;
	}

	private <S extends Skill> void notifyAddBySkill(Class<S> skillType, Skill entity) {
		skillWorldlistener(skillType).ifPresent(l -> l.entityAdded(entity));

	}

	private void notifyAdded(Skill entity) {
		listeners.stream().forEach(l -> l.entityAdded(entity));
	}

	private <S extends Skill> void notifyRemovedBySkill(Class<S> skillType, Skill entity) {
		skillWorldlistener(skillType).ifPresent(l -> l.entityRemoved(entity));
	}

	private void notifyRemoved(Skill entity) {
		listeners.stream().forEach(l -> l.entityRemoved(entity));
	}

	private void notifyBeforeUpdate() {
		listeners.stream().forEach(l -> l.beforeUpdate());
	}

	private void notifyAfterUpdate() {
		listeners.stream().forEach(l -> l.afterUpdate());
	}

	public void addEntities(Collection<Skill> toAdd) {
		toAdd.forEach(this::addEntity);
	}

	public void removeEntities(Collection<Skill> toRemove) {
		toRemove.forEach(this::removeEntity);
	}

	public WorldAccess getWorldAccess() {
		return worldAccess;
	}

	public Set<PhysicBody> getPhysicBodies() {
		return physicBodies;
	}

	public Set<PhysicJoint> getPhysicJoints() {
		return physicJoints;
	}

	public Set<SoundEffect> getSoundEffects() {
		return soundEffects;
	}

	public Collisions getCollisions() {
		return collisions;
	}

}

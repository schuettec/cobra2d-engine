package com.github.schuettec.cobra2d.world;

import static com.github.schuettec.cobra2d.entity.skills.Skill.asSkill;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.entity.camera.InputContext;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Skill;
import com.github.schuettec.cobra2d.entity.skills.SoundEffect;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.network.NetworkActor;
import com.github.schuettec.cobra2d.entity.skills.physics.PhysicBody;
import com.github.schuettec.cobra2d.entity.skills.sound.SoundCamera;
import com.github.schuettec.cobra2d.math.Point;

/**
 * This is the map data structure. This class makes all {@link Entity} objects
 * accessible that are currently existing in the world. The {@link Cobra2DWorld} also
 * manages all the collisions for the current frame.
 *
 *
 * <p>
 * <b>Note: The map is intended to be used in the rendering thread or at least
 * in one thread managing all the entities calls. Therefore this {@link Cobra2DWorld} is
 * not thread safe.</b>
 * </p>
 *
 * @author Chris
 *
 */
public class Cobra2DWorld {

	private static final float TIME_STEP = 1 / 165f;

	private static final int VELOCITY_ITERATIONS = 6;

	private static final int POSITION_ITERATIONS = 2;

	protected Map<String, Entity> allEntities;

	protected Set<Obstacle> obstacles;
	protected Set<Updatable> updateables;
	protected Set<Renderable> renderables;
	protected Set<Camera> cameras;
	protected Set<PhysicBody> physicBodies;
	protected Set<SoundEffect> soundEffects;

	protected Set<NetworkActor> networkActors;

	private transient List<WorldListener> listeners;

	/**
	 * Holds the listeners that are notified if an entity with the specified skill was added/removed.
	 */
	protected Map<Class<? extends Skill>, WorldListener> listenersBySkills = new Hashtable<>();

	private Map<Camera, List<Collision>> cameraCollisionMap;
	private Map<SoundCamera, List<Collision>> soundCollisionMap;

	private boolean calculateFullCameraCollisionPoints = false;

	private Camera cameraForInput;

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

		this.allEntities = new Hashtable<String, Entity>();
		this.obstacles = new HashSet<>();
		this.updateables = new HashSet<>();
		this.renderables = new HashSet<>();
		this.physicBodies = new HashSet<>();
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
				public void entityAdded(Entity entity) {
					PhysicBody physicBody = (PhysicBody) entity;
					BodyDef bodyDef = physicBody.createBodyDef();
					Body body = physicsWorld.createBody(bodyDef);
					physicBody.createFixture(body);

					System.out.println("Body count : " + physicsWorld.getBodyCount());

				}

				@Override
				public void entityRemoved(Entity entity) {
					PhysicBody physicBody = (PhysicBody) entity;
					Body body = physicBody.getBody();
					physicsWorld.destroyBody(body);
				}

			});
		}

		this.listenersBySkills.put(Renderable.class, engine.getRenderer());
	}

	public Optional<Entity> getEntityById(String entityId) {
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

	public void addEntity(Entity... entities) {
		for (Entity e : entities) {
			addEntity(e);
		}
	}

	public void addEntity(Entity entity) {
		this.allEntities.put(entity.getId(), entity);
		addEntityBySkill(entity);
	}

	private void addEntityBySkill(Entity entity) {
		addOnDemand(Camera.class, this.cameras, entity);
		addOnDemand(Obstacle.class, this.obstacles, entity);
		addOnDemand(Updatable.class, this.updateables, entity);
		addOnDemand(Renderable.class, this.renderables, entity);
		addOnDemand(PhysicBody.class, this.physicBodies, entity);
		addOnDemand(SoundEffect.class, this.soundEffects, entity);
		addOnDemand(NetworkActor.class, this.networkActors, entity);
	}

	public void removeEntity(Entity... entities) {
		for (Entity e : entities) {
			removeEntity(e);
		}
	}

	public void removeEntity(Entity entity) {
		this.allEntities.remove(entity);
		removeEntityBySkill(entity);
	}

	private void removeEntityBySkill(Entity entity) {
		removeOnDemand(Obstacle.class, this.obstacles, entity);
		removeOnDemand(Updatable.class, this.updateables, entity);
		removeOnDemand(Renderable.class, this.renderables, entity);
		removeOnDemand(Camera.class, this.cameras, entity);
		removeOnDemand(PhysicBody.class, this.physicBodies, entity);
		removeOnDemand(SoundEffect.class, this.soundEffects, entity);
		removeOnDemand(NetworkActor.class, this.networkActors, entity);
	}

	private <S extends Skill> void addOnDemand(Class<S> skillType, Set<S> obstacles, Entity entity) {
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

	private <S extends Skill> void removeOnDemand(Class<S> skillType, Set<S> obstacles, Entity entity) {
		Optional<S> asSkill = asSkill(skillType, entity);
		if (asSkill.isPresent()) {
			obstacles.remove(asSkill.get());
			notifyRemovedBySkill(skillType, entity);
		}
		notifyRemoved(entity);
	}

	public void update(float requestedFps, float deltaTime) {
		notifyBeforeUpdate();

		calculateCameraRelativeInput();

		if (isUpdateWorld()) {
			updateWorld(deltaTime);
		}

		updateCameras(deltaTime);

		notifyAfterUpdate();
	}

	private void calculateCameraRelativeInput() {
		Camera cameraForInput = this.getCameraForInput();
		if (nonNull(cameraForInput)) {
			Controller controller = engine.getRenderer()
			    .getControllerForEntity(cameraForInput);
			InputContext input = getCameraRelativeInput(controller, cameraForInput);
			controller.setCameraRelativeInput(input);
		}
	}

	private void updateCameras(float deltaTime) {
		cameraCollisionMap.clear();
		soundCollisionMap.clear();
		// Detect all collisions in the set of renderables with cameras
		for (Camera camera : cameras) {
			// If the world update is disabled, perform a dedicated camera update.
			// Otherwise the cameras will show nothing if not updated.
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
		    SoundCamera::getSoundRange, soundEffects, SoundEffect::getSoundRange, false, false, false);
		List<Collision> cameraCollisions = soundCollisions.getCollisions();
		return cameraCollisions;
	}

	private List<Collision> getCameraCollisions(Camera camera) {
		CollisionMap map = getCollisions().detectCollision(camera, Camera::getCollisionShapeInWorldCoordinates, renderables,
		    Renderable::getCollisionShapeInWorldCoordinates, false, calculateFullCameraCollisionPoints, false);
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
		Controller controller = engine.getRenderer()
		    .getControllerForEntity(updatable);
		updatable.update(worldAccess, deltaTime, controller);
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

	private InputContext getCameraRelativeInput(Controller controller, Camera cameraForInput) {
		Point mousePositionOnScreen = controller.getMousePositionOnScreen();
		if (nonNull(cameraForInput)) {
			return new InputContext(cameraForInput.screenToWorldCoordinates(mousePositionOnScreen));
		} else {
			return new InputContext(mousePositionOnScreen);
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
			// TODO: FIX THAT after getting rid of dependency from Collisions to HasCollisionShape
			// return result.stream()
			// .map(Collision::getOpponent)
			// .collect(Collectors.toList());
			return null;
		}
	}

	public Set<Entity> getAllEntities() {
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

	public Camera getCameraForInput() {
		return this.cameraForInput;
	}

	public void setCameraForInput(Camera camera) {
		this.cameraForInput = camera;
	}

	public boolean isUpdateWorld() {
		return updateWorld;
	}

	private <S extends Skill> void notifyAddBySkill(Class<S> skillType, Entity entity) {
		skillWorldlistener(skillType).ifPresent(l -> l.entityAdded(entity));

	}

	private void notifyAdded(Entity entity) {
		listeners.stream()
		    .forEach(l -> l.entityAdded(entity));
	}

	private <S extends Skill> void notifyRemovedBySkill(Class<S> skillType, Entity entity) {
		skillWorldlistener(skillType).ifPresent(l -> l.entityRemoved(entity));
	}

	private void notifyRemoved(Entity entity) {
		listeners.stream()
		    .forEach(l -> l.entityRemoved(entity));
	}

	private void notifyBeforeUpdate() {
		listeners.stream()
		    .forEach(l -> l.beforeUpdate());
	}

	private void notifyAfterUpdate() {
		listeners.stream()
		    .forEach(l -> l.afterUpdate());
	}

	public void addEntities(Collection<Entity> toAdd) {
		toAdd.forEach(this::addEntity);
	}

	public void removeEntities(Collection<Entity> toRemove) {
		toRemove.forEach(this::removeEntity);
	}

	public WorldAccess getWorldAccess() {
		return worldAccess;
	}

	public Set<PhysicBody> getPhysicBodies() {
		return physicBodies;
	}

	public Set<SoundEffect> getSoundEffects() {
		return soundEffects;
	}

	public Collisions getCollisions() {
		return collisions;
	}

}

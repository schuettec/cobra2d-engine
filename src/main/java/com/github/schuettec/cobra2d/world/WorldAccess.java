package com.github.schuettec.cobra2d.world;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Skill;
import com.github.schuettec.cobra2d.entity.skills.Updatable;

/**
 * Access layer to add/remove entities from the world while renderer and world
 * updater are running. This layer is accessed by entities or other game
 * managing objects to safely add/remove entities to the game world.
 */
public class WorldAccess implements WorldListener {

	private Cobra2DWorld world;

	private Deque<Skill> toAdd = new ConcurrentLinkedDeque<>();
	private Deque<Skill> toRemove = new ConcurrentLinkedDeque<>();

	public WorldAccess(Cobra2DWorld world) {
		super();
		this.world = world;
		this.world.addWorldListener(this);
	}

	public Optional<Skill> getEntityById(String entityId) {
		return world.getEntityById(entityId);
	}

	public void spawnEntity(Skill entity) {
		toAdd.add(entity);
	}

	public void removeEntity(Skill entity) {
		toRemove.add(entity);
	}

	@Override
	public void afterUpdate() {
		WorldListener.super.afterUpdate();
		world.removeEntities(toRemove);
		toRemove.clear();
		world.addEntities(toAdd);
		toAdd.clear();
	}

	public Set<Obstacle> getObstacles() {
		return world.getObstacles();
	}

	public Set<? extends HasCollisionShape> getObstaclesExcept(Entity... enties) {
		return world.getObstaclesExcept(enties);
	}

	public List<Collision> getCameraCollision(Camera camera) {
		return world.getCameraCollision(camera);
	}

	public Set<Skill> getAllEntities() {
		return world.getAllEntities();
	}

	public Set<Updatable> getUpdateables() {
		return world.getUpdateables();
	}

	public Set<Renderable> getRenderables() {
		return world.getRenderables();
	}

	public Set<Camera> getCameras() {
		return world.getCameras();
	}

	public boolean isUpdateWorld() {
		return world.isUpdateWorld();
	}

	public WorldAccess getWorldAccess() {
		return world.getWorldAccess();
	}

	public Collisions getCollisions() {
		return world.getCollisions();
	}

}

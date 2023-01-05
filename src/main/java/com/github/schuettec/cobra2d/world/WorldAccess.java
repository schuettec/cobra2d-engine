package com.github.schuettec.cobra2d.world;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.CollisionDetail;
import com.github.schuettec.cobra2d.entity.CollisionMap;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Shape;

/**
 * Access layer to add/remove entities from the world while renderer and world updater are running. This layer is
 * accessed by entities or other game managing objects to safely add/remove entities to the game world.
 */
public class WorldAccess implements WorldListener {

	private Cobra2DWorld world;

	private Deque<Entity> toAdd = new ConcurrentLinkedDeque<>();
	private Deque<Entity> toRemove = new ConcurrentLinkedDeque<>();

	public WorldAccess(Cobra2DWorld world) {
		super();
		this.world = world;
		this.world.addWorldListener(this);
	}

	public Optional<Entity> getEntityById(String entityId) {
		return world.getEntityById(entityId);
	}

	public void spawnEntity(Entity entity) {
		toAdd.add(entity);
	}

	public void removeEntity(Entity entity) {
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

	public CollisionMap detectCollision(HasCollisionShape firstEntity, Set<? extends HasCollisionShape> secondSet,
	    boolean outlineOnly, boolean allEntityPoints, boolean addBidirectional) {
		return world.detectCollision(firstEntity, secondSet, outlineOnly, allEntityPoints, addBidirectional);
	}

	public Set<Obstacle> getObstacles() {
		return world.getObstacles();
	}

	public Set<? extends HasCollisionShape> getObstaclesExcept(Entity... enties) {
		return world.getObstaclesExcept(enties);
	}

	public CollisionMap detectCollision(Set<? extends HasCollisionShape> firstSet,
	    Set<? extends HasCollisionShape> secondSet, boolean outlineOnly, boolean allEntityPoints,
	    boolean addBidirectional) {
		return world.detectCollision(firstSet, secondSet, outlineOnly, allEntityPoints, addBidirectional);
	}

	public CollisionMap detectCollision(Set<? extends HasCollisionShape> map, boolean outlineOnly,
	    boolean allEntityPoints, boolean addBidirectional) {
		return world.detectCollision(map, outlineOnly, allEntityPoints, addBidirectional);
	}

	public List<CollisionDetail> detectFirstCollision(Shape shape, Set<? extends HasCollisionShape> map,
	    boolean outlineOnly, boolean all) {
		return world.detectFirstCollision(shape, map, outlineOnly, all);
	}

	public Collision detectCollision(HasCollisionShape e1, HasCollisionShape e2, boolean outlineOnly, boolean all) {
		return world.detectCollision(e1, e2, outlineOnly, all);
	}

	public List<CollisionDetail> detectCollision(Shape s1, Shape s2, boolean outlineOnly, boolean all) {
		return world.detectCollision(s1, s2, outlineOnly, all);
	}

	public CollisionMap detectCollision(Shape shape, Set<? extends HasCollisionShape> obstaclesExcept,
	    boolean outlineOnly, boolean allEntityPoints, boolean addBidirectional) {
		return world.detectCollision(shape, obstaclesExcept, outlineOnly, allEntityPoints, addBidirectional);
	}

	public List<Collision> getCameraCollision(Camera camera) {
		return world.getCameraCollision(camera);
	}

	public Set<Entity> getAllEntities() {
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

	public Camera getCameraForInput() {
		return world.getCameraForInput();
	}

	public boolean isUpdateWorld() {
		return world.isUpdateWorld();
	}

	public WorldAccess getWorldAccess() {
		return world.getWorldAccess();
	}

}

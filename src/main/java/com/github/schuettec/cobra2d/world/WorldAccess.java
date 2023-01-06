package com.github.schuettec.cobra2d.world;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

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

	public Set<Obstacle> getObstacles() {
		return world.getObstacles();
	}

	public Set<? extends HasCollisionShape> getObstaclesExcept(Entity... enties) {
		return world.getObstaclesExcept(enties);
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

	public <E extends Entity> CollisionMap detectCollision(Set<E> firstSet, Function<E, Shape> shapeExtractor1,
	    Set<E> secondSet, Function<E, Shape> shapeExtractor2, boolean outlineOnly, boolean allEntityPoints,
	    boolean addBidirectional) {
		return world.detectCollision(firstSet, shapeExtractor1, secondSet, shapeExtractor2, outlineOnly, allEntityPoints,
		    addBidirectional);
	}

	public <E extends Entity> CollisionMap detectCollision(Set<E> map, Function<E, Shape> shapeExtractor,
	    boolean outlineOnly, boolean allEntityPoints, boolean addBidirectional) {
		return world.detectCollision(map, shapeExtractor, outlineOnly, allEntityPoints, addBidirectional);
	}

	public <E extends Entity> List<CollisionDetail> detectFirstCollision(Shape shape, Set<E> map,
	    Function<E, Shape> shapeExtrator, boolean outlineOnly, boolean all) {
		return world.detectFirstCollision(shape, map, shapeExtrator, outlineOnly, all);
	}

	public <E extends Entity> Collision detectCollision(E e1, Function<E, Shape> firstShapeExtractor, E e2,
	    Function<E, Shape> secondShapeExtractor, boolean outlineOnly, boolean all) {
		return world.detectCollision(e1, firstShapeExtractor, e2, secondShapeExtractor, outlineOnly, all);
	}

	public List<CollisionDetail> detectCollision(Shape s1, Shape s2, boolean outlineOnly, boolean all) {
		return world.detectCollision(s1, s2, outlineOnly, all);
	}

}

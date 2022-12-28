package com.github.schuettec.cobra2d.world;

import static com.github.schuettec.cobra2d.entity.skills.Skill.asSkill;
import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.CollisionDetail;
import com.github.schuettec.cobra2d.entity.CollisionMap;
import com.github.schuettec.cobra2d.entity.Collisions;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Skill;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Shape;

/**
 * This is the map data structure. This class makes all {@link Entity} objects
 * accessible that are currently existing in the world. The {@link World} also
 * manages all the collisions for the current frame.
 *
 *
 * <p>
 * <b>Note: The map is intended to be used in the rendering thread or at least
 * in one thread managing all the entities calls. Therefore this {@link World} is
 * not thread safe.</b>
 * </p>
 *
 * @author Chris
 *
 */
public class World {

	// this would be a useful regression test
	// public static void main(String[] args) {
	// Map map = new Map();
	// Entity e1 = new AbstractPolygonObstacle((Point) null, new EntityPoint[]
	// {});
	// Entity e2 = new AbstractPolygonObstacle((Point) null, new EntityPoint[]
	// {});
	// map.detectedCollision = new HashSet<Collision>();
	// map.detectedCollision.add(new Collision(e1, null, new
	// LinkedList<Point>()));
	// map.detectedCollision.add(new Collision(e1, e2, new
	// LinkedList<Point>()));
	// System.out.println(map.detectedCollision.size());
	// }

	protected Set<Entity> allEntities;

	protected Set<Obstacle> obstacles;
	protected Set<Updatable> updateables;
	protected Set<Renderable> renderables;
	protected Set<Camera> cameras;

	private java.util.Map<Camera, List<Collision>> cameraCollisionMap;

	private boolean calculateFullCameraCollisionPoints = false;
	private boolean calculateFullEntityCollisionPoints = true;

	private Controller controller;

	public World(Controller controller) {
		this.controller = controller;
		this.allEntities = new HashSet<Entity>();
		this.obstacles = new HashSet<>();
		this.updateables = new HashSet<>();
		this.renderables = new HashSet<>();
		this.cameras = new HashSet<>();
		this.cameraCollisionMap = new Hashtable<>();
	}

	// public class AdHocDetection {
	// public List<CollisionDetail> getCollision(Shape shape, boolean all) {
	// return Collisions.detectFirstCollision(shape, obstacles, all);
	// }
	//
	// public List<CollisionDetail> getCollision(Shape shape, Set<? extends Obstacle> ignore, boolean all) {
	// Set<? extends Obstacle> toCheck = filter(ignore);
	// return Collisions.detectFirstCollision(shape, toCheck, all);
	// }
	//
	// public boolean hasCollision(Shape shape, boolean all) {
	// return Collisions.detectFirstCollision(shape, obstacles, all) != null;
	// }
	//
	// public boolean hasCollision(Shape shape, Set<? extends Obstacle> ignore, boolean all) {
	// Set<? extends Obstacle> toCheck = filter(ignore);
	// return Collisions.detectFirstCollision(shape, toCheck, all) != null;
	// }
	//
	// private Set<? extends Obstacle> filter(Set<? extends Obstacle> ignore) {
	// Set<? extends Obstacle> toCheck = new HashSet<>(obstacles);
	// if (ignore != null) {
	// toCheck.removeAll(ignore);
	// }
	// return toCheck;
	// }
	// }
	//
	// /**
	// * @return Returns an object that supports ad-hoc collision detection functions.
	// * This object is separated from the map because it supports time
	// * consuming features that are not recommended to be used heavily.
	// */
	// public AdHocDetection adHocDetection() {
	// return new AdHocDetection();
	// }

	public void addEntity(Entity... entities) {
		for (Entity e : entities) {
			addEntity(e);
		}
	}

	public void addEntity(Entity entity) {
		this.allEntities.add(entity);
		addEntityBySkill(entity);
	}

	private void addEntityBySkill(Entity entity) {
		addOnDemand(Camera.class, this.cameras, entity);
		addOnDemand(Obstacle.class, this.obstacles, entity);
		addOnDemand(Updatable.class, this.updateables, entity);
		addOnDemand(Renderable.class, this.renderables, entity);
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
	}

	private <S extends Skill> void addOnDemand(Class<S> skillType, Set<S> obstacles, Entity entity) {
		Optional<S> asSkill = asSkill(skillType, entity);
		if (asSkill.isPresent()) {
			obstacles.add(asSkill.get());
		}
	}

	private <S extends Skill> void removeOnDemand(Class<S> skillType, Set<S> obstacles, Entity entity) {
		Optional<S> asSkill = asSkill(skillType, entity);
		if (asSkill.isPresent()) {
			obstacles.remove(asSkill.get());
		}
	}

	public void update(float deltaTime) {
		for (Updatable updatable : updateables) {
			updatable.update(this, deltaTime, controller);
		}
		cameraCollisionMap.clear();
		// Detect all collisions in the set of renderables with cameras
		for (Camera camera : cameras) {
			Set<Camera> cameraSet = new HashSet<>();
			cameraSet.add(camera);
			CollisionMap map = detectCollision(cameraSet, renderables, false, calculateFullCameraCollisionPoints, false);
			List<Collision> cameraCollisions = map.getCollisions();
			cameraCollisionMap.put(camera, cameraCollisions);
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

	public Set<Entity> getAllEntities() {
		return new HashSet<>(allEntities);
	}

	public Set<Obstacle> getObstacles() {
		return new HashSet<>(obstacles);
	}

	public Set<Updatable> getUpdateables() {
		return new HashSet<>(updateables);
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
	 * Performs a collision detection.
	 * 
	 * @param firstSet The first set of entities
	 * @param secondSet The second set of entities.
	 * @param allEntityPoints If <code>true</code> all entity points are resolved in case entities collide. If
	 *        <code>false</code> the first entity point of a collision is used.
	 * @param addBidirectional If <code>true</code> the collision is added bidirectionally to the collision map. If
	 *        <code>false</code> only the collider of the first set is added.
	 * @return
	 */
	public CollisionMap detectCollision(HasCollisionShape firstEntity, Set<? extends HasCollisionShape> secondSet,
	    boolean outlineOnly, boolean allEntityPoints, boolean addBidirectional) {
		return Collisions.detectCollision(firstEntity, secondSet, outlineOnly, allEntityPoints, addBidirectional);
	}

	/**
	 * Performs a collision detection.
	 * 
	 * @param firstSet The first set of entities
	 * @param secondSet The second set of entities.
	 * @param allEntityPoints If <code>true</code> all entity points are resolved in case entities collide. If
	 *        <code>false</code> the first entity point of a collision is used.
	 * @param addBidirectional If <code>true</code> the collision is added bidirectionally to the collision map. If
	 *        <code>false</code> only the collider of the first set is added.
	 * @return
	 */
	public CollisionMap detectCollision(Set<? extends HasCollisionShape> firstSet,
	    Set<? extends HasCollisionShape> secondSet, boolean outlineOnly, boolean allEntityPoints,
	    boolean addBidirectional) {
		return Collisions.detectCollision(firstSet, secondSet, outlineOnly, allEntityPoints, addBidirectional);
	}

	/**
	 * Calculates the collision of all entities in the specified set.
	 *
	 * @param collisionMap A collision map to store the detected
	 *        collisions.
	 * @param map The set of entities to detect collisions for.
	 * @param allEntityPoints Specified if all collision points should be
	 *        calculated. If <code>false</code> only the
	 *        first collision point will be calculated. If
	 *        <code>true</code> all the other points will be
	 *        calculated.
	 *
	 * @param bidirectionalCollisions If <code>true</code> and if a collision c1~c2
	 *        was detected, the {@link Collision} object for
	 *        c2~c1 will also be calculated.
	 * @return
	 */
	public CollisionMap detectCollision(Set<? extends HasCollisionShape> map, boolean outlineOnly,
	    boolean allEntityPoints, boolean addBidirectional) {
		// TODO: We can optimize this call: If c1~c2 was checked, then c2~c1 can
		// be skipped. We can achieve this if we separate the map in two disjunct sets.
		return Collisions.detectCollision(map, outlineOnly, allEntityPoints, addBidirectional);
	}

	/**
	 * Calculates the first collision of the specified shape with all entities in
	 * the specified set.
	 *
	 * @param shape The shape used to check collisions.
	 * @param map The set of entities to detect collisions with the shape.
	 * @param all Specified if all collision points should be calculated. If
	 *        <code>false</code> only the first collision point will be
	 *        calculated. If <code>true</code> all the other points will be
	 *        calculated.
	 * @return Returns the collision points on the shape as a list.
	 */
	public List<CollisionDetail> detectFirstCollision(Shape shape, Set<? extends HasCollisionShape> map,
	    boolean outlineOnly, boolean all) {
		return Collisions.detectFirstCollision(shape, map, outlineOnly, all);
	}

	/**
	 * Detects collisions for two {@link Obstacle}s.
	 *
	 * @param e1 The obstacle.
	 * @param e2 the other obstacle. The set of entities to detect collisions with
	 *        the shape.
	 * @param all Specified if all collision points should be calculated. If
	 *        <code>false</code> only the first collision point will be
	 *        calculated. If <code>true</code> all the other points will be
	 *        calculated.
	 * @return Returns a {@link Collision} object that manages the list of collision
	 *         points or <code>null</code> if no collision was detected.
	 */
	public Collision detectCollision(HasCollisionShape e1, HasCollisionShape e2, boolean outlineOnly, boolean all) {
		return Collisions.detectCollision(e1, e2, outlineOnly, all);
	}

	/**
	 * Detects a collision between to shapes.
	 * 
	 * @param s1 Shape
	 * @param s2 Another shape
	 * @param all If <code>true</code> all collision points are calculates, otherwise the first collision point is
	 *        returned. <b>Note: Do not calculate all collision points if you do not really need them.</b>
	 * 
	 * @return Returns the list of collision points or an empty list if there is no collision.
	 */
	public List<CollisionDetail> detectCollision(Shape s1, Shape s2, boolean outlineOnly, boolean all) {
		return Collisions.detectCollision(s1, s2, outlineOnly, all);
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

	public CollisionMap detectCollision(Shape shape, Set<? extends HasCollisionShape> obstaclesExcept,
	    boolean outlineOnly, boolean allEntityPoints, boolean addBidirectional) {
		return Collisions.detectCollision(shape, obstaclesExcept, outlineOnly, allEntityPoints, addBidirectional);
	}

}

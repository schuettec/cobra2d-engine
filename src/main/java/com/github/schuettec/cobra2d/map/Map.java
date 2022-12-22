package com.github.schuettec.cobra2d.map;

import static com.github.schuettec.cobra2d.entity.Collisions.detectCollision;
import static com.github.schuettec.cobra2d.entity.skills.Skill.asSkill;
import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.CollisionMap;
import com.github.schuettec.cobra2d.entity.Collisions;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Skill;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Shape;

/**
 * This is the map data structure. This class makes all {@link Entity} objects
 * accessible that are currently existing in the world. The {@link Map} also
 * manages all the collisions for the current frame.
 *
 *
 * <p>
 * <b>Note: The map is intended to be used in the rendering thread or at least
 * in one thread managing all the entities calls. Therefore this {@link Map} is
 * not thread safe.</b>
 * </p>
 *
 * @author Chris
 *
 */
public class Map {

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

	private CollisionMap detectedCollision;
	private java.util.Map<Camera, List<Collision>> cameraCollisionMap;

	private boolean calculateFullCameraCollisionPoints = false;
	private boolean calculateFullEntityCollisionPoints = true;

	private Controller controller;

	public Map(Controller controller) {
		this.controller = controller;
		this.allEntities = new HashSet<Entity>();
		this.obstacles = new HashSet<>();
		this.updateables = new HashSet<>();
		this.renderables = new HashSet<>();
		this.cameras = new HashSet<>();
		this.detectedCollision = new CollisionMap();
		this.cameraCollisionMap = new Hashtable<>();
	}

	public class AdHocDetection {
		public List<Point> getCollision(Shape shape, boolean all) {
			return Collisions.detectFirstCollision(shape, obstacles, all);
		}

		public List<Point> getCollision(Shape shape, Set<? extends Obstacle> ignore, boolean all) {
			Set<? extends Obstacle> toCheck = filter(ignore);
			return Collisions.detectFirstCollision(shape, toCheck, all);
		}

		public boolean hasCollision(Shape shape, boolean all) {
			return Collisions.detectFirstCollision(shape, obstacles, all) != null;
		}

		public boolean hasCollision(Shape shape, Set<? extends Obstacle> ignore, boolean all) {
			Set<? extends Obstacle> toCheck = filter(ignore);
			return Collisions.detectFirstCollision(shape, toCheck, all) != null;
		}

		private Set<? extends Obstacle> filter(Set<? extends Obstacle> ignore) {
			Set<? extends Obstacle> toCheck = new HashSet<>(obstacles);
			if (ignore != null) {
				toCheck.removeAll(ignore);
			}
			return toCheck;
		}
	}

	/**
	 * @return Returns an object that supports ad-hoc collision detection functions.
	 *         This object is separated from the map because it supports time
	 *         consuming features that are not recommended to be used heavily.
	 */
	public AdHocDetection adHocDetection() {
		return new AdHocDetection();
	}

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

	public void update() {
		for (Updatable updatable : updateables) {
			List<Collision> collisions = new LinkedList<>();
			if (updatable instanceof Obstacle) {
				if (detectedCollision.hasCollision(updatable)) {
					Collision collision = detectedCollision.getCollision(updatable);
					collisions.add(collision);
				}
			}
			updatable.update(controller, collisions);
		}

		// Detect all collisions in the set of obstacles
		detectCollision(this.detectedCollision, obstacles, calculateFullEntityCollisionPoints, true);

		cameraCollisionMap.clear();
		// Detect all collisions in the set of renderables with cameras
		for (Camera camera : cameras) {
			CollisionMap map = new CollisionMap();
			Set<Camera> cameraSet = new HashSet<>();
			cameraSet.add(camera);
			detectCollision(map, cameraSet, renderables, calculateFullCameraCollisionPoints, false);
			List<Collision> cameraCollisions = map.getCollisions();
			cameraCollisionMap.put(camera, cameraCollisions);
		}
	}

	/**
	 * Checks if there was a collision reported by the collision detection where the
	 * specified entity is involved.
	 *
	 * @param entity The entity as one of the collision participant.
	 * @return Returns <code>true</code> if there was a collision detected,
	 *         otherwise <code>false</code> is returned.
	 */
	public boolean hasCollision(Entity entity) {
		return detectedCollision.hasCollision(entity);
	}

	/**
	 * Returns the calculated collision for the specified {@link Entity} if there is
	 * one.
	 *
	 * @param entity The entity to check for collisions.
	 * @return Returns the {@link Collision} object for the specified entity if it
	 *         collides with another entity, otherwise <code>null</code> is
	 *         returned.
	 */
	public Collision getCollision(Entity entity) {
		return detectedCollision.getCollision(entity);
	}

	public List<Collision> getCollisions() {
		return this.detectedCollision.getCollisions();
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

}

package com.github.schuettec.cobra2d.entity;

import static com.github.schuettec.cobra2d.entity.CollisionDetail.ofLineBased;
import static com.github.schuettec.cobra2d.entity.CollisionDetail.ofNonLineBased;
import static java.util.Objects.nonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.EntityPoint;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;
import com.github.schuettec.cobra2d.math.Rectangle;
import com.github.schuettec.cobra2d.math.Shape;

/**
 * A public util class with methods to perform a collision detection.
 *
 * @author schuettec
 *
 */
public abstract class Collisions {
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
	public static CollisionMap detectCollision(HasCollisionShape firstEntity, Set<? extends HasCollisionShape> secondSet,
	    boolean outlineOnly, boolean allEntityPoints, boolean addBidirectional) {
		return detectCollision(Set.of(firstEntity), secondSet, outlineOnly, allEntityPoints, addBidirectional);
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
	public static CollisionMap detectCollision(Set<? extends HasCollisionShape> firstSet,
	    Set<? extends HasCollisionShape> secondSet, boolean outlineOnly, boolean allEntityPoints,
	    boolean addBidirectional) {
		CollisionMap collisionMap = new CollisionMap();

		for (Entity c1 : firstSet) {
			for (Entity c2 : secondSet) {
				if (c1 == c2)
					continue;

				HasCollisionShape o1 = (HasCollisionShape) c1;
				HasCollisionShape o2 = (HasCollisionShape) c2;
				{
					Collision collision = detectCollision(o1, o2, outlineOnly, allEntityPoints);
					if (addBidirectional) {
						Collision reverse = detectCollision(o2, o1, outlineOnly, allEntityPoints);
						collisionMap.addCollisionsBidirectional(collision, reverse);
					} else {
						collisionMap.addCollisionsUnidirectional(collision);
					}
				}
			}

		}
		return collisionMap;
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
	public static CollisionMap detectCollision(Set<? extends HasCollisionShape> map, boolean outlineOnly,
	    boolean allEntityPoints, boolean addBidirectional) {
		// TODO: We can optimize this call: If c1~c2 was checked, then c2~c1 can
		// be skipped. We can achieve this if we separate the map in two disjunct sets.
		return detectCollision(new HashSet<>(map), new HashSet<>(map), outlineOnly, allEntityPoints, addBidirectional);
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
	public static List<CollisionDetail> detectFirstCollision(Shape shape, Set<? extends HasCollisionShape> map,
	    boolean outlineOnly, boolean all) {

		for (Entity c1 : new HashSet<>(map)) {
			HasCollisionShape o1 = (HasCollisionShape) c1;
			List<CollisionDetail> collision = detectCollision(shape, o1.getCollisionShapeInWorldCoordinates(), outlineOnly,
			    all);
			// Collision may be null if there is none
			if (collision != null) {
				collision = detectCollision(o1.getCollisionShapeInWorldCoordinates(), shape, outlineOnly, all);
			}
			if (!collision.isEmpty()) {
				return collision;
			}
		}
		return null;
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
	public static Collision detectCollision(HasCollisionShape e1, HasCollisionShape e2, boolean outlineOnly,
	    boolean all) {
		Shape s1 = e1.getCollisionShapeInWorldCoordinates();
		Shape s2 = e2.getCollisionShapeInWorldCoordinates();
		List<CollisionDetail> collisions = detectCollision(s1, s2, outlineOnly, all);
		if (collisions.isEmpty()) {
			return null;
		} else {
			return new Collision(e1, e2, collisions, all);
		}
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
	public static List<CollisionDetail> detectCollision(Shape s1, Shape s2, boolean outlineOnly, boolean all) {
		List<CollisionDetail> collisions = null;
		if (s1 instanceof Polygon && s2 instanceof Polygon) {
			collisions = _detectCollision((Polygon) s1, (Polygon) s2, outlineOnly, all);
		} else if (s1 instanceof Circle && s2 instanceof Circle) {
			collisions = _detectCollision((Circle) s1, (Circle) s2, outlineOnly, all);
		} else if (s1 instanceof Circle && s2 instanceof Polygon) {
			collisions = _detectCollision((Circle) s1, (Polygon) s2, outlineOnly, all);
		} else {
			collisions = _detectCollision((Circle) s2, (Polygon) s1, outlineOnly, all);
		}

		return collisions;
	}

	private static List<CollisionDetail> _detectCollision(Circle c1, Circle c2, boolean outlineOnly, boolean all) {
		/*
		 * The following collision detection only checks the collision with the outline of the
		 * given shapes.
		 */
		List<CollisionDetail> collisions = new LinkedList<>();

		double d = Math2D.getEntfernung(c1.getPosition(), c2.getPosition());

		double r0 = c1.getRadius();
		double r1 = c2.getRadius();
		if (d > r0 + r1) {
			return collisions;
		} else if (d < Math.abs(r0 - r1)) {
			return _detectInnerCollision(c1, c2);
		} else if (d == 0 && r0 == r1) {
			Point p = c1.getPosition()
			    .clone();
			collisions.add(ofNonLineBased(p));
			return collisions;
		}

		double a = (Math.pow(r0, 2) - Math.pow(r1, 2) + Math.pow(d, 2)) / (2d * d);
		double h = Math.sqrt(Math.pow(r0, 2) - Math.pow(a, 2));

		Point p0 = c1.getPosition();
		Point p1 = c2.getPosition();
		Point p2 = new Point(p0.getX() + a * (p1.getX() - p0.getX()) / d, p0.getY() + a * (p1.getY() - p0.getY()) / d);

		Point p31 = new Point(p2.getX() + h * (p1.getY() - p0.getY()) / d, p2.getY() - h * (p1.getX() - p0.getX()) / d);
		Point p32 = new Point(p2.getX() - h * (p1.getY() - p0.getY()) / d, p2.getY() + h * (p1.getX() - p0.getX()) / d);

		collisions.add(ofNonLineBased(p31));
		collisions.add(ofNonLineBased(p32));

		/*
		 * The following collision detection checks also the inner collision of shapes, occurring if shapes containing each
		 * other.
		 */
		if (collisions.isEmpty()) {
			if (outlineOnly) {
				return Collections.emptyList();
			} else {
				return _detectInnerCollision(c1, c2);
			}
		} else {
			return collisions;
		}
	}

	/**
	 * This method checks if the circles contain each other.
	 * 
	 * @param c1 Cicle 1
	 * @param c2 Cicle 2
	 * @return Returns the center point of circle 2 if the circle c2 is inside c2. If there is no collision, an empty list
	 *         is returned.
	 */
	private static List<CollisionDetail> _detectInnerCollision(Circle c1, Circle c2) {
		Point m1 = c1.getPosition();
		Point m2 = c2.getPosition();
		double distance = Math2D.getEntfernung(m1, m2);
		if (distance < Math.abs(c1.getRadius() - c2.getRadius())) {
			List<CollisionDetail> result = new LinkedList<>();
			// Just return the center point of c2, because the collisiont point is not neccessarily c1.center.
			result.add(ofNonLineBased(c2.getPosition()));
			return result;
		} else {
			return Collections.emptyList();
		}
	}

	private static List<CollisionDetail> _detectCollision(Circle p1, Polygon p2, boolean outlineOnly, boolean all) {
		/*
		 * The following collision detection only checks the collision with the outline of the
		 * given shapes.
		 */
		List<CollisionDetail> collisions = new LinkedList<>();
		List<Line> h2 = p2.getLines();
		for (Line l2 : h2) {

			if (l2.isParallelY()) {
				double x = l2.getX1()
				    .getX();
				double mx = p1.getPosition()
				    .getRoundX();
				double my = p1.getPosition()
				    .getRoundY();
				double r = p1.getRadius();

				double y1 = my + Math.sqrt(Math.pow(r, 2) - Math.pow(x - mx, 2));
				double y2 = my - Math.sqrt(Math.pow(r, 2) - Math.pow(x - mx, 2));

				Point candidate1 = new Point(x, y1);
				Point candidate2 = new Point(x, y2);

				if (l2.isDefined(candidate1)) {
					collisions.add(ofLineBased(candidate1, null, l2));
				}

				if (l2.isDefined(candidate2)) {
					collisions.add(ofLineBased(candidate2, null, l2));
				}

			} else {
				// Kreisgleichung:
				// (x - xM)² + ([m * x + n] - yM)² = r²
				// =>
				// x² - ((xm + cm) / (m² + 1)) x + (xm² + c² - r²) / (m² + 1) = 0
				double xm = p1.getPosition().x;
				double ym = p1.getPosition().y;
				double r = p1.getRadius();
				double m = l2.getM();
				double b = l2.getB();

				double p = ((-2d * xm + 2d * m * b - 2d * m * ym) / (Math.pow(m, 2) + 1d));
				// VORHER:
				double q = ((Math.pow(xm, 2) + Math.pow(b - ym, 2) - Math.pow(r, 2)) / (Math.pow(m, 2) + 1d));

				double[] results = Math2D.pqFormula(p, q);

				if (results.length > 0) {
					for (double result : results) {
						double x = result;
						double y = m * x + b;
						Point point = new Point(x, y);
						if (l2.isDefined(point)) {
							collisions.add(ofLineBased(point, null, l2));
						}
					}
				}
			}

			if (!all && !collisions.isEmpty()) {
				break;
			}
		}

		/*
		 * The following collision detection checks also the inner collision of shapes, occurring if shapes containing each
		 * other.
		 */
		if (collisions.isEmpty()) {
			if (outlineOnly) {
				return Collections.emptyList();
			} else {
				return _detectInnerCollision(p1, p2, all);
			}
		} else {
			return collisions;
		}
	}

	/**
	 * This method checks if the circles contains the polygon. Note: The collision with the outline is not checked here!
	 * 
	 * @param p1 Cicle
	 * @param p2 Polygon
	 * @param allPoints If <code>true</code> the returned list contains all points of the polygon that are inside the
	 *        circle. If <code>false</code> only the first point inside the circle is returned:
	 * @return Returns the points of the polygon that are contained within the circle. If there is no collision, an empty
	 *         list is returned.
	 */
	private static List<CollisionDetail> _detectInnerCollision(Circle p1, Polygon p2, boolean allPoints) {
		List<CollisionDetail> points = new LinkedList<>();
		Point c = p1.getPosition();
		double radius = p1.getRadius();
		List<EntityPoint> polygonPoints = p2.getEntityPoints();
		for (EntityPoint p : polygonPoints) {
			Point polygonPoint = p.getCoordinates();
			boolean inCircle = Math2D.isInCircle(polygonPoint, c, radius);
			if (inCircle) {
				points.add(ofNonLineBased(polygonPoint));
				if (!allPoints) {
					break;
				}
			}
		}
		if (points.isEmpty()) {
			// Possible if the circle is inside to polygon. Check if the circles center is inside the polygon:
			if (isPointInPolygon(p1.getPosition(), p2)) {
				return Arrays.asList(ofNonLineBased(p1.getPosition()));
			} else {
				return Collections.emptyList();
			}
		} else {
			return points;
		}
	}

	/**
	 * Calculates the collision of two polygons.
	 *
	 * @param p1 A Polygon
	 * @param p2 the other polygon
	 * @param all Specified if all collision points should be calculated. If
	 *        <code>false</code> only the first collision point will be
	 *        calculated. If <code>true</code> all the other points will be
	 *        calculated.
	 *
	 * @return Returns the list of collision points. How much collision points will
	 *         be calculated is specified with the <code>all</code> parameter.
	 */
	private static List<CollisionDetail> _detectCollision(Polygon p1, Polygon p2, boolean outlineOnly, boolean all) {
		List<CollisionDetail> collisions = new LinkedList<>();
		List<Line> h1 = p1.getLines();
		List<Line> h2 = p2.getLines();
		for (Line l1 : h1) {
			for (Line l2 : h2) {
				Point intersection = l1.intersects(l2);
				if (intersection != null) {
					collisions.add(ofLineBased(intersection, l1, l2));
					if (!all) {
						return collisions;
					}
				}
			}
		}

		/*
		 * The following collision detection checks also the inner collision of shapes, occurring if shapes containing each
		 * other.
		 */
		if (collisions.isEmpty()) {
			if (outlineOnly) {
				return Collections.emptyList();
			} else {
				List<CollisionDetail> p1p2 = _detectInnerCollision(p1, p2, all);
				if (p1p2.isEmpty()) {
					List<CollisionDetail> p2p1 = _detectInnerCollision(p2, p1, all);
					if (p2p1.isEmpty()) {
						return Collections.emptyList();
					} else {
						return p2p1;
					}
				} else {
					return p1p2;
				}
			}
		} else {
			return collisions;
		}
	}

	private static List<CollisionDetail> _detectInnerCollision(Polygon p1, Polygon p2, boolean all) {
		List<Point> points = p1.getEntityPoints()
		    .stream()
		    .map(p -> p.getCoordinates())
		    .collect(Collectors.toList());
		Rectangle huellRect = Math2D.getHuellRect(points);
		Point centerOfPolygon = new Point(huellRect.getX() + Math2D.saveRound(huellRect.getWidth() / 2.0),
		    huellRect.getY() + Math2D.saveRound(huellRect.getHeight() / 2.0));
		if (isPointInPolygon(centerOfPolygon, p2)) {
			return Arrays.asList(ofNonLineBased(centerOfPolygon));
		} else {
			return Collections.emptyList();
		}
	}

	private static boolean isPointInPolygon(Point point, Polygon polygon) {
		List<Point> points = polygon.getEntityPoints()
		    .stream()
		    .map(p -> p.getCoordinates())
		    .collect(Collectors.toList());
		Point maxX = Math2D.getPointMaxX(points);

		if (point.getRoundX() > maxX.getRoundX()) {
			return false;
		}

		// The magic +1 means that the collision check line must be a little longer than the maxX of the polygon to create
		// an intersection.
		Line collisionCheck = new Line(point, new Point(maxX.getRoundX() + 1, point.getRoundY()));

		List<Point> collisionPoint = new LinkedList<>();
		List<Line> lines = polygon.getLines();
		for (Line pLine : lines) {
			Point intersection = pLine.intersects(collisionCheck);
			// If the collision check line lays exactly on the polygon-line, the polygon line is parallel to X-Axis. In this
			// case the
			// shapes can only collide on the outline, which was checked before this method.
			// This case is defined to not being a collision.
			if (nonNull(intersection) && !pLine.isParallelX()) {
				collisionPoint.add(intersection);
			}
		}
		// if the number of collision points is odd the point is inside the polygon.
		return (collisionPoint.size() % 2 != 0);
	}

	public static CollisionMap detectCollision(Shape shape, Set<? extends HasCollisionShape> obstacles,
	    boolean outlineOnly, boolean allEntityPoints, boolean addBidirectional) {
		HasCollisionShape emulated = new HasCollisionShape() {

			@Override
			public Entity translate(Point translation) {
				throw new IllegalAccessError("This method should not be called here!");
			}

			@Override
			public void setScale(double scale) {
				throw new IllegalAccessError("This method should not be called here!");
			}

			@Override
			public void setPosition(Point worldCoordinates) {
				throw new IllegalAccessError("This method should not be called here!");
			}

			@Override
			public void setDegrees(double degrees) {
				throw new IllegalAccessError("This method should not be called here!");
			}

			@Override
			public Entity scale(double scaleFactor) {
				throw new IllegalAccessError("This method should not be called here!");
			}

			@Override
			public Entity rotate(double degrees) {
				throw new IllegalAccessError("This method should not be called here!");
			}

			@Override
			public double getScale() {
				throw new IllegalAccessError("This method should not be called here!");

			}

			@Override
			public Point getPosition() {
				throw new IllegalAccessError("This method should not be called here!");

			}

			@Override
			public double getDegrees() {
				throw new IllegalAccessError("This method should not be called here!");

			}

			@Override
			public Shape getCollisionShapeInWorldCoordinates() {
				return shape;
			}

			@Override
			public Shape getCollisionShape(boolean applyScaling, boolean applyRotation, boolean applyWorldCoordinates) {
				return shape;
			}

			@Override
			public String getId() {
				return UUID.randomUUID()
				    .toString();
			}

			@Override
			public void setId(String setId) {
			}
		};
		return detectCollision(emulated, obstacles, outlineOnly, allEntityPoints, addBidirectional);
	}
}

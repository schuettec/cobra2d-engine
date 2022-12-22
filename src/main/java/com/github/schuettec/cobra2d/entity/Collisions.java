package com.github.schuettec.cobra2d.entity;

import static java.util.Objects.nonNull;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
import com.github.schuettec.cobra2d.math.Shape;

/**
 * A public util class with methods to perform a collision detection.
 *
 * @author schuettec
 *
 */
public abstract class Collisions {

	/**
	 * Calculates the collision pairs of two sets. If the sets are disjunct this
	 * method can work more efficiently.
	 *
	 * @param collisionMap A collision map to store the detected
	 *        collisions.
	 * @param map The set of entities to detect collisions for.
	 * @param allEntityPoints Specified if all collision points should be
	 *        calculated. If <code>false</code> only the
	 *        first collision point will be calculated. If
	 *        <code>true</code> all the other points will be
	 *        calculated.
	 */
	public static void detectCollision(CollisionMap collisionMap, Set<? extends HasCollisionShape> firstSet,
	    Set<? extends HasCollisionShape> secondSet, boolean allEntityPoints) {
		collisionMap.clearCollisions();

		for (Entity c1 : firstSet) {
			for (Entity c2 : secondSet) {
				if (c1 == c2)
					continue;

				HasCollisionShape o1 = (HasCollisionShape) c1;
				HasCollisionShape o2 = (HasCollisionShape) c2;
				{
					Collision collision = detectCollision(o1, o2, allEntityPoints);
					Collision reverse = detectCollision(o2, o1, allEntityPoints);
					collisionMap.addCollisionsBidirectional(collision, reverse);
				}
			}

		}
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
	 */
	public static void detectCollision(CollisionMap collisionMap, Set<? extends HasCollisionShape> map,
	    boolean allEntityPoints) {
		// TODO: We can optimize this call: If c1~c2 was checked, then c2~c1 can
		// be skipped. We can achieve this if we separate the map in two disjunct sets.
		detectCollision(collisionMap, new HashSet<>(map), new HashSet<>(map), allEntityPoints);
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
	public static List<Point> detectFirstCollision(Shape shape, Set<? extends HasCollisionShape> map, boolean all) {

		for (Entity c1 : new HashSet<>(map)) {
			HasCollisionShape o1 = (HasCollisionShape) c1;
			List<Point> collision = detectCollision(shape, o1.getCollisionShape(), all);
			// Collision may be null if there is none
			if (collision != null) {
				collision = detectCollision(o1.getCollisionShape(), shape, all);
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
	public static Collision detectCollision(HasCollisionShape e1, HasCollisionShape e2, boolean all) {
		Shape s1 = e1.getCollisionShape();
		Shape s2 = e2.getCollisionShape();
		List<Point> collisions = detectCollision(s1, s2, all);
		if (collisions.isEmpty()) {
			return null;
		} else {
			return new Collision(e1, e2, collisions, all);
		}
	}

	public static List<Point> detectCollision(Shape s1, Shape s2, boolean all) {
		List<Point> collisions = null;

		if (s1 instanceof Polygon && s2 instanceof Polygon) {
			collisions = _detectCollision((Polygon) s1, (Polygon) s2, all);
		} else if (s1 instanceof Circle && s2 instanceof Circle) {
			collisions = _detectCollision((Circle) s1, (Circle) s2, all);
		} else if (s1 instanceof Circle && s2 instanceof Polygon) {
			collisions = _detectCollision((Circle) s1, (Polygon) s2, all);
		} else {
			collisions = _detectCollision((Circle) s2, (Polygon) s1, all);
		}

		return collisions;
	}

	private static List<Point> _detectCollision(Circle c1, Circle c2, boolean all) {
		/*
		 * The following collision detection only checks the collision with the outline of the
		 * given shapes.
		 */
		List<Point> collisions = new LinkedList<>();

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
			collisions.add(p);
			return collisions;
		}

		double a = (Math.pow(r0, 2) - Math.pow(r1, 2) + Math.pow(d, 2)) / (2d * d);
		double h = Math.sqrt(Math.pow(r0, 2) - Math.pow(a, 2));

		Point p0 = c1.getPosition();
		Point p1 = c2.getPosition();
		Point p2 = new Point(p0.getX() + a * (p1.getX() - p0.getX()) / d, p0.getY() + a * (p1.getY() - p0.getY()) / d);

		Point p31 = new Point(p2.getX() + h * (p1.getY() - p0.getY()) / d, p2.getY() - h * (p1.getX() - p0.getX()) / d);
		Point p32 = new Point(p2.getX() - h * (p1.getY() - p0.getY()) / d, p2.getY() + h * (p1.getX() - p0.getX()) / d);

		collisions.add(p31);
		collisions.add(p32);

		/*
		 * The following collision detection checks also the inner collision of shapes, occurring if shapes containing each
		 * other.
		 */
		if (collisions.isEmpty()) {
			return _detectInnerCollision(c1, c2);
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
	private static List<Point> _detectInnerCollision(Circle c1, Circle c2) {
		Point m1 = c1.getPosition();
		Point m2 = c2.getPosition();
		double distance = Math2D.getEntfernung(m1, m2);
		if (distance < Math.abs(c1.getRadius() - c2.getRadius())) {
			List<Point> result = new LinkedList<>();
			// Just return the center point of c2, because the collisiont point is not neccessarily c1.center.
			result.add(c2.getPosition());
			return result;
		} else {
			return Collections.emptyList();
		}
	}

	private static List<Point> _detectCollision(Circle p1, Polygon p2, boolean all) {
		/*
		 * The following collision detection only checks the collision with the outline of the
		 * given shapes.
		 */
		List<Point> collisions = new LinkedList<>();
		List<Line> h2 = p2.getLines();
		for (Line l2 : h2) {
			// Kreisgleichung:
			// (x - xM)² + ([m * x + n] - yM)² = r²
			// =>
			// x² - ((xm + cm) / (m² + 1)) x + (xm² + c² - r²) / (m² + 1) = 0
			double xm = p1.getPosition().x;
			double ym = p1.getPosition().y;
			double r = p1.getRadius();
			double m = l2.getM();
			double n = l2.getB();

			double p = ((-2d * xm + 2d * m * n - 2d * m * ym) / (Math.pow(m, 2) + 1d));
			double q = ((Math.pow(xm, 2) + Math.pow(n - ym, 2) - Math.pow(r, 2)) / (Math.pow(m, 2) + 1d));

			double[] results = Math2D.pqFormula(p, q);

			if (results.length > 0) {
				for (double result : results) {
					if (result >= 0) {
						double x = result;
						double y = m * x + n;
						Point point = new Point(x, y);
						if (l2.isDefined(point)) {
							collisions.add(point);
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
			return _detectInnerCollision(p1, p2, all);
		} else {
			return collisions;
		}
	}

	/**
	 * This method checks if the circles contains the polygon. Note: The collisoin with the outline is not checked here!
	 * 
	 * @param p1 Cicle
	 * @param p2 Polygon
	 * @param allPoints If <code>true</code> the returned list contains all points of the polygon that are inside the
	 *        circle. If <code>false</code> only the first point inside the circle is returned:
	 * @return Returns the points of the polygon that are contained within the circle. If there is no collision, an empty
	 *         list is returned.
	 */
	private static List<Point> _detectInnerCollision(Circle p1, Polygon p2, boolean allPoints) {
		List<Point> points = new LinkedList<>();
		Point c = p1.getPosition();
		double radius = p1.getRadius();
		List<EntityPoint> polygonPoints = p2.getEntityPoints();
		for (EntityPoint p : polygonPoints) {
			Point polygonPoint = p.getCoordinates();
			boolean inCircle = Math2D.isInCircle(polygonPoint, c, radius);
			if (inCircle) {
				points.add(polygonPoint);
				if (!allPoints) {
					break;
				}
			}
		}
		return points;
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
	private static List<Point> _detectCollision(Polygon p1, Polygon p2, boolean all) {
		List<Point> collisions = new LinkedList<>();
		List<Line> h1 = p1.getLines();
		List<Line> h2 = p2.getLines();
		for (Line l1 : h1) {
			for (Line l2 : h2) {
				Point intersection = l1.intersects(l2);
				if (intersection != null) {
					collisions.add(intersection);
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
			return _detectInnerCollision(p1, p2, all);
		} else {
			return collisions;
		}
	}

	private static List<Point> _detectInnerCollision(Polygon p1, Polygon p2, boolean all) {
		List<Line> lines = p1.getLines();
		List<Point> points = p2.getEntityPoints()
		    .stream()
		    .map(p -> p.getCoordinates())
		    .collect(Collectors.toList());
		Rectangle huellRect = Math2D.getHuellRect(points);
		Point centerOfPolygon = new Point(huellRect.getLocation().x, huellRect.getLocation().y);
		Point maxX = Math2D.getPointMaxDistToY(points);
		Line collisionCheck = new Line(centerOfPolygon, maxX);
		for (Line pLine : lines) {
			if (nonNull(pLine.intersects(collisionCheck))) {
				List<Point> collisionPoint = new LinkedList<>();
				collisionPoint.add(centerOfPolygon);
				return collisionPoint;
			}
		}
		return Collections.emptyList();
	}

}

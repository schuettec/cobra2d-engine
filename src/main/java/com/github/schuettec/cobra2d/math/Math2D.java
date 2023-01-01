package com.github.schuettec.cobra2d.math;

import static java.util.Arrays.asList;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.List;

import com.github.schuettec.cobra2d.entity.skills.Entity;

public class Math2D {

	public static List<EntityPoint> getRectangle(Dimension dimension) {
		return getRectangle(dimension.getWidth(), dimension.getHeight());
	}

	public static List<EntityPoint> getRectangle(double width, double height) {
		double dX = Math2D.saveRound(width / 2.0d);
		double dY = Math2D.saveRound(height / 2.0d);
		EntityPoint ep1 = new EntityPoint(new Point(-dX, dY));
		EntityPoint ep2 = new EntityPoint(new Point(dX, dY));
		EntityPoint ep3 = new EntityPoint(new Point(+dX, -dY));
		EntityPoint ep4 = new EntityPoint(new Point(-dX, -dY));
		return asList(ep1, ep2, ep3, ep4);
	}

	/**
	 * Calculates the middlepoint in a picture.
	 *
	 * @param image
	 *        The picture
	 * @return Returns the middlepoint in the picture
	 */
	public static Point getMiddlepoint(final BufferedImage image) {
		final double middleX = image.getWidth() / 2.0d;
		final double middleY = image.getHeight() / 2.0d;
		final Point middleInPicture = new Point(middleX, middleY);
		return middleInPicture;
	}

	/**
	 * Calculates a random number between min an max (included).
	 *
	 * @param min
	 * @param max
	 * @return The random number in the given range.
	 */
	public static double random(double min, double max) {
		final double h = min;
		min = Math.min(min, max);
		max = Math.max(h, max);
		return (Math.random() * (max - min + 1d) + min);
	}

	// /**
	// * Shorter command for (int) Math.round(value);
	// *
	// * @param value
	// * @return Returns the rounded int of the value.
	// */
	// public static int saveRound(final float value) {
	// return Math.round(value);
	// }

	/**
	 * Shorter command for (int) Math.round(value);
	 *
	 * @param value
	 * @return Returns the rounded int of the value.
	 */
	public static int saveRound(final double value) {
		return (int) Math.round(value);
	}

	/**
	 * Calculates real world coordinates into relative coordinates in a viewport.
	 *
	 * @param realWorld
	 *        Real world coordinates.
	 * @param viewport
	 *        Viewport coordinates.
	 * @return
	 */
	public static Point getRelativePointTranslation(final Point realWorld, final Rectangle viewport) {
		final Point renderPos = new Point(realWorld.x - viewport.getX(), realWorld.y - viewport.getY());
		return renderPos;
	}

	/**
	 * Calculates real world coordinates into relative coordinates in a viewport.
	 *
	 * @param realWorld
	 *        Real world coordinates.
	 * @param viewport
	 *        Viewport coordinates.
	 * @return
	 */
	public static Point getRelativePointTranslation(final Entity entity, final Rectangle viewport) {
		return Math2D.getRelativePointTranslation(entity.getPosition(), viewport);
	}

	// public static int getDirection(double yourDegrees, double wantedDegrees)
	// {
	// double half = yourDegrees - wantedDegrees;
	// int sign;
	//
	// if (half < 0)
	// half = 360 - half;
	//
	// if (half > 180)
	// sign = 1;
	// else
	// sign = -1;
	//
	// return sign;
	// }

	public static Point getCenterOfScreen(final double width, final double height) {

		final double sx = Toolkit.getDefaultToolkit()
		    .getScreenSize().width;
		final double sy = Toolkit.getDefaultToolkit()
		    .getScreenSize().height;
		return new Point((sx - width) / 2d, (sy - height) / 2d);
	}

	public static Point getMittelpunkt(final Point start, final Point ende) {
		return new Point((start.x + ende.x) / 2.0d, (start.y + ende.y) / 2.0d);
	}

	public static boolean isInCircle(final Point point, final Point mcircle, final double radius) {

		if (Math.pow(point.x - mcircle.x, 2.0d) + Math.pow(point.y - mcircle.y, 2.0d) <= Math.pow(radius, 2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Prüft, ob ein Punkt in einem Rechteck liegt. Dabei wird das Rechteck durch den Punkt oben links und unten rechts
	 * beschrieben.
	 * 
	 * @param point
	 * @param rectUpperLeft
	 * @param rectBottomRight
	 * @return
	 */
	public static boolean isInRect(final Point point, Point rectUpperLeft, Point rectBottomRight) {

		if ((point.x >= rectUpperLeft.x && point.x <= rectBottomRight.x)
		    && (point.y >= rectUpperLeft.y && point.y <= rectBottomRight.y)) {
			return true;
		}

		final Point tmp = rectUpperLeft;
		rectUpperLeft = rectBottomRight;
		rectBottomRight = tmp;

		if ((point.x >= rectUpperLeft.x && point.x <= rectBottomRight.x)
		    && (point.y >= rectUpperLeft.y && point.y <= rectBottomRight.y)) {
			return true;
		}

		return false;
	}

	public static double getSteigung(final Point start, final Point ende) {

		double result;
		result = (ende.y - start.y) / (ende.x - start.x);
		return result;
	}

	public static Point getCircle(final Point start, final double radius, final double winkel) {
		// winkel -= 90;

		final double w = winkel * (Math.PI / 180.d);

		final double x = (start.getX() + (Math.cos(w) * radius));
		final double y = (start.getY() + (Math.sin(w) * radius));

		return new Point(x, y);

	}

	public static double getAngle(final Point start, final Point end) {

		double w = (Math.atan2((end.y - start.y), (end.x - start.x))) * (180.d / Math.PI);

		// w = w + 90;
		if (w <= 0.0) {
			w = 360.0 + w;
		}

		// //BEDENKLICH
		if (w >= 360.0) {
			w = w - 360.0;
		}

		return w;

	}

	public static double getEntfernung(final Point start, final Point ende) {
		final double e = Math.sqrt(Math.pow((start.x - ende.x), 2) + Math.pow((start.y - ende.y), 2));
		return e;
	}

	public static Point getPointMinX(final List<Point> points) {
		double minX = Double.MAX_VALUE;
		Point pointMinX = points.get(0);
		for (int i = 0; i < points.size(); i++) {
			final Point aktPoint = points.get(i);
			final double x = aktPoint.x;

			if (x <= minX) {
				minX = x;
				pointMinX = aktPoint;
			}
		}
		return new Point(pointMinX.x, pointMinX.y);
	}

	public static Point getPointMaxX(final List<Point> points) {
		double maxX = Double.MIN_VALUE;
		Point pointMaxX = points.get(0);
		for (int i = 0; i < points.size(); i++) {
			final Point aktPoint = points.get(i);
			final double x = aktPoint.x;
			if (x > maxX) {
				maxX = x;
				pointMaxX = aktPoint;
			}
		}
		return new Point(pointMaxX.x, pointMaxX.y);
	}

	public static Point getPointMinY(final List<Point> points) {
		double minY = Double.MAX_VALUE;
		Point anchorNextToY = points.get(0);
		for (int i = 0; i < points.size(); i++) {
			final Point aktPoint = points.get(i);
			final double y = aktPoint.y;

			if (y < minY) {
				minY = y;
				anchorNextToY = aktPoint;
			}
		}
		return new Point(anchorNextToY.x, anchorNextToY.y);
	}

	public static Point getPointMaxY(final List<Point> points) {
		double maxY = Double.MIN_VALUE;
		Point anchorMaxDistToY = points.get(0);
		for (int i = 0; i < points.size(); i++) {
			final Point aktPoint = points.get(i);
			final double y = aktPoint.y;
			if (y > maxY) {
				maxY = y;
				anchorMaxDistToY = aktPoint;
			}
		}
		return new Point(anchorMaxDistToY.x, anchorMaxDistToY.y);
	}

	public static EntityPoint getPointNextTo(final EntityPoint nextTo, final List<EntityPoint> points) {
		double laenge = Double.MAX_VALUE;
		EntityPoint closest = null;

		for (int i = 0; i < points.size(); i++) {
			final EntityPoint point = points.get(i);
			final double newLaenge = Math2D.getEntfernung(nextTo.getCoordinates(), point.getCoordinates());
			if (newLaenge < laenge) {
				laenge = newLaenge;
				closest = point;
			}
		}

		return closest;
	}

	public static Point getPointNextTo(final Point nextTo, final List<Point> points) {
		double laenge = Double.MAX_VALUE;
		Point closest = null;

		for (int i = 0; i < points.size(); i++) {
			final Point point = points.get(i);
			final double newLaenge = Math2D.getEntfernung(nextTo, point);
			if (newLaenge < laenge) {
				laenge = newLaenge;
				closest = point;
			}
		}

		return closest;
	}

	public static Point getPointNextToEntityPoints(final Point nextTo, final List<EntityPoint> points) {
		double laenge = Double.MAX_VALUE;
		Point closest = null;
		for (int i = 0; i < points.size(); i++) {
			final EntityPoint ePoint = points.get(i);
			final Point point = ePoint.getCoordinates();
			final double newLaenge = Math2D.getEntfernung(nextTo, point);
			if (newLaenge < laenge) {
				laenge = newLaenge;
				closest = point;
			}
		}
		return closest;
	}

	public static Rectangle getHuellRect(final List<Point> punktliste) {
		final Point x1 = new Point(Math2D.getPointMinX(punktliste).x, Math2D.getPointMinY(punktliste).y);
		final Point x3 = new Point(Math2D.getPointMaxX(punktliste).x, Math2D.getPointMaxY(punktliste).y);
		final Rectangle rect = new Rectangle(x1, new Dimension(x3.x - x1.x, x3.y - x1.y));
		return rect;
	}

	/**
	 * Sorts the specified list of points so that the points connected with lines in
	 * the order the list specified result in a polygon with non crossing lines.
	 *
	 * @param points
	 */
	public static void sortEntityPoints(List<EntityPoint> points) {
		if (points.size() == 0)
			return;

		for (int i = 1; i < points.size(); i++) {
			EntityPoint current = points.get(i - 1);
			List<EntityPoint> subListView = points.subList(i, points.size());
			EntityPoint nextPoint = getPointNextTo(current, subListView);
			points.remove(nextPoint);
			points.add(i, nextPoint);
		}
	}

	public static double[] pqFormula(double p, double q) {
		double diskriminante;
		diskriminante = Math.pow(p / 2.0, 2) - q;
		if (diskriminante >= 0) {
			double x1, x2;
			x1 = (-(p / 2.0)) + Math.sqrt(diskriminante);
			x2 = (-(p / 2.0)) - Math.sqrt(diskriminante);
			if (x1 == x2) {
				return new double[] {
				    x2
				};
			} else {
				return new double[] {
				    x1, x2
				};// Was x1, x2
			}
		} else {
			return new double[] {};
		}
	}

	/**
	 * Compares two doubles with a tolerance.
	 * 
	 * @param d1 double 1
	 * @param d2 double 2
	 * @return Returns <code>true</code> if the doubles equal with a tolerance of 0.000001d. Otherwise <code>false</code>
	 *         is returned.
	 */
	public static boolean compareDouble(double d1, double d2) {
		double epsilon = 0.000001d;
		return Math.abs(d1 - d2) < epsilon;
	}

	/**
	 * Normalizes the specified angle to a value between 0 and 360 degrees.
	 * 
	 * @param d The angle to normalize
	 * @return The normalized angle.
	 */
	public static double normalizeAngle(double d) {
		if (d > 360) {
			return (d % 360);
		} else if (d < 0) {
			return d + Math.ceil(Math.abs(d) / 360.0) * 360.0;
		}
		return d;
	}

	/**
	 * @param degrees Degrees.
	 * @return Returns radians.
	 */
	public static float toRadians(double degrees) {
		return (float) Math.toRadians(degrees);
	}

}

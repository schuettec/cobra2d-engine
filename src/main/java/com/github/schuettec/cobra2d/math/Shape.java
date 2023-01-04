package com.github.schuettec.cobra2d.math;

import java.io.Serializable;
import java.util.List;

/**
 * This interface describes methods a geometry must provide.
 *
 * @author Chris
 *
 */
public interface Shape extends Serializable {

	/**
	 * @return Returns <code>true</code> if the shape is point based, otherwise <code>false</code>
	 *         is returned.
	 * 
	 */
	public boolean isPointBased();

	/**
	 * @return If this shape is point based, this method returns the entity points. Otherwise an empty list should be
	 *         returned.
	 */
	public List<Point> getPoints();

	/**
	 * Rotates this {@link Shape} and returns itself to support a fluent API
	 * style of method calls. The default rotation point of this {@link Shape}
	 * is used
	 *
	 * @param degrees
	 *        The amount of degrees to rotate.
	 * @return Returns this {@link Shape} to support a fluent API style of
	 *         method calls
	 */
	public Shape rotate(double degrees);

	/**
	 * Translates the shape by adding the specified translation.
	 *
	 * @param translation
	 *        The point to add as translation.
	 * @return Returns this {@link Shape} to support a fluent API style of
	 *         method calls
	 */
	public Shape translate(Point translation);

	/**
	 * Scales this {@link Shape} by the specfied scale factor.
	 *
	 * @param scaleFactor
	 *        The scale factor.
	 * @return Returns this {@link Shape} to support a fluent API style of
	 *         method calls
	 */
	public Shape scale(double scaleFactor);
}

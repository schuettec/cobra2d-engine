package com.github.schuettec.cobra2d.math;

import java.util.Collections;
import java.util.List;

/**
 * This class defines a {@link Shape} representing a circle.
 *
 * @author Chris
 *
 */
public class Circle implements Shape, Cloneable {

  protected Point position;

  protected double radius;

  public Circle() {
    this(new Point(), 0);
  }

  public Circle(Point center, double radius) {
    super();
    this.position = center.clone();
    this.radius = radius;
  }

  @Override
  public Circle clone() {
    return new Circle(position.clone(), radius);
  }

  @Override
  public Circle rotate(double degrees) {
    return this;
  }

  @Override
  public Circle translate(Point translation) {
    position.translate(translation);
    return this;
  }

  @Override
  public Circle scale(double scaleFactor) {
    this.radius *= scaleFactor;
    return this;
  }

  public Point getPosition() {
    return position.clone();
  }

  public Circle setPosition(Point position) {
    this.position.setLocation(position);
    return this;
  }

  public double getRadius() {
    return radius;
  }

  public Circle setRadius(double radius) {
    this.radius = radius;
    return this;
  }

  @Override
  public boolean isPointBased() {
    return false;
  }

  @Override
  public List<Point> getPoints() {
    return Collections.emptyList();
  }

  public boolean contains(Point mousePoint) {
    return Math2D.isInCircle(mousePoint, getPosition(), radius);
  }

}

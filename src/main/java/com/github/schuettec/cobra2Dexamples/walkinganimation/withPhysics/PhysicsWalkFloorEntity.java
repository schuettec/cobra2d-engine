package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import java.util.List;
import java.util.Optional;

import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.EntityPoint;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Polygon;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class PhysicsWalkFloorEntity extends PhysicsObstacleEntity implements FloorStairSkill {

	private static final double PSEUDO_WIDTH = 5;
	private static final double STEP_UP_OFFSET = 15;

	public PhysicsWalkFloorEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
	}

	public Line getWalkLine() {
		Dimension collisionShapeDimension = getCollisionShapeDimension();
		double width = collisionShapeDimension.getWidth();
		double height = collisionShapeDimension.getHeight();
		double widthHalf = width / 2.;
		double heightHalf = height / 2.;
		Point s = new Point(getPosition().x - widthHalf, getPosition().y + heightHalf);
		Point e = new Point(getPosition().x + widthHalf, getPosition().y + heightHalf);
		return new Line(s, e);
	}

	public Optional<Point> getNextStep(double xPos) {
		Line walkLine = getWalkLine();
		if (walkLine.isDefined(new Point(xPos, walkLine.getX2().y))) {
			double value = walkLine.getValue(xPos);
			return Optional.of(new Point(value, walkLine.getX2().y));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		super.render(renderer, position);

		Polygon leftSensor = getStepUpSensorLeft().clone();
		PolygonRenderable.renderPolygon(leftSensor, renderer, position, Color.GREEN);

		Polygon rightSensor = getStepUpSensorRight().clone();
		PolygonRenderable.renderPolygon(rightSensor, renderer, position, Color.GREEN);

		Point stepUpPointLeft = getStepUpPointLeft().clone()
		    .translate(position);
		renderer.drawCircle(stepUpPointLeft.getFloatX(), stepUpPointLeft.getFloatY(), 4, Color.GREEN);

		Point stepUpPointRight = getStepUpPointRight().clone()
		    .translate(position);
		renderer.drawCircle(stepUpPointRight.getFloatX(), stepUpPointRight.getFloatY(), 4, Color.GREEN);
	}

	@Override
	public int getLayer() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Point getStepUpPointLeft() {
		Point position = getPosition().clone();
		Dimension dimension = getDimension(true, false);
		return position.translate(-(dimension.getWidth() / 2d) + STEP_UP_OFFSET, dimension.getHeight() / 2d);
	}

	@Override
	public Point getStepUpPointRight() {
		Point position = getPosition().clone();
		Dimension dimension = getDimension(true, false);
		return position.translate((dimension.getWidth() / 2d) - STEP_UP_OFFSET, dimension.getHeight() / 2d);
	}

	@Override
	public Polygon getStepUpSensorLeft() {
		Dimension dimension = getDimension(true, false);
		List<EntityPoint> rectangle = Math2D.getRectangle(PSEUDO_WIDTH, PSEUDO_WIDTH);
		Polygon p = new Polygon(rectangle);
		return p.translate(getPosition())
		    .translate(new Point(-dimension.getWidth() / 2d, dimension.getHeight() / 2d));
	}

	@Override
	public Polygon getStepUpSensorRight() {
		Dimension dimension = getDimension(true, false);
		List<EntityPoint> rectangle = Math2D.getRectangle(PSEUDO_WIDTH, PSEUDO_WIDTH);
		Polygon p = new Polygon(rectangle);
		return p.translate(getPosition())
		    .translate(new Point(dimension.getWidth() / 2d, dimension.getHeight() / 2d));
	}
}

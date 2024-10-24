package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import static java.util.Objects.isNull;

import com.github.schuettec.cobra2Dexamples.walkinganimation.WalkAnimationController;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Point;

public class StepUpAnimationControllerOLD extends WalkAnimationController {

	private Point targetPoint;
	private Point sourcePoint;

	public StepUpAnimationControllerOLD(double maxStep, double legLength, double stepSize, double stepHeight,
	    double stepHeightFast, double crouchHeight) {
		super(maxStep, legLength, stepSize, stepHeight, stepHeightFast, crouchHeight);

	}

	public Point getSourcePoint() {
		return targetPoint;
	}

	public void setSourcePoint(Point sourcePoint) {
		this.sourcePoint = sourcePoint;
	}

	public Point getTargetPoint() {
		return targetPoint;
	}

	public void setTargetPoint(Point targetPoint) {
		this.targetPoint = targetPoint;
	}

	@Override
	public AnimationResult calculateTargetByStep(Point worldCoordinates, double currentStep) {

		if (isNull(sourcePoint)) {
			throw new RuntimeException("Target step point is null!");
		}
		if (isNull(targetPoint)) {
			throw new RuntimeException("Source step point is null!");
		}

		Point targetPoint = this.targetPoint;

		Line interpolation = null;
		boolean mirrored = false;
		// interpolation = new Line(targetPoint, sourcePoint);
		if (liegtLinksVon(targetPoint, sourcePoint)) {
			System.out.println("LIEGT LINKS");
			targetPoint = spiegeln(targetPoint, sourcePoint);
			mirrored = true;
		}

		interpolation = new Line(sourcePoint, targetPoint);
		double minX = interpolation.getMinX();
		double maxX = interpolation.getMaxX();

		// Umrechnen von Steps auf Line-Koordinatensystem
		double x = minX + currentStep / maxStep * (maxX - minX);

		double y = interpolation.getValue(x);

		return new AnimationResult(worldCoordinates, new Point(x, y), null);
	}

	public static Point spiegeln(Point p, Point s) {
		double xGespiegelt = 2d * s.x - p.x;
		return new Point(xGespiegelt, p.y);
	}

	public static boolean liegtLinksVon(Point p1, Point p2) {
		// Prüfen, ob p1 links von p2 liegt (d.h. ob die x-Koordinate
		// kleiner ist)
		return p1.x < p2.x;
	}
}

package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import static java.util.Objects.isNull;

import com.github.schuettec.cobra2Dexamples.walkinganimation.WalkAnimationController;
import com.github.schuettec.cobra2d.math.Point;

public class LiftLegAnimationController extends WalkAnimationController {

	private Point targetPoint;
	private Point sourcePoint;

	public LiftLegAnimationController(double maxStep, double legLength, double stepSize, double stepHeight,
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

		return new AnimationResult(worldCoordinates, targetPoint, null);
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

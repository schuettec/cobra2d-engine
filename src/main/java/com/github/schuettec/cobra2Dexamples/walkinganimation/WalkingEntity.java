package com.github.schuettec.cobra2Dexamples.walkinganimation;

import com.github.schuettec.cobra2Dexamples.moveableShapes.MoveableCircleEntity;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class WalkingEntity extends MoveableCircleEntity {

	private int currentStep = 0;
	private int maxStep = 1000;
	private double radius;

	public WalkingEntity(Point worldCoordinates, double radius, boolean playerControlled) {
		super(worldCoordinates, radius, playerControlled);
		this.radius = radius;
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		super.render(renderer, position);

		currentStep = (currentStep + 1) % maxStep;

		Point o1S = getPosition().clone().translate(position);
		Point o1E = Math2D.getCircle(o1S, radius, -45);

		renderer.drawLine(o1S.getRoundX(), o1S.getRoundY(), o1E.getRoundX(), o1E.getRoundY(), getDrawColor());
	}
}

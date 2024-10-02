package com.github.schuettec.cobra2Dexamples.walkinganimation;

import com.github.schuettec.cobra2Dexamples.moveableShapes.MoveableCircleEntity;
import com.github.schuettec.cobra2Dexamples.walkinganimation.Leg.LegBuilder;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class WalkingEntity extends MoveableCircleEntity {

	private static final int MAX_STEP = 50;

	private int currentStep = 0;

	private Leg leg1;

	private Leg leg2;

	public WalkingEntity(Point worldCoordinates, double radius, boolean playerControlled) {
		super(worldCoordinates, radius, playerControlled);
		LegBuilder builder = Leg.newLeg()
		    .setLegLength(radius)
		    .setWinkelVorschwungOberschenkel(60)
		    .setWinkelRückschwungOberschenkel(40)
		    .setMaxStep(MAX_STEP);
		this.leg1 = builder.build();
		this.leg2 = builder.build();
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		super.render(renderer, position);

		leg1.calculateStep(position, currentStep)
		    .render(renderer, position);
		leg2.calculateStep(position, currentStep + (MAX_STEP / 2))
		    .render(renderer, position);

		currentStep = (currentStep + 1) % MAX_STEP;

	}

}

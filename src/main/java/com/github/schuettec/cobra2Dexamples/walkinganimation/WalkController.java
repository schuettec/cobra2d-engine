package com.github.schuettec.cobra2Dexamples.walkinganimation;

import java.util.Set;

import com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics.FloorStairSkill;
import com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics.PhysicsWalkingEntity;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.world.Collision;
import com.github.schuettec.cobra2d.world.CollisionMap;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class WalkController {

	/**
	 * Offset prevents sensor lines parallel to y-axis to touch the bottom floor.
	 */
	private static final double OFFSET_Y = 1;

	public enum WalkMode {
		WALK,
		STEP_UP_LEFT,
		STEP_UP_RIGHT;
	}

	protected Point stepTarget;
	protected WalkMode mode;

	/**
	 * In case of mode {@link WalkMode#STEP_UP}
	 */
	FloorStairSkill leftFloor;
	FloorStairSkill rightFloor;

	private Point nextStepPointLeft;

	private Point nextStepPointRight;

	private Line[] sensorLines;

	public void updateWalk(PhysicsWalkingEntity entity, WorldAccess worldAccess) {
		mode = WalkMode.WALK;

		this.sensorLines = createSensorLines(entity.getPosition(), entity.getDimension(true, false));

		Set<FloorStairSkill> floorOrStairEntities = worldAccess.getEntititesBySkill(FloorStairSkill.class);
		CollisionMap collisionLeftSensor = worldAccess.getCollisions()
		    .detectCollision(entity, (e) -> sensorLines[0], floorOrStairEntities, FloorStairSkill::getStepUpSensorRight,
		        true, false, false);
		CollisionMap collisionRightSensor = worldAccess.getCollisions()
		    .detectCollision(entity, (e) -> sensorLines[1], floorOrStairEntities, FloorStairSkill::getStepUpSensorLeft,
		        true, false, false);

		if (collisionLeftSensor.hasCollision(entity)) {
			// Step Up left
			Collision collision = collisionLeftSensor.getCollisions()
			    .getFirst();
			leftFloor = (FloorStairSkill) collision.getOpponent();
			// Attention: The step up point from entity is right, the nextStepPoint is called left, because the latter is from
			// the walking entities perspective and the former of floorStair-entity.
			nextStepPointLeft = leftFloor.getStepUpPointRight();
			mode = WalkMode.STEP_UP_LEFT;
		} else if (collisionRightSensor.hasCollision(entity)) {
			// Step Up right
			Collision collision = collisionRightSensor.getCollisions()
			    .getFirst();
			rightFloor = (FloorStairSkill) collision.getOpponent();
			// Attention: The step up point from entity is right, the nextStepPoint is called left, because the latter is from
			// the walking entities perspective and the former of floorStair-entity.
			nextStepPointRight = rightFloor.getStepUpPointLeft();
			mode = WalkMode.STEP_UP_RIGHT;
		}
	}

	/**
	 * Creates the sensor lines to detect steps more precisely
	 * 
	 * @param position The entity position in world coordinates
	 * @param dimension The entity's dimension
	 * @return Returns the array of three sensor lines in order: bottom, left, right.
	 */
	private Line[] createSensorLines(Point position, Dimension dimension) {
		// The offset is used to create an inset of the sensor lines relative to the collision shape

		Point leftStart = new Point(position.getX() - (dimension.getWidth() / 2d), position.getFloatY());
		Point leftEnd = new Point(position.getX() - (dimension.getWidth() / 2d),
		    // The offset is multiplied by 2 because the bottom floor uses 1 * Offset
		    position.getFloatY() - (dimension.getHeight() / 2d) + OFFSET_Y);
		Line left = new Line(leftStart, leftEnd);

		Point rightStart = new Point(position.getX() + (dimension.getWidth() / 2d), position.getFloatY());
		// The offset is multiplied by 2 because the bottom floor uses 1 * Offset
		Point rightEnd = new Point(position.getX() + (dimension.getWidth() / 2d),
		    position.getFloatY() - (dimension.getHeight() / 2d) + OFFSET_Y);
		Line right = new Line(rightStart, rightEnd);

		return new Line[] {
		    left, right
		};
	}

	public Point getStepTarget() {
		return stepTarget;
	}

	public WalkMode getMode() {
		return mode;
	}

	public FloorStairSkill getLeftFloor() {
		return leftFloor;
	}

	public FloorStairSkill getRightFloor() {
		return rightFloor;
	}

	public Point getNextStepPoint() {
		if (isStepUpLeft()) {
			return nextStepPointLeft;
		} else if (isStepUpRight()) {
			return nextStepPointRight;
		} else {
			throw new IllegalStateException("The walk animation is not in step mode.");
		}
	}

	public Point getNextStepPointLeft() {
		return nextStepPointLeft;
	}

	public Point getNextStepPointRight() {
		return nextStepPointRight;
	}

	public boolean isStepUpLeft() {
		return WalkMode.STEP_UP_LEFT.equals(mode);
	}

	public boolean isStepUpRight() {
		return WalkMode.STEP_UP_RIGHT.equals(mode);
	}

	public boolean isWalking() {
		return WalkMode.WALK.equals(mode);
	}

	public Line[] getSensorLines() {
		return sensorLines;
	}

	@Override
	public String toString() {
		return "WalkController [mode=" + mode + "]";
	}

}

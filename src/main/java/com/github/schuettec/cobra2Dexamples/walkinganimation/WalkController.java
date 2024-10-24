package com.github.schuettec.cobra2Dexamples.walkinganimation;

import static java.util.Objects.nonNull;

import java.util.Optional;
import java.util.function.Predicate;

import com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics.PhysicsWalkFloorEntity;
import com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics.PhysicsWalkingEntity;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.world.Collision;
import com.github.schuettec.cobra2d.world.CollisionDetail;
import com.github.schuettec.cobra2d.world.CollisionMap;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class WalkController {

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
	PhysicsWalkFloorEntity leftFloor;
	PhysicsWalkFloorEntity rightFloor;
	PhysicsWalkFloorEntity bottomFloor;

	private Point nextStepPointLeft;

	private Point nextStepPointRight;

	public void updateWalk(PhysicsWalkingEntity entity, WorldAccess worldAccess) {
		mode = null;

		CollisionMap collisionMap = worldAccess.getCollisions()
		    .detectCollision(entity, HasCollisionShape::getCollisionShapeInWorldCoordinates,
		        worldAccess.getObstaclesExcept(entity), HasCollisionShape::getCollisionShapeInWorldCoordinates, true, true,
		        false);

		for (Collision c : collisionMap.getCollisions()) {
			if (c.getOpponent() instanceof PhysicsWalkFloorEntity) {
				PhysicsWalkFloorEntity opponent = (PhysicsWalkFloorEntity) c.getOpponent();
				Point oPosition = opponent.getPosition();

				Optional<CollisionDetail> lineParallelX = hasCollisionLineMatching(c, Line::isParallelX);
				Optional<CollisionDetail> lineParallelY = hasCollisionLineMatching(c, Line::isParallelY);
				// Here was also lineParallelX.isPresent() &&
				if (!hasCollisionLineMatching(c, Line::isParallelY).isPresent() && isBottom(entity, oPosition)) {
					// Walking on floor
					bottomFloor = opponent;
					mode = WalkMode.WALK;
					break;
				} else if (lineParallelY.isPresent() && isLeft(entity, oPosition)) {
					// Step Up left
					CollisionDetail detail = lineParallelY.get();
					nextStepPointLeft = detail.getIntersection();
					leftFloor = opponent;
					mode = WalkMode.STEP_UP_LEFT;
					break;
				} else if (lineParallelY.isPresent() && isRight(entity, oPosition)) {
					// Step Up right
					CollisionDetail detail = lineParallelY.get();
					nextStepPointRight = detail.getIntersection();
					rightFloor = opponent;
					mode = WalkMode.STEP_UP_RIGHT;
					break;
				}
			}
		}
	}

	private Optional<CollisionDetail> hasCollisionLineMatching(Collision c, Predicate<Line> predicate) {
		return c.getCollisionDetails()
		    .stream()
		    .filter(detail -> {
			    Line entityLine = detail.getEntityLine();
			    if (nonNull(entityLine)) {
				    if (predicate.test(entityLine)) {
					    return true;
				    } else {
					    return false;
				    }
			    } else {
				    return false;
			    }
		    })
		    .findFirst();
	}

	private boolean isLeft(PhysicsWalkingEntity entity, Point oPosition) {
		// opponent position must be lover than entity.
		double myX = entity.getPosition().x;
		if (oPosition.x <= myX) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isRight(PhysicsWalkingEntity entity, Point oPosition) {
		double myX = entity.getPosition().x;
		if (oPosition.x >= myX) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isBottom(PhysicsWalkingEntity entity, Point oPosition) {
		double myY = entity.getPosition().y;
		double myHeight = entity.getDimension(false, false)
		    .getHeight();
		if (oPosition.y <= myY + myHeight) {
			return true;
		} else {
			return false;
		}
	}

	public Point getStepTarget() {
		return stepTarget;
	}

	public WalkMode getMode() {
		return mode;
	}

	public PhysicsWalkFloorEntity getLeftFloor() {
		return leftFloor;
	}

	public PhysicsWalkFloorEntity getRightFloor() {
		return rightFloor;
	}

	public PhysicsWalkFloorEntity getBottomFloor() {
		return bottomFloor;
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

	@Override
	public String toString() {
		return "WalkController [mode=" + mode + "]";
	}

}

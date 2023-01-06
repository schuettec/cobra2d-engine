package com.github.schuettec.cobra2d.world;

import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Point;

/**
 * Contains detailed information of the collision detection.
 */
public class CollisionDetail {

	private Point intersection;
	private Line entityLine;
	private Line opponentLine;
	private boolean pointBased;

	CollisionDetail(Point intersection, Line entityLine, Line opponentLine) {
		this.intersection = intersection;
		this.entityLine = entityLine;
		this.opponentLine = opponentLine;
		this.pointBased = true;
	}

	CollisionDetail(Point intersection) {
		this.intersection = intersection;
		this.pointBased = false;
	}

	public static CollisionDetail ofLineBased(Point intersection, Line entityLine, Line opponentLine) {
		return new CollisionDetail(intersection, entityLine, opponentLine);
	}

	public static CollisionDetail ofNonLineBased(Point intersection) {
		return new CollisionDetail(intersection);
	}

	public Point getIntersection() {
		return intersection;
	}

	public Line getEntityLine() {
		return entityLine;
	}

	public Line getOpponentLine() {
		return opponentLine;
	}

	boolean isPointBased() {
		return pointBased;
	}

}

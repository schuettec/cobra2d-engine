package com.github.schuettec.cobra2d.entity.skills.placement;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;

/**
 * Class that simplifies placement of entities with a collision shape.
 */
public interface Placeable extends HasCollisionShape {

	class PlaceableCreator {
		Supplier<HasCollisionShape> creator;
		Placeable placeable;
		List<HasCollisionShape> created = new LinkedList<>();

		PlaceableCreator(Supplier<HasCollisionShape> creator, Placeable placeable) {
			super();
			this.creator = creator;
			this.placeable = placeable;
		}

		private HasCollisionShape create() {
			HasCollisionShape creat = creator.get();
			created.add(creat);
			return creat;
		}

		public PlaceableCreator placeNorthOf() {
			placeable.place(create(), north());
			return this;
		}

		public PlaceableCreator placeSouthOf() {
			placeable.place(create(), south());
			return this;
		}

		public PlaceableCreator placeWesthOf() {
			placeable.place(create(), west());
			return this;
		}

		public PlaceableCreator placeEastOf() {
			placeable.place(create(), east());
			return this;
		}

		public List<HasCollisionShape> getCreated() {
			return created;
		}
	}

	default PlaceableCreator placeWithCreator(Supplier<HasCollisionShape> creator) {
		return new PlaceableCreator(creator, this);
	}

	default Placeable placeNorthOf(HasCollisionShape otherEntity) {
		place(otherEntity, north());
		return this;
	}

	default Placeable placeSouthOf(HasCollisionShape otherEntity) {
		place(otherEntity, south());
		return this;
	}

	default Placeable placeWesthOf(HasCollisionShape otherEntity) {
		place(otherEntity, west());
		return this;
	}

	default Placeable placeEastOf(HasCollisionShape otherEntity) {
		place(otherEntity, east());
		return this;
	}

	static Point north() {
		Point NORTH = new Point(0, 1);
		return NORTH;
	}

	static Point south() {
		Point NORTH = new Point(0, -1);
		return NORTH;
	}

	static Point west() {
		Point NORTH = new Point(-1, 0);
		return NORTH;
	}

	static Point east() {
		Point NORTH = new Point(1, 0);
		return NORTH;
	}

	default Placeable place(HasCollisionShape otherEntity, Point direction) {
		Point position = getPosition();
		Dimension halfDimension = getCollisionShapeDimension().half();
		Dimension halfDimensionOpponent = otherEntity.getCollisionShapeDimension()
		    .half();
		Point northTranslationThis = direction.clone()
		    .scale(halfDimension.getWidth(), halfDimension.getHeight());
		Point northTranslationOpponent = direction.clone()
		    .scale(halfDimensionOpponent.getWidth(), halfDimensionOpponent.getHeight());
		Point newPosition = position.clone()
		    .translate(northTranslationThis)
		    .translate(northTranslationOpponent);
		otherEntity.setPosition(newPosition);
		return this;
	}

}

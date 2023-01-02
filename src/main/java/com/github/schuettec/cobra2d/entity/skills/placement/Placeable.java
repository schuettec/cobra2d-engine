package com.github.schuettec.cobra2d.entity.skills.placement;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;

/**
 * Class that simplifies placement of entities with a collision shape.
 */
public interface Placeable extends HasCollisionShape {

	class PlaceableCreator {
		Supplier<Placeable> creator;
		Placeable placeable;
		List<Placeable> created = new LinkedList<>();
		private Cobra2DEngine engine;

		PlaceableCreator(Cobra2DEngine engine, Supplier<Placeable> creator, Placeable placeable) {
			super();
			this.engine = engine;
			this.creator = creator;
			this.placeable = placeable;
		}

		PlaceableCreator(Cobra2DEngine engine, Supplier<Placeable> creator, Placeable placeable, List<Placeable> created) {
			this.engine = engine;
			this.creator = creator;
			this.placeable = placeable;
			this.created = created;
		}

		private Placeable create() {
			Placeable create = creator.get();
			created.add(create);
			engine.addEntity(create);
			return create;
		}

		public PlaceableCreator placeNorthOf() {
			Placeable create = create();
			placeable.place(create, north());
			return new PlaceableCreator(engine, creator, create, created);
		}

		public PlaceableCreator placeSouthOf() {
			Placeable create = create();
			placeable.place(create, south());
			return new PlaceableCreator(engine, creator, create, created);
		}

		public PlaceableCreator placeWesthOf() {
			Placeable create = create();
			placeable.place(create, west());
			return new PlaceableCreator(engine, creator, create, created);
		}

		public PlaceableCreator placeEastOf() {
			Placeable create = create();
			placeable.place(create, east());
			return new PlaceableCreator(engine, creator, create, created);
		}

		public List<Placeable> getCreated() {
			return created;
		}
	}

	default PlaceableCreator placeWithCreator(Cobra2DEngine engine, Supplier<Placeable> creator) {
		return new PlaceableCreator(engine, creator, this);
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

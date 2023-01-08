package com.github.schuettec.cobra2Dexamples.networking.bullet;

import java.util.List;
import java.util.Optional;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.Obstacle;
import com.github.schuettec.cobra2d.entity.skills.SoundEffect;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.damage.Damageable;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Line;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.SoundAccess;
import com.github.schuettec.cobra2d.world.Collision;
import com.github.schuettec.cobra2d.world.CollisionMap;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class BulletEntity extends TexturedEntity implements Updatable, SoundEffect {

	private static final float BULLET_SPEED = 500f;

	double dealDamage = 10d;

	public BulletEntity(String textureId, Point worldCoordinates, Dimension intialDimension, int layer) {
		super(textureId, worldCoordinates, intialDimension, layer);
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		Point newPosition = Math2D.getCircle(getPosition(), BULLET_SPEED * deltaTime, getDegrees());
		Line bulletRay = new Line(worldCoordinates, newPosition);
		CollisionMap detectedCollisions = worldAccess.getCollisions().detectCollision(this, e -> bulletRay,
				worldAccess.getObstacles(), Obstacle::getCollisionShapeInWorldCoordinates, false, false, false);
		List<Collision> collisions = detectedCollisions.getCollision(this);
		if (!collisions.isEmpty()) {
			Collision collision = collisions.get(0);
			Entity opponent = collision.getOpponent();
			Damageable.dealDamage((Obstacle) opponent, dealDamage);
		}
	}

	@Override
	public Circle getSoundRangeInWorldCoordinates() {
		return null;
	}

	@Override
	public void updateSound(SoundAccess soundAccess, Optional<Entity> relativeTo) {

	}
}

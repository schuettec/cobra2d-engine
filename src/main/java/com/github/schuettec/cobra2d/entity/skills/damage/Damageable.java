package com.github.schuettec.cobra2d.entity.skills.damage;

import com.github.schuettec.cobra2d.entity.skills.Obstacle;

public interface Damageable extends Obstacle {

	/**
	 * Called by other entities to deal damage.
	 * 
	 * @param damage The damage value.
	 */
	public void dealDamage(double damage);

	public static void dealDamage(Obstacle obstacle, double damage) {
		if(obstacle instanceof Damageable) {
			Damageable damagable = (Damageable) obstacle;
			damagable.dealDamage(damage);
		}
	}
}

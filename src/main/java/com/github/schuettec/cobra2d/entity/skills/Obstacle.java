package com.github.schuettec.cobra2d.entity.skills;

/**
 * An obstacle is an entity with a collision shape that is used to detect collisions with other obstacles. For obstacles
 * a collision detection is calculated on each world update.
 * 
 * Other entities that are not obstacles can also have a collision shape. But this collision shape is used for other
 * purposes like detecting entities being captures by a camera's viewport.
 *
 */
public interface Obstacle extends HasCollisionShape {

}

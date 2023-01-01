package com.github.schuettec.cobra2d.entity.skills.physics;

import com.github.schuettec.cobra2d.entity.skills.Obstacle;

/**
 * A static body does not move under simulation and behaves as if it has infinite mass. Internally, Box2D stores zero
 * for the mass and the inverse mass. Static bodies can be moved manually by the user. A static body has zero velocity.
 * Static bodies do not collide with other static or kinematic bodies.
 * *
 */
public interface StaticBody extends PhysicBody, Obstacle {

}

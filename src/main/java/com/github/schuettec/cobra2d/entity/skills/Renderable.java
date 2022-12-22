package com.github.schuettec.cobra2d.entity.skills;

import java.awt.Graphics2D;

import com.github.schuettec.cobra2d.math.Point;

public interface Renderable extends HasCollisionShape {

	// If the entity should be visible in the scene, it should give a image
	// that will be rendered to the camera
	public abstract void render(final Graphics2D graphics, final Point position);

	// Layer
	public abstract int getLayer();
}

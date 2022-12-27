package com.github.schuettec.cobra2d.entity.skills;

import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public interface Renderable extends HasCollisionShape {

	// If the entity should be visible in the scene, it should give a image
	// that will be rendered to the camera
	public abstract void render(final RendererAccess renderer, final Point position);

	// Layer
	public abstract int getLayer();
}

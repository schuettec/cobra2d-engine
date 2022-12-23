package com.github.schuettec.cobra2d.entity.skills;

import java.util.List;

import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public interface Camera extends Updatable, HasCollisionShape {

	/**
	 * Called before {@link #render(RendererAccess, Map, List)} to set/draw the clipping mask of this camera.
	 * 
	 * @param renderer The {@link RendererAccess}.
	 */
	void renderClippingMask(final RendererAccess renderer);

	/**
	 * Renders the scene with the specified captured entities.
	 * 
	 * @param renderer The {@link RendererAccess}.
	 * @param map The current map.
	 * @param capturedEntities The entities that colliding the viewport shape of
	 *        this camera.
	 */
	void render(final RendererAccess renderer, final Map map, final List<Collision> capturedEntities);

}

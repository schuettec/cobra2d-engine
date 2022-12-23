package com.github.schuettec.cobra2d.entity.skills;

import java.util.List;

import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.renderer.common.RendererAccess;

public interface Camera extends Updatable, HasCollisionShape {

	/**
	 * Renders the scene with the specified captured entities.
	 * 
	 * @param capturedEntities The entities that colliding the viewport shape of
	 *        this camera.
	 */
	void render(final RendererAccess renderer, final Map map, final List<Collision> capturedEntities);

}

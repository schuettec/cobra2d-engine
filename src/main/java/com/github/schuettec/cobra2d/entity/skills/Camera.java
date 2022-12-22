package com.github.schuettec.cobra2d.entity.skills;

import java.awt.Graphics2D;
import java.util.List;

import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.map.Map;

public interface Camera extends Updatable, HasCollisionShape {

	/**
	 * Renders the scene with the specified captured entities.
	 * 
	 * @param capturedEntities The entities that colliding the viewport shape of
	 *        this camera.
	 */
	void render(final Graphics2D graphics, final Map map, final List<Collision> capturedEntities);

}

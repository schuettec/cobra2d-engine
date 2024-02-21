package com.github.schuettec.cobra2d.renderer;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.entity.skills.Skill;
import com.github.schuettec.cobra2d.world.WorldListener;

public interface Renderer extends WorldListener {

	public void initializeRenderer(final Cobra2DEngine engine) throws RendererException;

	public void start();

	public void finish();

	/**
	 * Returns the controller for a specific entity. In a single player environment
	 * most of the time there is one controller only.
	 *
	 * @param entity The controlled entity
	 * @return The controller controlling this entity. In a multiplayer environment
	 *         this is the controller of the specific client controlling this
	 *         entity.
	 */
	public Controller getControllerForEntity(Skill entity);

}

package com.github.schuettec.cobra2d.renderer;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.world.WorldListener;

public interface Renderer extends WorldListener {

	public void initializeRenderer(final Cobra2DEngine engine) throws RendererException;

	public void start();

	public void finish();

	public Controller getController();

}

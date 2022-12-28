package com.github.schuettec.cobra2d.renderer;

import java.net.URL;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;

public interface Renderer {

	public void addTexture(String textureId, URL url);

	public void initializeRenderer(final Cobra2DEngine engine, final int resolutionX, final int resolutionY,
	    final int bitDepth, final int refreshRate, final boolean fullscreen) throws RendererException;

	public void render();

	public void finish();

	public Controller getController();

	public RendererAccess getRendererAccess();

}

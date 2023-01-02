package com.github.schuettec.cobra2d.renderer;

import java.net.URL;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.world.WorldListener;

public interface Renderer extends WorldListener {

	public void addTexture(String textureId, URL url);

	public void initializeRenderer(final Cobra2DEngine engine, final int resolutionX, final int resolutionY,
	    final int bitDepth, final int refreshRate, final boolean fullscreen) throws RendererException;

	public void start();

	public void render();

	public void finish();

	public Controller getController();

	public RendererAccess getRendererAccess();

}

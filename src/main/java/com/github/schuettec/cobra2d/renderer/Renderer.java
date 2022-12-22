package com.github.schuettec.cobra2d.renderer;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.github.schuettec.cobra2d.engine.Cobra2DEngine;

public interface Renderer {

	public void initializeRenderer(final Cobra2DEngine engine, final int resolutionX, final int resolutionY,
	    final int bitDepth, final int refreshRate, final boolean fullscreen) throws RendererException;

	public void render();

	public void finish();

	public void addKeyListener(KeyListener singlePlayerController);

	public void removeKeyListener(KeyListener singlePlayerController);

	public void addMouseListener(MouseListener listener);

	public void removeMouseListener(MouseListener listener);

	public void addMouseMotionListener(MouseMotionListener listener);

	public void removeMouseMotionListener(MouseMotionListener listener);

}

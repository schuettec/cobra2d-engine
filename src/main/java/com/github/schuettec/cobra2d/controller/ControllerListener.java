package com.github.schuettec.cobra2d.controller;

public interface ControllerListener {
	public void keyTyped(final int keyCode);

	public void keyReleased(final int keyCode);

	public void keyPressed(final int keyCode);

	public void keyChar(final char c);

}

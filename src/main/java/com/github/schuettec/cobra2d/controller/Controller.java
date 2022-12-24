package com.github.schuettec.cobra2d.controller;

public interface Controller {

	/**
	 * Checks the state of the specified key code. Note: The key code is renderer-specific! For common keys use the
	 * provided methods like isUpKeyPressed().
	 * 
	 * @param keyCode The key code.
	 * @return Returns <code>true</code> if the specified key is pressed, otherwise <code>false</code> is returned.
	 */
	public abstract boolean isKeyPressed(final int keyCode);

	public boolean isEscapePressed();

	public boolean isUpKeyPressed();

	boolean isDownKeyPressed();

	boolean isLeftKeyPressed();

	boolean isRightKeyPressed();

	public boolean isWKeyPressed();

	public boolean isAKeyPressed();

	public boolean isSKeyPressed();

	public boolean isDKeyPressed();

	public boolean isSpaceKeyPressed();

	public boolean isCtrlLeftKeyPressed();

	public boolean isShiftLeftKeyPressed();

	public boolean isAltLeftKeyPressed();

	public boolean isPlusKeyPressed();

	public boolean isMinusKeyPressed();
}

package com.github.schuettec.cobra2d.renderer.j2d;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.github.schuettec.cobra2d.controller.Controller;

public class SwingController implements Controller, KeyListener {
	private final boolean[] pressedKeys = new boolean[1000];
	private boolean controlDown;
	private boolean shiftDown;
	private boolean altDown;

	@Override
	public void keyPressed(KeyEvent arg0) {
		pressedKeys[arg0.getKeyCode()] = true;
		this.controlDown = arg0.isControlDown();
		this.shiftDown = arg0.isShiftDown();
		this.altDown = arg0.isAltDown();
	}

	@Override
	public void keyReleased(final KeyEvent arg0) {
		pressedKeys[arg0.getKeyCode()] = false;
		this.controlDown = arg0.isControlDown();
		this.shiftDown = arg0.isShiftDown();
		this.altDown = arg0.isAltDown();
	}

	@Override
	public void keyTyped(final KeyEvent arg0) {

	}

	@Override
	public boolean isKeyPressed(int keyCode) {
		return this.pressedKeys[keyCode];
	}

	@Override
	public boolean isUpKeyPressed() {
		return isKeyPressed(KeyEvent.VK_UP);
	}

	@Override
	public boolean isDownKeyPressed() {
		return isKeyPressed(KeyEvent.VK_DOWN);
	}

	@Override
	public boolean isLeftKeyPressed() {
		return isKeyPressed(KeyEvent.VK_LEFT);
	}

	@Override
	public boolean isRightKeyPressed() {
		return isKeyPressed(KeyEvent.VK_RIGHT);
	}

	@Override
	public boolean isWKeyPressed() {
		return isKeyPressed(KeyEvent.VK_W);
	}

	@Override
	public boolean isAKeyPressed() {
		return isKeyPressed(KeyEvent.VK_A);
	}

	@Override
	public boolean isSKeyPressed() {
		return isKeyPressed(KeyEvent.VK_S);
	}

	@Override
	public boolean isDKeyPressed() {
		return isKeyPressed(KeyEvent.VK_D);
	}

	@Override
	public boolean isSpaceKeyPressed() {
		return isKeyPressed(KeyEvent.VK_SPACE);
	}

	@Override
	public boolean isCtrlLeftKeyPressed() {
		return controlDown;
	}

	@Override
	public boolean isShiftLeftKeyPressed() {
		return shiftDown;
	}

	@Override
	public boolean isAltLeftKeyPressed() {
		return altDown;
	}

	@Override
	public boolean isEscapePressed() {
		return isKeyPressed(KeyEvent.VK_ESCAPE);
	}

	@Override
	public boolean isPlusKeyPressed() {
		return isKeyPressed(KeyEvent.VK_PLUS);
	}

	@Override
	public boolean isMinusKeyPressed() {
		return isKeyPressed(KeyEvent.VK_MINUS);
	}

}

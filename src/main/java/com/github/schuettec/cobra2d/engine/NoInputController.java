package com.github.schuettec.cobra2d.engine;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.math.Point;

public class NoInputController implements Controller {

	@Override
	public boolean isKeyPressed(int keyCode) {
		return false;
	}

	@Override
	public boolean isEscapePressed() {
		return false;
	}

	@Override
	public boolean isUpKeyPressed() {
		return false;
	}

	@Override
	public boolean isDownKeyPressed() {
		return false;
	}

	@Override
	public boolean isLeftKeyPressed() {
		return false;
	}

	@Override
	public boolean isRightKeyPressed() {
		return false;
	}

	@Override
	public boolean isWKeyPressed() {
		return false;
	}

	@Override
	public boolean isAKeyPressed() {
		return false;
	}

	@Override
	public boolean isSKeyPressed() {
		return false;
	}

	@Override
	public boolean isDKeyPressed() {
		return false;
	}

	@Override
	public boolean isSpaceKeyPressed() {
		return false;
	}

	@Override
	public boolean isCtrlLeftKeyPressed() {
		return false;
	}

	@Override
	public boolean isShiftLeftKeyPressed() {
		return false;
	}

	@Override
	public boolean isAltLeftKeyPressed() {
		return false;
	}

	@Override
	public boolean isPlusKeyPressed() {
		return false;
	}

	@Override
	public boolean isMinusKeyPressed() {
		return false;
	}

	@Override
	public Point getMousePositionOnScreen() {
		return new Point();
	}

	@Override
	public double getMouseDegreesRelativeToScreenCenter() {
		return 0;
	}

	@Override
	public Point getMousePositionWorldCoordinates() {
		return new Point();
	}

}

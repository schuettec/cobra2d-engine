package com.github.schuettec.cobra2d.renderer.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.github.schuettec.cobra2d.controller.Controller;

public class LibGdxController implements Controller {

	@Override
	public boolean isKeyPressed(int keyCode) {
		return Gdx.input.isKeyPressed(keyCode);
	}

	@Override
	public boolean isUpKeyPressed() {
		return isKeyPressed(Keys.UP);
	}

	@Override
	public boolean isDownKeyPressed() {
		return isKeyPressed(Keys.DOWN);
	}

	@Override
	public boolean isLeftKeyPressed() {
		return isKeyPressed(Keys.LEFT);
	}

	@Override
	public boolean isRightKeyPressed() {
		return isKeyPressed(Keys.RIGHT);
	}

	@Override
	public boolean isWKeyPressed() {
		return isKeyPressed(Keys.W);
	}

	@Override
	public boolean isAKeyPressed() {
		return isKeyPressed(Keys.A);
	}

	@Override
	public boolean isSKeyPressed() {
		return isKeyPressed(Keys.S);
	}

	@Override
	public boolean isDKeyPressed() {
		return isKeyPressed(Keys.D);
	}

	@Override
	public boolean isSpaceKeyPressed() {
		return isKeyPressed(Keys.SPACE);
	}

	@Override
	public boolean isCtrlLeftKeyPressed() {
		return isKeyPressed(Keys.CONTROL_LEFT);
	}

	@Override
	public boolean isShiftLeftKeyPressed() {
		return isKeyPressed(Keys.SHIFT_LEFT);
	}

	@Override
	public boolean isAltLeftKeyPressed() {
		return isKeyPressed(Keys.ALT_LEFT);
	}
}

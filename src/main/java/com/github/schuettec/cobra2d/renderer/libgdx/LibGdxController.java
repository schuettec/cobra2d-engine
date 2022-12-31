package com.github.schuettec.cobra2d.renderer.libgdx;

import static com.github.schuettec.cobra2d.math.Math2D.getAngle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.camera.InputContext;
import com.github.schuettec.cobra2d.math.Point;

public class LibGdxController implements Controller {

	private InputContext cameraRelativeInput;

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

	public boolean isEscapePressed() {
		return isKeyPressed(Keys.ESCAPE);
	}

	@Override
	public boolean isPlusKeyPressed() {
		return isKeyPressed(Keys.NUMPAD_ADD);
	}

	@Override
	public boolean isMinusKeyPressed() {
		return isKeyPressed(Keys.NUMPAD_SUBTRACT);
	}

	@Override
	public Point getMousePositionOnScreen() {
		int y = Gdx.input.getY();
		return new Point(Gdx.input.getX(), Gdx.graphics.getHeight() - y);

	}

	@Override
	public double getMouseDegreesRelativeToScreenCenter() {
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		return getAngle(new Point(width / 2.0, height / 2.0), getMousePositionOnScreen());
	}

	@Override
	public void setCameraRelativeInput(InputContext input) {
		this.cameraRelativeInput = input;
	}

	@Override
	public InputContext getCameraRelativeInput() {
		return cameraRelativeInput;
	}
}

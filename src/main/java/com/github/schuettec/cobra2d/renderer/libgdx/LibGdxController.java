package com.github.schuettec.cobra2d.renderer.libgdx;

import static java.util.Objects.requireNonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.skills.Camera;
import com.github.schuettec.cobra2d.math.Point;

public class LibGdxController implements Controller {

  private Camera cameraForInput;

  public LibGdxController(Camera cameraForInput) {
    requireNonNull(cameraForInput,
        "If using a local renderer the camera must be set to translate input coordinates to world coordinates.");
    this.cameraForInput = cameraForInput;
  }

  @Override
  public boolean isKeyPressed(int keyCode) {
    return Gdx.input.isKeyPressed(keyCode);
  }

  @Override
  public boolean isJustTouched() {
    return Gdx.input.justTouched();
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

  @Override
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
    return new Point(Gdx.input.getX(),
        Gdx.graphics.getHeight() - y);

  }

  @Override
  public Point getMousePositionWorldCoordinates() {
    Point worldCoordinates = cameraForInput
        .screenToWorldCoordinates(getMousePositionOnScreen());
    return worldCoordinates;
  }

  @Override
  public boolean isCtrlRightKeyPressed() {
    return isKeyPressed(Keys.CONTROL_RIGHT);
  }

  @Override
  public boolean isShiftRightKeyPressed() {
    return isKeyPressed(Keys.SHIFT_RIGHT);
  }

  @Override
  public boolean isAltRightKeyPressed() {
    return isKeyPressed(Keys.ALT_RIGHT);
  }

}

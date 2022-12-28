package com.github.schuettec.cobra2d.entity.camera;

import static java.util.Objects.isNull;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.World;

public class BasicCircleMapCamera extends BasicCircleEntity implements AbstractCamera {

	private boolean playerControlled;
	private Point screenPosition;

	public BasicCircleMapCamera(Point worldCoordinates, double radius, boolean playerControlled) {
		super(worldCoordinates, radius);
		this.playerControlled = playerControlled;
		this.screenPosition = null;
	}

	public BasicCircleMapCamera(Point worldCoordinates, double radius, boolean playerControlled, Point screenPosition) {
		super(worldCoordinates, radius);
		this.playerControlled = playerControlled;
		this.screenPosition = new Point(0, 0);
	}

	public Point getScreenTranslation(RendererAccess renderer) {
		Point screenTranslation = getScreenPosition();
		if (isNull(screenTranslation)) {
			screenTranslation = new Point(renderer.getWidth() / 2.0, renderer.getHeight() / 2.0);
		}
		return screenTranslation;
	}

	@Override
	public void renderClippingMask(RendererAccess renderer) {
		Point screenTranslation = getScreenTranslation(renderer);
		renderer.fillCircle(screenTranslation.getRoundX(), screenTranslation.getRoundY(), Math2D.saveRound(getRadius() + 1),
		    Color.BLACK);
	}

	@Override
	public void drawCameraOutline(RendererAccess renderer) {
		// Draw camera outline.
		CircleRenderable.renderCircle(getCollisionShape(true, true, false), renderer, getScreenTranslation(renderer),
		    Color.GREEN);
	}

	@Override
	public void update(World map, float deltaTime, Controller controller) {
		if (playerControlled) {
			if (controller.isLeftKeyPressed()) {
				this.moveLeft();
			}
			if (controller.isRightKeyPressed()) {
				this.moveRight();
			}
			if (controller.isUpKeyPressed()) {
				this.moveUp();
			}
			if (controller.isDownKeyPressed()) {
				this.moveDown();
			}
		}
	}

	public void moveLeft() {
		this.translate(new Point(-5, 0));
	}

	public void moveRight() {
		this.translate(new Point(5, 0));
	}

	public void moveDown() {
		this.translate(new Point(0, -5));
	}

	public void moveUp() {
		this.translate(new Point(0, 5));
	}

	@Override
	public void setScreenPosition(Point point) {
		this.screenPosition = point.clone();
	}

	@Override
	public Point getScreenPosition() {
		return screenPosition;
	}

}

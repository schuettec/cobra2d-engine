package com.github.schuettec.cobra2d.entity.camera;

import static com.github.schuettec.cobra2d.math.Math2D.saveRound;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicCircleEntity;
import com.github.schuettec.cobra2d.entity.skills.CircleRenderable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

public class BasicCircleMapCamera extends BasicCircleEntity implements AbstractCamera {

	private boolean drawEntityPoints;
	private boolean drawCollisionShape;
	private boolean drawCameraOutline;

	private boolean centerOnScreen = true;

	private boolean playerControlled;

	private Point screenPosition;

	public BasicCircleMapCamera(Point worldCoordinates, double radius, boolean playerControlled) {
		super(worldCoordinates, radius);
		this.playerControlled = playerControlled;
		this.screenPosition = new Point(0, 0);
	}

	public BasicCircleMapCamera(Point worldCoordinates, double radius, boolean playerControlled, Point screenPosition) {
		super(worldCoordinates, radius);
		this.playerControlled = playerControlled;
		this.screenPosition = new Point(0, 0);
	}

	@Override
	public void renderClippingMask(RendererAccess renderer) {
		Point screenTranslation = getScreenPosition();
		renderer.fillCircle(screenTranslation.getRoundX(), screenTranslation.getRoundY(), Math2D.saveRound(getRadius() + 1),
		    Color.BLACK);
	}

	@Override
	public void drawCameraOutline(RendererAccess renderer) {
		// Draw camera outline.
		CircleRenderable.renderCircle(getCollisionShape(true, true, false), renderer, getScreenPosition(), Color.GREEN);
	}

	@Override
	public void update(Cobra2DWorld map, float deltaTime, Controller controller) {
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

	@Override
	public boolean isDrawEntityPoints() {
		return drawEntityPoints;
	}

	@Override
	public boolean isDrawCollisionShape() {
		return drawCollisionShape;
	}

	@Override
	public boolean isDrawCameraOutline() {
		return drawCameraOutline;
	}

	@Override
	public void setDrawEntityPoints(boolean drawEntityPoints) {
		this.drawEntityPoints = drawEntityPoints;
	}

	@Override
	public void setDrawCollisionShape(boolean drawCollisionShape) {
		this.drawCollisionShape = drawCollisionShape;
	}

	@Override
	public void setDrawCameraOutline(boolean drawCameraOutline) {
		this.drawCameraOutline = drawCameraOutline;
	}

	@Override
	public void setPlayerControlled(boolean playerControlled) {
		this.playerControlled = playerControlled;
	}

	@Override
	public Dimension getCollisionShapeDimension() {
		return getDimension(true, false);
	}

	@Override
	public Dimension getDimension(boolean applyScaling, boolean applyRotation) {
		Dimension dimension = new Dimension(saveRound(getRadius() * 2), saveRound(getRadius() * 2));
		return dimension;
	}

	@Override
	public boolean isCenterOnScreen() {
		return centerOnScreen;
	}

	@Override
	public void setCenterOnScreen(boolean centerOnScreen) {
		this.centerOnScreen = centerOnScreen;
	}

}

package com.github.schuettec.cobra2d.entity.camera;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.Collision;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.EntityPoint;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

public class BasicRectangleMapCamera extends BasicRectangleEntity implements AbstractCamera {

	private boolean drawEntityPoints;
	private boolean drawCollisionShape;
	private boolean drawCameraOutline;
	private boolean drawMouse;

	private boolean playerControlled;

	private Point screenPosition;
	private Point mousePosition;

	public BasicRectangleMapCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled) {
		super(worldCoordinates, dimension);
		this.playerControlled = playerControlled;
		this.screenPosition = null;
	}

	public BasicRectangleMapCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled,
	    Point screenPosition) {
		super(worldCoordinates, dimension);
		this.playerControlled = playerControlled;
		this.screenPosition = new Point(0, 0);
	}

	@Override
	public Point getScreenTranslation(RendererAccess renderer) {
		Point screenTranslation = screenPosition;
		if (isNull(screenPosition)) {
			screenTranslation = new Point(renderer.getWidth() / 2.0, renderer.getHeight() / 2.0);
		}

		return screenTranslation;
	}

	@Override
	public void render(RendererAccess renderer, Cobra2DWorld map, List<Collision> capturedEntities) {
		AbstractCamera.super.render(renderer, map, capturedEntities);
		drawMouse(renderer);
	}

	@Override
	public void renderClippingMask(RendererAccess renderer) {
		Point screenTranslation = getScreenTranslation(renderer);
		// The fill rectangle function is not center oriented. So we have to correct the position by half of dimension
		Point rectangleCorrection = new Point(-(getDimension().getWidth() / 2.0), -(getDimension().getHeight() / 2.0));
		screenTranslation.translate(rectangleCorrection);
		Dimension dimension = getDimension();
		renderer.fillRectangle((float) screenTranslation.getRoundX() - 1, (float) screenTranslation.getRoundY() - 1,
		    (float) dimension.getWidth() + 1, (float) dimension.getHeight() + 1, Color.BLACK);
	}

	@Override
	public void drawCameraOutline(RendererAccess renderer) {
		// Draw camera outline.
		PolygonRenderable.renderPolygon(getCollisionShape(true, true, false), renderer, getScreenTranslation(renderer),
		    Color.GREEN);
	}

	protected void drawMouse(RendererAccess renderer) {
		if (isDrawMouse() && nonNull(mousePosition)) {
			// Point screenTranslation = getScreenTranslation(renderer).scale(-1);
			Point toDraw = mousePosition;
			System.out.println(toDraw);
			drawPoint(renderer, toDraw, 5, Color.CORAL);
		}
	}

	@Override
	public void update(Cobra2DWorld map, float deltaTime, Controller controller) {
		this.mousePosition = controller.getMousePositionRelativeToInputCamera();

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

	public void setScreenPosition(Point point) {
		this.screenPosition = point.clone();
	}

	@Override
	public String toString() {

		String str = "BasicRectangleMapCamera [";
		List<EntityPoint> entityPoints = getCollisionShape(true, true, true).getEntityPoints();
		for (EntityPoint p : entityPoints) {
			str += p.getCoordinates()
			    .toString() + "\n";
		}
		return str + "]";
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

	public void setDrawEntityPoints(boolean drawEntityPoints) {
		this.drawEntityPoints = drawEntityPoints;
	}

	public void setDrawCollisionShape(boolean drawCollisionShape) {
		this.drawCollisionShape = drawCollisionShape;
	}

	public void setDrawCameraOutline(boolean drawCameraOutline) {
		this.drawCameraOutline = drawCameraOutline;
	}

	public void setPlayerControlled(boolean playerControlled) {
		this.playerControlled = playerControlled;
	}

	public boolean isDrawMouse() {
		return drawMouse;
	}

	public void setDrawMouse(boolean drawMouse) {
		this.drawMouse = drawMouse;
	}

}

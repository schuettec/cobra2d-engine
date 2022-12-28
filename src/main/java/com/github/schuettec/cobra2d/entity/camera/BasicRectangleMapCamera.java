package com.github.schuettec.cobra2d.entity.camera;

import static java.util.Objects.isNull;

import java.awt.Dimension;
import java.util.List;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.math.EntityPoint;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.World;

public class BasicRectangleMapCamera extends BasicRectangleEntity implements AbstractCamera {

	private boolean playerControlled;
	private Point screenPosition;

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
	public void renderClippingMask(RendererAccess renderer) {
		Point screenTranslation = getScreenTranslation(renderer);
		// The fill rectangle function is not center oriented. So we have to correct the position by half of dimension
		Point rectangleCorrection = new Point(-(getDimension().width / 2.0), -(getDimension().height / 2.0));
		screenTranslation.translate(rectangleCorrection);
		Dimension dimension = getDimension();
		renderer.fillRectangle(screenTranslation.getRoundX() - 1, screenTranslation.getRoundY() - 1, dimension.width + 1,
		    dimension.height + 1, Color.BLACK);
	}

	@Override
	public void drawCameraOutline(RendererAccess renderer) {
		// Draw camera outline.
		PolygonRenderable.renderPolygon(getCollisionShape(true, true, false), renderer, getScreenTranslation(renderer),
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

}

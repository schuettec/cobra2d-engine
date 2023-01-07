package com.github.schuettec.cobra2d.entity.camera;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Optional;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.Controllable;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.PolygonRenderable;
import com.github.schuettec.cobra2d.entity.skills.SoundEffect;
import com.github.schuettec.cobra2d.entity.skills.sound.SoundCamera;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.EntityPoint;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.math.Shape;
import com.github.schuettec.cobra2d.renderer.Color;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.SoundAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;
import com.github.schuettec.cobra2d.world.Collision;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class BasicRectangleMapCamera extends BasicRectangleEntity implements AbstractCamera, Controllable, SoundCamera {

	private boolean drawEntityPoints;
	private boolean drawCollisionShape;
	private boolean drawCameraOutline;
	private boolean drawMouse;

	private boolean centerOnScreen = true;

	private boolean playerControlled;

	private Point screenPosition;
	private Point mousePosition;

	private Entity followEntity;
	private String followEntityId;

	public BasicRectangleMapCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled) {
		super(worldCoordinates, dimension);
		this.playerControlled = playerControlled;
		this.screenPosition = new Point(0, 0);
	}

	public BasicRectangleMapCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled,
	    Point screenPosition) {
		super(worldCoordinates, dimension);
		this.playerControlled = playerControlled;
		this.screenPosition = new Point(0, 0);
	}

	@Override
	public void processControllerState(Controller controller) {
		this.mousePosition = controller.getMousePositionWorldCoordinates();
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

	@Override
	public void update(WorldAccess worldAccess, float deltaTime) {
		if (!playerControlled) {
			followOnDemand(worldAccess);
		}
	}

	@Override
	public void playback(SoundAccess soundAccess, Cobra2DWorld map, List<SoundEffect> capturedSoundEffects) {
		Optional<Entity> relativeTo = getFollowEntity(map.getWorldAccess());
		for (SoundEffect se : capturedSoundEffects) {
			se.updateSound(soundAccess, relativeTo);
		}
	}

	@Override
	public void renderClippingMask(RendererAccess renderer) {
		Point screenTranslation = getScreenPosition();
		// The fill rectangle function is not center oriented. So we have to correct the position by half of dimension
		Point rectangleCorrection = new Point(-(getCollisionShapeDimension().getWidth() / 2.0),
		    -(getCollisionShapeDimension().getHeight() / 2.0));
		screenTranslation.translate(rectangleCorrection);
		Dimension dimension = getCollisionShapeDimension();
		renderer.fillRectangle((float) screenTranslation.getRoundX() - 1, (float) screenTranslation.getRoundY() - 1,
		    (float) dimension.getWidth() + 1, (float) dimension.getHeight() + 1, Color.BLACK);
	}

	@Override
	public void render(RendererAccess renderer, Cobra2DWorld map, List<Collision> capturedEntities) {
		AbstractCamera.super.render(renderer, map, capturedEntities);
		drawMouse(renderer);
	}

	@Override
	public void drawCameraOutline(RendererAccess renderer) {
		// Draw camera outline.
		PolygonRenderable.renderPolygon(getCollisionShape(true, true, false), renderer, getScreenPosition(), Color.GREEN);
	}

	protected void drawMouse(RendererAccess renderer) {
		if (isDrawMouse() && nonNull(mousePosition)) {
			Point toDraw = worldToScreenCoordinates(mousePosition);
			drawPoint(renderer, toDraw, 5, Color.CORAL);
		}
	}

	private void followOnDemand(WorldAccess worldAccess) {
		getFollowEntity(worldAccess).ifPresent(follow -> this.setPosition(follow.getPosition()
		    .clone()));
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
		this.screenPosition = point;
	}

	@Override
	public Point getScreenPosition() {
		return screenPosition.clone();
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

	public boolean isDrawMouse() {
		return drawMouse;
	}

	public void setDrawMouse(boolean drawMouse) {
		this.drawMouse = drawMouse;
	}

	@Override
	public boolean isCenterOnScreen() {
		return centerOnScreen;
	}

	@Override
	public void setCenterOnScreen(boolean centerOnScreen) {
		this.centerOnScreen = centerOnScreen;
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
	public void follow(Entity followEntity) {
		this.followEntity = followEntity;
	}

	@Override
	public Shape getSoundRangeInWorlCoordinates() {
		return getCollisionShape(true, true, true);
	}

	@Override
	public void follow(String entityId) {
		this.followEntityId = entityId;
	}

	@Override
	public Entity getFollowEntity() {
		return followEntity;
	}

	@Override
	public String getFollowEntityId() {
		if (nonNull(followEntity)) {
			return followEntity.getId();
		} else {
			return followEntityId;
		}
	}

	@Override
	public void setFollowEntityId(String followEntityId) {
		this.followEntityId = followEntityId;
	}

	@Override
	public void setFollowEntity(Entity entity) {
		this.followEntity = entity;
	}

}

package com.github.schuettec.cobra2Dexamples.textureRendering;

import static com.github.schuettec.cobra2d.math.Math2D.normalizeAngle;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.entity.skills.state.EntityStateValue;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class TexturedEntity extends BasicRectangleEntity implements Renderable, Updatable {

	private static final long serialVersionUID = 1L;

	protected String textureId;

	@EntityStateValue
	protected int layer;

	protected boolean playerControlled;

	public TexturedEntity(String textureId, Point worldCoordinates, Dimension intialDimension, int layer,
	    boolean playerControlled) {
		super(worldCoordinates, intialDimension);
		this.layer = layer;
		this.textureId = textureId;
		this.playerControlled = playerControlled;
	}

	@Override
	public void initialize(RendererAccess rendererAccess) {
		Renderable.super.initialize(rendererAccess);
		createCollisionShape(rendererAccess);
	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {
		/*
		 * Without the next translation, the texture is rendered at the entities center point,
		 * so we have to translate by the half of the texture dimension
		 */
		Point texturePosition = renderer.getTexturePosition(getTextureId(), getPosition(), screenTranslation);
		renderer.drawTexture(getTextureId(), 1f, texturePosition.getRoundX(), texturePosition.getRoundY(),
		    (float) getDegrees());
	}

	protected void createCollisionShape(RendererAccess rendererAccess) {
		Dimension textureDimension = rendererAccess.getTextureDimension(getTextureId());
		createRectangleShape(textureDimension);
	}

	@Override
	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	@Override
	public void update(WorldAccess worldAccess, float deltaTime, Controller controller) {
		if (playerControlled) {
			if (controller.isLeftKeyPressed()) {
				this.rotateLeft();
			}
			if (controller.isRightKeyPressed()) {
				this.rotateRight();
			}
		}
	}

	public void rotateLeft() {
		setDegrees(normalizeAngle(getDegrees() - 5));
	}

	public void rotateRight() {
		setDegrees(normalizeAngle(getDegrees() + 5));
	}

	public String getTextureId() {
		return textureId;
	}

}

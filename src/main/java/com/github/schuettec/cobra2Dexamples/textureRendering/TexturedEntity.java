package com.github.schuettec.cobra2Dexamples.textureRendering;

import static com.github.schuettec.cobra2d.math.Math2D.normalizeAngle;

import java.awt.Dimension;

import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.World;

public class TexturedEntity extends BasicRectangleEntity implements Renderable, Updatable {

	private static final long serialVersionUID = 1L;

	protected String textureId;
	protected int layer;

	protected boolean playerControlled;

	public TexturedEntity(String textureId, Point worldCoordinates, int layer, boolean playerControlled) {
		super(worldCoordinates, new Dimension(0, 0));
		this.layer = layer;
		this.textureId = textureId;
		this.playerControlled = playerControlled;
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		Dimension textureDimension = renderer.getTextureDimension(textureId);
		createRectangleShape(textureDimension);
		/*
		 * Without the next translation, the texture is rendered at the entities center point,
		 * so we have to translate by the half of the texture dimension
		 */
		Point texturePosition = getTexturePosition(renderer, position);
		Point textureCenter = renderer.getTextureCenter(textureId);
		renderer.drawTexture(textureId, 1f, (float) texturePosition.getRoundX(), (float) texturePosition.getRoundY(),
		    textureCenter.getRoundX(), textureCenter.getRoundY(), (float) textureDimension.width,
		    (float) textureDimension.height, (float) this.getScale(), (float) this.getScale(), (float) getDegrees(), 0, 0,
		    textureDimension.width, textureDimension.height, false, false);

	}

	protected Point getTexturePosition(RendererAccess renderer, Point position) {
		Point textureCenter = renderer.getTextureCenter(textureId);
		Point textureCenterCorrection = textureCenter.scale(-1);
		return getPosition().translate(position)
		    .translate(textureCenterCorrection);
	}

	@Override
	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	@Override
	public void update(World map, float deltaTime, Controller controller) {
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

}

package com.github.schuettec.cobra2Dexamples.textureRendering;

import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.state.EntityStateValue;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class TexturedEntity extends BasicRectangleEntity implements Renderable {

	private static final long serialVersionUID = 1L;

	protected String textureId;

	@EntityStateValue
	protected int layer;

	public TexturedEntity(String textureId, Point worldCoordinates, Dimension intialDimension, int layer) {
		super(worldCoordinates, intialDimension);
		this.layer = layer;
		this.textureId = textureId;
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

	public String getTextureId() {
		return textureId;
	}

}

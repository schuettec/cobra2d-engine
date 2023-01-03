package com.github.schuettec.cobra2d.renderer.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public interface LibGdxAnimation extends LibGdxRenderable {

	float getStateTime();

	float getFrameDuration();

	int getFrameCols();

	int getFrameRows();

	String getAnimationTextureId();

	Animation<TextureRegion> getAnimation();

	default float renderAnimationFrame(RendererAccess renderer, Point screenTranslation) {
		float nextState = getStateTime() + Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = getAnimation().getKeyFrame(getStateTime(), true);

		Point textureCenter = new Point(currentFrame.getRegionWidth() / 2.0, currentFrame.getRegionHeight() / 2.0);
		Point textureCenterCorrection = textureCenter.scale(-1);
		Point texturePosition = getPosition().clone()
		    .translate(screenTranslation)
		    .translate(textureCenterCorrection);

		LibGdxExtendedAccess extendedRenderer = getExtendedRenderer(renderer);
		extendedRenderer.drawTextureRegion(currentFrame, 1f, texturePosition.getRoundX(), texturePosition.getRoundY(),
		    (float) getDegrees());
		return nextState;
	}

	default Animation<TextureRegion> createAnimation(RendererAccess rendererAccess) {
		LibGdxExtendedAccess extendedRenderer = getExtendedRenderer(rendererAccess);
		Texture texture = extendedRenderer.getTexture(getAnimationTextureId());
		int tileWidth = texture.getWidth() / getFrameCols();
		int tileHeight = texture.getHeight() / getFrameRows();
		TextureRegion[][] tmp = TextureRegion.split(texture, tileWidth, tileHeight);

		TextureRegion[] frames = new TextureRegion[getFrameCols() * getFrameRows()];
		int index = 0;
		for (int i = 0; i < getFrameRows(); i++) {
			for (int j = 0; j < getFrameCols(); j++) {
				frames[index++] = tmp[i][j];
			}
		}

		return new Animation<TextureRegion>(getFrameDuration(), frames);
	}

}
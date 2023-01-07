package com.github.schuettec.cobra2Dexamples.libgdx.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.libgdx.LibGdxAnimation;

public class AnimationEntity extends TexturedEntity implements LibGdxAnimation {

	private int frameCols;
	private int frameRows;
	private float frameDuration;

	private Animation<TextureRegion> animation;

	private float stateTime;

	private String animationTextureId;

	public AnimationEntity(String textureId, String animationTextureId, Point worldCoordinates,
	    Dimension initialDimension, int frameCols, int frameRows, float frameDuration, int layer,
	    boolean playerControlled) {
		super(textureId, worldCoordinates, initialDimension, layer);
		this.animationTextureId = animationTextureId;
		this.frameCols = frameCols;
		this.frameRows = frameRows;
		this.frameDuration = frameDuration;
	}

	@Override
	public void initialize(RendererAccess rendererAccess) {
		super.initialize(rendererAccess);
		this.animation = createAnimation(rendererAccess);
	}

	@Override
	public void render(RendererAccess renderer, Point screenTranslation) {
		createCollisionShape(renderer);
		float newStateTime = renderAnimationFrame(renderer, screenTranslation);
		setStateTime(newStateTime);
	}

	@Override
	public int getFrameRows() {
		return frameRows;
	}

	@Override
	public int getFrameCols() {
		return frameCols;
	}

	@Override
	public float getFrameDuration() {
		return frameDuration;
	}

	@Override
	public float getStateTime() {
		return stateTime;
	}

	@Override
	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	@Override
	public String getAnimationTextureId() {
		return animationTextureId;
	}

	@Override
	public Animation<TextureRegion> getAnimation() {
		return animation;
	}

}

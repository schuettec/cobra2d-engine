package com.github.schuettec.cobra2Dexamples.libgdx.blending;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.controller.Controller;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.entity.skills.Updatable;
import com.github.schuettec.cobra2d.math.Parabel;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.World;

public class PoliceCarEntity extends TexturedEntity implements Renderable, Updatable {

	/**
	 * Interval in milliseconds to switch between blue and red lights.
	 */
	private static final long LIGHT_SWITCH_INTERVAL = 500;

	/**
	 * The alpha function.
	 */
	private static Parabel alphaParabel = new Parabel(new Point(0, 0), new Point(LIGHT_SWITCH_INTERVAL / 2.0, 1));

	/**
	 * If true, the lights are rendered and switched on.
	 */
	private boolean lightsOn = true;

	/**
	 * Last timestamp the lights switched. In milliseconds.
	 */
	private long lastSwitchTimestamp = 0;
	/**
	 * If true the blue light, otherwise the red light is rendered
	 */
	private boolean blue = true;

	private String redLightTextureId;
	private String blueLightTextureId;

	private float alpha;

	public PoliceCarEntity(String carTextureId, String redLightTextureId, String blueLightTextureId,
	    Point worldCoordinates, int layer, boolean playerControlled) {
		super(carTextureId, worldCoordinates, layer, playerControlled);
		this.redLightTextureId = redLightTextureId;
		this.blueLightTextureId = blueLightTextureId;
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		super.render(renderer, position);

		Point texturePosition = getTexturePosition(renderer, position);

		if (lightsOn) {
			if (blue) {
				renderer.drawTexture(blueLightTextureId, alpha, (float) texturePosition.getRoundX(),
				    (float) texturePosition.getRoundY(), (float) getDegrees());
			} else {
				renderer.drawTexture(redLightTextureId, alpha, (float) texturePosition.getRoundX(),
				    (float) texturePosition.getRoundY(), (float) getDegrees());
			}
		}
	}

	@Override
	public void update(World map, float deltaTime, Controller controller) {
		long currentTimeMillis = System.currentTimeMillis();

		super.update(map, deltaTime, controller);

		if (lightsOn) {
			long lightTime = currentTimeMillis - lastSwitchTimestamp;
			this.alpha = (float) alphaParabel.getValue(Math.min(lightTime, LIGHT_SWITCH_INTERVAL));
			if (lightTime > LIGHT_SWITCH_INTERVAL) {
				lastSwitchTimestamp = currentTimeMillis;
				blue = !blue;
			}
		}
	}

}

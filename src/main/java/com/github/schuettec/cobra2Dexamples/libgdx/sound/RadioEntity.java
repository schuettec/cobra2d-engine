package com.github.schuettec.cobra2Dexamples.libgdx.sound;

import static java.util.Objects.isNull;

import java.util.Optional;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.entity.skills.Entity;
import com.github.schuettec.cobra2d.entity.skills.SoundEffect;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.Sound;
import com.github.schuettec.cobra2d.renderer.SoundAccess;

public class RadioEntity extends TexturedEntity implements SoundEffect {

	private String soundRessourceId;
	private long currentSoundId;
	private int soundRadius;

	private Sound sound;
	private Circle soundShape;

	public RadioEntity(String soundRessourceId, int soundRadius, String textureId, Point worldCoordinates,
	    Dimension intialDimension, int layer, boolean playerControlled) {
		super(textureId, worldCoordinates, intialDimension, layer);
		this.soundRessourceId = soundRessourceId;
		this.soundRadius = soundRadius;
		this.soundShape = new Circle(new Point(), soundRadius);
	}

	@Override
	public Circle getSoundRangeInWorldCoordinates() {
		return soundShape.clone()
		    .scale(getScale())
		    .rotate(getDegrees())
		    .translate(worldCoordinates);
	}

	@Override
	public void updateSound(SoundAccess soundAccess, Optional<Entity> relativeTo) {
		if (isNull(sound)) {
			this.sound = soundAccess.getSound(soundRessourceId);
			this.currentSoundId = this.sound.play();
			this.sound.setLooping(currentSoundId, true);
		}
		relativeTo.ifPresentOrElse(e -> setPanAndVolume(this.getPosition(), e),
		    () -> this.sound.setVolume(currentSoundId, 1f));
		;
	}

	private void setPanAndVolume(Point soundPoint, Entity relativeTo) {
		Point listenerPoint = relativeTo.getPosition();
		double distance = Math2D.getEntfernung(soundPoint, listenerPoint);
		float volume = (float) Math.max(0f, 1f - (1f / soundRadius * distance));
		double listenerDegrees = Math2D
		    .normalizeAngle(Math2D.getAngle(listenerPoint, soundPoint) - relativeTo.getDegrees());
		float leftPan = 0;
		if (listenerDegrees > 0 && listenerDegrees <= 90) {
			leftPan = (float) ((listenerDegrees) * -1f / 90f);
		} else if (listenerDegrees >= 0 && listenerDegrees <= 270) {
			leftPan = (float) (listenerDegrees * 2f / 180f - 2);
		} else if (listenerDegrees > 270 && listenerDegrees <= 360) {
			leftPan = (float) (1 - (listenerDegrees - 270) * 1f / 90f);
		}
		this.sound.setPan(this.currentSoundId, leftPan, volume);
	}

}

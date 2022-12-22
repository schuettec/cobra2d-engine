package com.github.schuettec.cobra2d.resource;

import java.awt.image.VolatileImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnimationMemory implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private transient HashMap<String, Animation> images = new HashMap<String, Animation>();

	private transient ImageMemory imageMemory;

	private final List<Serializable[]> loadedAnimations;

	public HashMap<String, Animation> getAnimations() {
		return new HashMap<String, Animation>(images);
	}

	public AnimationMemory(final ImageMemory imageMemory) {
		this.imageMemory = imageMemory;
		this.loadedAnimations = new ArrayList<Serializable[]>();
	}

	public void addAnimation(String address, String texturAddress, int width, int height) {
		loadedAnimations.add(new Serializable[] {
		    address, texturAddress, width, height
		});
		loadAnimationInternal(address, texturAddress, width, height);
	}

	public Animation removeAnimation(String animationAddress) {
		return images.remove(animationAddress);
	}

	public void clearAnimationMemory() {
		images.clear();
	}

	private void loadAnimationInternal(String address, String texturAddress, int width, int height) {
		VolatileImage image = imageMemory.getImage(texturAddress);
		Animation animation = new Animation(image, width, height);
		images.put(address, animation);
	}

	public Animation getAnimation(String address) {
		// Vorgeladene Animationen Klonen ist KEINE GUTE IDEE
		if (!images.containsKey(address)) {
			throw new RuntimeException("Cannot find an animation with key: " + address);
		} else {
			return images.get(address);
		}
	}

}

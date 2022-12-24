package com.github.schuettec.cobra2Dexamples;

import java.util.Properties;

import com.github.schuettec.cobra2d.engine.Cobra2DProperties;

public class DemoUtils {
	public static void fullscreenMode(final Properties properties) {
		properties.put(Cobra2DProperties.RESOLUTION_X, "2560"); // 1920
		properties.put(Cobra2DProperties.RESOLUTION_Y, "1440"); // 1080
		properties.put(Cobra2DProperties.BIT_DEPHT, "24");
		properties.put(Cobra2DProperties.REFRESH_REATE, "165");
		properties.put(Cobra2DProperties.REQUESTED_FPS, "165");
		properties.put(Cobra2DProperties.FULLSCREEN, "true");
	}

	public static void windowMode(final Properties properties) {
		properties.put(Cobra2DProperties.RESOLUTION_X, "800"); // 1920
		properties.put(Cobra2DProperties.RESOLUTION_Y, "800"); // 1080
		properties.put(Cobra2DProperties.BIT_DEPHT, "32");
		properties.put(Cobra2DProperties.REFRESH_REATE, "59");
		properties.put(Cobra2DProperties.REQUESTED_FPS, "59");
		properties.put(Cobra2DProperties.FULLSCREEN, "false");
	}
}

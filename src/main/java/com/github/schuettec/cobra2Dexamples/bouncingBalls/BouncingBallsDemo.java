package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import java.awt.Dimension;
import java.util.Properties;

import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class BouncingBallsDemo {
	public static void main(String[] args) {

		final Properties properties = new Properties();
		properties.put(Cobra2DProperties.RESOLUTION_X, "800"); // 1920
		properties.put(Cobra2DProperties.RESOLUTION_Y, "800"); // 1080
		properties.put(Cobra2DProperties.BIT_DEPHT, "32");
		properties.put(Cobra2DProperties.REFRESH_REATE, "59");
		properties.put(Cobra2DProperties.REQUESTED_FPS, "59");
		properties.put(Cobra2DProperties.FULLSCREEN, "false");
		properties.put(Cobra2DProperties.MAP_UPDATE, "true");
		properties.put(Cobra2DProperties.RENDERER, RendererType.LIBGDX.toString());
		properties.put(Cobra2DProperties.CREATE_WORLD_UPDATER, "false");
		properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
		properties.put(Cobra2DProperties.RESOURCE_LOCATION, "classpath");

		Cobra2DEngine engine = new Cobra2DEngine(properties);
		engine.initialize();

		WallEntity wall = new WallEntity(new Point(20, 20), new Dimension(759, 759));

		Ball ball = new Ball(new Point(300, 300), 30);

		BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(1, 1), new Dimension(799, 799), true);
		// Set screen position or the camera is centered automatically
		// camera.setScreenPosition(new Point(0, 0));

		engine.addEntity(wall, ball);
		engine.addEntity(camera);
		engine.start();
	}
}

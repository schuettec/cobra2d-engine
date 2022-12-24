package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import java.awt.Dimension;
import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.DemoUtils;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class BouncingBallsDemo {
	public static void main(String[] args) {
		// 2560x1440, bpp: 24, hz: 165
		final Properties properties = new Properties();
		DemoUtils.windowMode(properties);
		properties.put(Cobra2DProperties.MAP_UPDATE, "true");
		properties.put(Cobra2DProperties.RENDERER, RendererType.LIBGDX.toString());
		properties.put(Cobra2DProperties.CREATE_WORLD_UPDATER, "false");
		properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
		properties.put(Cobra2DProperties.RESOURCE_LOCATION, "classpath");

		Cobra2DEngine engine = new Cobra2DEngine(properties);
		engine.initialize();

		WallEntity wall = new WallEntity(new Point(0, 0), new Dimension(759, 759));

		Ball ball1 = new Ball(new Point(0, 0), 30, 5, 65);
		// Ball ball2 = new Ball(new Point(300, 300), 30, 5, 85);
		// Ball ball3 = new Ball(new Point(300, 300), 30, 5, 115);
		// Ball ball4 = new Ball(new Point(300, 300), 30, 5, 125);
		// Ball ball5 = new Ball(new Point(300, 300), 30, 5, 135);
		// Ball ball6 = new Ball(new Point(300, 300), 30, 5, 145);

		BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(0, 0), new Dimension(799, 799), true);
		// Set screen position or the camera is centered automatically
		// camera.setScreenPosition(new Point(0, 0));

		engine.addEntity(wall, ball1);// , ball2, ball3, ball4, ball5, ball6);
		engine.addEntity(camera);
		engine.start();
	}

}

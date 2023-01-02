package com.github.schuettec.cobra2Dexamples.bouncingBalls;

import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.DemoUtils;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicCircleMapCamera;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class CircleMapCameraDemo {
	public static void main(String[] args) {
		// 2560x1440, bpp: 24, hz: 165
		final Properties properties = new Properties();
		// DemoUtils.windowMode(properties);
		DemoUtils.windowMode(properties);
		properties.put(Cobra2DProperties.MAP_UPDATE, "true");
		properties.put(Cobra2DProperties.RENDERER, RendererType.LIBGDX.toString());
		properties.put(Cobra2DProperties.CREATE_WORLD_UPDATER, "false");
		properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
		properties.put(Cobra2DProperties.RESOURCE_LOCATION, "classpath");

		Cobra2DEngine engine = new Cobra2DEngine(properties);
		engine.initialize();

		WallEntity wall = new WallEntity(new Point(0, 0), new Dimension(759, 759));

		BallEntity ball1 = new BallEntity(new Point(0, 0), 30, 200, 35);
		BallEntity ball2 = new BallEntity(new Point(0, 300), 30, 200, 85);
		BallEntity ball3 = new BallEntity(new Point(300, 0), 30, 200, 115);
		BallEntity ball4 = new BallEntity(new Point(300, 300), 30, 200, 125);
		BallEntity ball5 = new BallEntity(new Point(70, 60), 30, 200, 35);
		BallEntity ball6 = new BallEntity(new Point(70, 300), 30, 200, 85);
		BallEntity ball7 = new BallEntity(new Point(140, 150), 30, 200, 115);
		BallEntity ball8 = new BallEntity(new Point(180, 350), 30, 200, 125);

		BasicCircleMapCamera camera = new BasicCircleMapCamera(new Point(0, 0), 400, true);
		camera.setDrawCameraOutline(true);
		camera.setDrawCollisionShape(true);
		camera.setDrawEntityPoints(true);
		// Set screen position or the camera is centered automatically
		// camera.setScreenPosition(new Point(0, 0));

		engine.addEntity(wall, ball1, ball2, ball3, ball4, ball5, ball6, ball7, ball8);
		engine.addEntity(camera);
		engine.start();
	}

}

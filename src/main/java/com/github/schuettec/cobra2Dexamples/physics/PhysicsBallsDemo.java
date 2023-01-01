package com.github.schuettec.cobra2Dexamples.physics;

import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.DemoUtils;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class PhysicsBallsDemo {
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

		PhysicsWallEntity wall = new PhysicsWallEntity(new Point(300, 0), new Dimension(100, 799));

		PhysicsBallEntity ball1 = new PhysicsBallEntity(new Point(0, 0), 30, 20000, 30);
		// PhysicsBallEntity ball2 = new PhysicsBallEntity(new Point(0, 300), 30, 200, 85);
		// PhysicsBallEntity ball3 = new PhysicsBallEntity(new Point(300, 0), 30, 200, 115);
		// PhysicsBallEntity ball4 = new PhysicsBallEntity(new Point(300, 300), 30, 200, 125);
		// PhysicsBallEntity ball5 = new PhysicsBallEntity(new Point(70, 60), 30, 200, 35);
		// PhysicsBallEntity ball6 = new PhysicsBallEntity(new Point(70, 300), 30, 200, 85);
		// PhysicsBallEntity ball7 = new PhysicsBallEntity(new Point(140, 150), 30, 200, 115);
		// PhysicsBallEntity ball8 = new PhysicsBallEntity(new Point(180, 450), 30, 200, 125);

		BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(0, 0), new Dimension(799, 799), true);
		// Set screen position or the camera is centered automatically
		// camera.setScreenPosition(new Point(0, 0));

		engine.addEntity(wall, ball1);// , ball2, ball3, ball4, ball5, ball6, ball7, ball8);
		engine.addEntity(camera);
		engine.start();
	}

}

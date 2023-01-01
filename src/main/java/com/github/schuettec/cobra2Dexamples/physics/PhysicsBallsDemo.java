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

		PhysicsWallEntity wall1 = new PhysicsWallEntity(new Point(20, 400), new Dimension(20, 789));
		PhysicsWallEntity wall2 = new PhysicsWallEntity(new Point(779, 400), new Dimension(20, 789));
		PhysicsWallEntity wall3 = new PhysicsWallEntity(new Point(400, 779), new Dimension(789, 20));
		PhysicsWallEntity wall4 = new PhysicsWallEntity(new Point(400, 19), new Dimension(789, 20));
		PhysicsWallEntity block = new PhysicsWallEntity(new Point(400, 400), new Dimension(200, 200));

		int forceToApply = 15;
		int radius = 15;
		PhysicsBallEntity ball1 = new PhysicsBallEntity(new Point(240, 240), radius, forceToApply, 30);
		PhysicsBallEntity ball2 = new PhysicsBallEntity(new Point(200, 200), radius, forceToApply, 30);
		PhysicsBallEntity ball3 = new PhysicsBallEntity(new Point(170, 170), radius, forceToApply, 30);
		PhysicsBallEntity ball4 = new PhysicsBallEntity(new Point(140, 140), radius, forceToApply, 30);
		PhysicsBallEntity ball5 = new PhysicsBallEntity(new Point(110, 110), radius, forceToApply, 30);
		PhysicsBallEntity ball6 = new PhysicsBallEntity(new Point(80, 80), radius, forceToApply, 30);
		PhysicsBallEntity ball7 = new PhysicsBallEntity(new Point(50, 50), radius, forceToApply, 30);
		PhysicsBallEntity ball8 = new PhysicsBallEntity(new Point(10, 10), radius, forceToApply, 30);

		BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(400, 400), new Dimension(799, 799), true);
		camera.setDrawCameraOutline(true);
		camera.setDrawCollisionShape(true);
		camera.setDrawEntityPoints(true);
		camera.setDrawMouse(true);

		// Set screen position or the camera is centered automatically
		// camera.setScreenPosition(new Point(0, 0));

		engine.addEntity(wall1, wall2, wall3, wall4, block, ball1, ball2, ball3, ball4, ball5, ball6, ball7, ball8);
		// wall4,
		engine.addEntity(camera);
		engine.setCameraForInput(camera);
		engine.start();
	}

}

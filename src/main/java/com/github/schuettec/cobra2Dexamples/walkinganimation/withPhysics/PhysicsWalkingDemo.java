package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.DemoUtils;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

public class PhysicsWalkingDemo {
	public static void main(String[] args) {
		// 2560x1440, bpp: 24, hz: 165
		final Properties properties = new Properties();
		// DemoUtils.windowMode(properties);
		DemoUtils.windowMode(properties);
		properties.put(Cobra2DProperties.RESOLUTION_X, "1600"); // 1920
		properties.put(Cobra2DProperties.RESOLUTION_Y, "800"); // 1080
		properties.put(Cobra2DProperties.MAP_UPDATE, "true");
		properties.put(Cobra2DProperties.RENDERER, RendererType.LIBGDX.toString());
		properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
		properties.put(Cobra2DProperties.RESOURCE_LOCATION, "classpath");

		Cobra2DEngine engine = new Cobra2DEngine(properties);
		engine.initialize();

		Cobra2DWorld world = engine.getWorld();
		world.setGravity(0, -9.7f);

		PhysicsWalkFloorEntity boden = new PhysicsWalkFloorEntity(new Point(0, 19), new Dimension(1600, 20));
		System.out.println("Boden: " + boden.getId());

		PhysicsWalkFloorEntity block1 = new PhysicsWalkFloorEntity(new Point(600, 55), new Dimension(50, 50));
		System.out.println("Block 1: " + block1.getId());

		PhysicsWalkFloorEntity block2 = new PhysicsWalkFloorEntity(new Point(100, 55), new Dimension(50, 50));
		System.out.println("Block 2: " + block2.getId());

		PhysicsWalkingEntity player = new PhysicsWalkingEntity(new Point(300, 400), new Dimension(50, 200), 100, 4, 0);

		BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(0, 400), new Dimension(1600, 799), false);
		camera.setDrawCameraOutline(true);
		camera.setDrawCollisionShape(true);
		camera.setDrawEntityPoints(true);
		camera.setDrawMouse(true);
		camera.setScreenPosition(new Point(400, 400));
		engine.setCameraForInput(camera);

		// Set screen position or the camera is centered
		// automatically
		// camera.setScreenPosition(new Point(0, 0));

		engine.addEntity(player, boden, block1, block2);
		// block);
		engine.addEntity(camera);
		engine.setCameraForInput(camera);
		engine.start();
	}

}

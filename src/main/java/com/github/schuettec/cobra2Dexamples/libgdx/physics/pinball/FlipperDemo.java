package com.github.schuettec.cobra2Dexamples.libgdx.physics.pinball;

import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.DemoUtils;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape.RectanglePoint;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;

public class FlipperDemo {
	public static void main(String[] args) {
		// 2560x1440, bpp: 24, hz: 165
		final Properties properties = new Properties();
		// DemoUtils.windowMode(properties);
		DemoUtils.windowMode(properties);
		properties.put(Cobra2DProperties.MAP_UPDATE, "true");
		properties.put(Cobra2DProperties.RENDERER, RendererType.LIBGDX.toString());
		properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
		properties.put(Cobra2DProperties.RESOURCE_LOCATION, "classpath");

		Cobra2DEngine engine = new Cobra2DEngine(properties);
		engine.initialize();
		Cobra2DWorld world = engine.getWorld();
		world.setGravity(0, -10.0f);

		// Pinball playfield ratio w/h: 0.4822
		// height: 800 => width: 385.76

		PhysicsWallEntity wall1 = new PhysicsWallEntity(new Point(0, 0), new Dimension(10, 789));
		PhysicsWallEntity wall2 = new PhysicsWallEntity(new Point(0, 0), new Dimension(10, 789));
		PhysicsWallEntity wall3 = new PhysicsWallEntity(new Point(0, 0), new Dimension(789, 10));
		PhysicsWallEntity wall4 = new PhysicsWallEntity(new Point(0, 0), new Dimension(789, 10));

		wall1.setPositionByPoint(RectanglePoint.TR, new Point(0, 0));
		wall2.setPositionByPoint(RectanglePoint.TL, new Point(386, 0));
		wall2.setPositionByPoint(RectanglePoint.TL, new Point(386, 0));

		FlipperEntity flipperLeft = new FlipperEntity(new Point(240, 200), new Dimension(200, 20));

		PinballCamera camera = new PinballCamera(new Point(0, 0), new Dimension(799, 799), true);
		camera.setPositionByPoint(RectanglePoint.TL, new Point(-10, 20));
		camera.setDrawCameraOutline(true);
		camera.setDrawCollisionShape(true);
		camera.setDrawEntityPoints(true);
		camera.setDrawMouse(true);
		camera.setScreenPosition(new Point(400, 400));
		engine.setCameraForInput(camera);

		// Set screen position or the camera is centered automatically
		// camera.setScreenPosition(new Point(0, 0));
		engine.addEntity(wall1);
		engine.addEntity(wall2);
//		engine.addEntity(wall3);
//		engine.addEntity(wall4);
		engine.addEntity(flipperLeft);
		// wall4,
		engine.addEntity(camera);
		engine.setCameraForInput(camera);
		engine.start();
	}

}

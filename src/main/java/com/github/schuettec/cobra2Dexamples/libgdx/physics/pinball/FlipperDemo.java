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
	private static final int WALL_WIDTH = 10;
	private static final double PLAYFIELD_RATIO = 0.4812d;

	public static void main(String[] args) {
		// 2560x1440, bpp: 24, hz: 165
		final Properties properties = new Properties();
		// DemoUtils.windowMode(properties);
		DemoUtils.windowMode(properties);
		properties.put(Cobra2DProperties.MAP_UPDATE, "true");
		properties.put(Cobra2DProperties.RENDERER, RendererType.LIBGDX.toString());
		properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
		properties.put(Cobra2DProperties.RESOURCE_LOCATION, "classpath");
		properties.put(Cobra2DProperties.RESOLUTION_X, "1000");
		properties.put(Cobra2DProperties.RESOLUTION_Y, "1000");

		Cobra2DEngine engine = new Cobra2DEngine(properties);
		engine.initialize();
		Cobra2DWorld world = engine.getWorld();
		world.setGravity(0, -10.0f);

		// Pinball playfield ratio w/h: 0.4812
		double height = 800;
		double width = height * PLAYFIELD_RATIO;

		PhysicsWallEntity wall1 = new PhysicsWallEntity(new Point(0, 0), new Dimension(WALL_WIDTH, height));
		PhysicsWallEntity wall2 = new PhysicsWallEntity(new Point(0, 0), new Dimension(WALL_WIDTH, height));
		PhysicsWallEntity wall3 = new PhysicsWallEntity(new Point(0, 0), new Dimension(width, WALL_WIDTH));
		PhysicsWallEntity wall4 = new PhysicsWallEntity(new Point(0, 0), new Dimension(width, WALL_WIDTH));

		wall1.setPositionByPoint(RectanglePoint.TR, new Point(0, height));
		wall2.setPositionByPoint(RectanglePoint.TL, new Point(width, height));
		wall3.setPositionByPoint(RectanglePoint.TL, new Point(0, 0));
		wall4.setPositionByPoint(RectanglePoint.TL, new Point(0, height + WALL_WIDTH));

		AnchorEntity entityLeft = new AnchorEntity(new Point(70, 150), new Dimension(5, 5));
		FlipperEntity flipperLeft = new FlipperEntity(new Point(70, 150), new Dimension(90, 18));
		flipperLeft.setDegrees(330);

		RevoluteJointEntity jointLeft = new RevoluteJointEntity(entityLeft, flipperLeft);

		PinballCamera camera = new PinballCamera(new Point(0, height), new Dimension(1000, 1000), false);
		camera.setPositionByPoint(RectanglePoint.TL, new Point(-20, height + 20));
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
		engine.addEntity(wall3);
		engine.addEntity(wall4);
		engine.addEntity(entityLeft);
		engine.addEntity(flipperLeft);
		engine.addEntity(jointLeft);

		engine.addEntity(camera);
		engine.setCameraForInput(camera);
		engine.start();
	}

}

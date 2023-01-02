package com.github.schuettec.cobra2Dexamples.libgdx.physics.car;

import static java.lang.String.valueOf;

import java.net.URL;
import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.libgdx.physics.bouncingballs.PhysicsWallEntity;
import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape.RectanglePoint;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class PhysxCarDemo {

	public static void main(String[] args) throws Exception {

		int xRes = 1920;
		float xResHalf = xRes / 2.0f;
		int yRes = 1080;
		float yResHalf = yRes / 2.0f;

		final Properties properties = new Properties();
		properties.put(Cobra2DProperties.RESOLUTION_X, valueOf(xRes)); // 1920
		properties.put(Cobra2DProperties.RESOLUTION_Y, valueOf(yRes)); // 1080
		properties.put(Cobra2DProperties.BIT_DEPHT, "32");
		properties.put(Cobra2DProperties.REFRESH_REATE, "59");
		properties.put(Cobra2DProperties.REQUESTED_FPS, "59");
		properties.put(Cobra2DProperties.FULLSCREEN, "false");
		properties.put(Cobra2DProperties.MAP_UPDATE, "true");
		properties.put(Cobra2DProperties.RENDERER, RendererType.LIBGDX.toString());
		properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
		properties.put(Cobra2DProperties.RESOURCE_LOCATION, "classpath");

		Cobra2DEngine engine = new Cobra2DEngine(properties);
		engine.addImage("floor", new URL("resource:floor.png"));
		engine.addImage("police", new URL("resource:cars/police.png"));
		engine.addImage("police-red-alarm-light", new URL("resource:cars/police-red-alarm-light.png"));
		engine.addImage("police-blue-alarm-light", new URL("resource:cars/police-blue-alarm-light.png"));
		engine.addImage("police-red-light", new URL("resource:cars/police-red-light.png"));
		engine.addImage("police-blue-light", new URL("resource:cars/police-blue-light.png"));
		engine.addImage("front-light", new URL("resource:cars/front-light.png"));
		engine.addImage("brake-light", new URL("resource:cars/brake-light.png"));
		engine.addImage("brake-light-color", new URL("resource:cars/brake-light-color.png"));
		engine.addImage("light", new URL("resource:light.png"));

		engine.initialize();

		PhysicsWallEntity wall1 = new PhysicsWallEntity(new Point(), new Dimension());
		PhysicsWallEntity wall2 = new PhysicsWallEntity(new Point(), new Dimension());
		PhysicsWallEntity wall3 = new PhysicsWallEntity(new Point(), new Dimension());
		PhysicsWallEntity wall4 = new PhysicsWallEntity(new Point(), new Dimension());

		wall1.setPositionByPoint(new Point());
		wall2.setPositionByPoint(RectanglePoint.TR, new Point(xRes, yRes));
		wall3.setPositionByPoint(RectanglePoint.TL, new Point(0, yRes));
		wall4.setPositionByPoint(RectanglePoint.BL, new Point(0, 0));
		engine.addEntity(wall1, wall2, wall3, wall4);

		BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(0, 0), new Dimension(1920, 1080), false);
		engine.addEntity(camera);
		camera.setPositionByPoint(RectanglePoint.BL, new Point(0, 0));
		camera.setDrawCameraOutline(true);
		camera.setDrawCollisionShape(true);
		camera.setDrawEntityPoints(true);

		TexturedEntity t1 = new TexturedEntity("floor", new Point(), 0, false);
		engine.addEntity(t1);
		t1.setPositionByPoint(RectanglePoint.BL, new Point());

		// List<Placeable> floorEntities = t1
		// .placeWithCreator(engine, () -> new TexturedEntity("floor", new Point(), 0, false))
		// .placeEastOf()
		// .placeEastOf()
		// .placeEastOf()
		// .placeEastOf()
		// .getCreated();
		// engine.addEntity(floorEntities);

		PhysxPoliceCarEntity b1 = new PhysxPoliceCarEntity("police", "police-red-alarm-light", "police-blue-alarm-light",
		    "police-red-light", "police-blue-light", "front-light", "brake-light", "brake-light-color", new Point(300, 300),
		    new Dimension(216, 100), 2, true);

		engine.addEntity(b1);

		engine.start();
	}
}

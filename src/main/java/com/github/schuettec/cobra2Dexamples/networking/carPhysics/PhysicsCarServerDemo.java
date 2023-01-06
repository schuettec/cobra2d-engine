package com.github.schuettec.cobra2Dexamples.networking.carPhysics;

import static java.lang.String.valueOf;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.libgdx.physics.bouncingballs.PhysicsWallEntity;
import com.github.schuettec.cobra2Dexamples.libgdx.physics.car.PhysxPoliceCarEntity;
import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape.RectanglePoint;
import com.github.schuettec.cobra2d.entity.skills.placement.Placeable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.network.server.Cobra2DServer;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class PhysicsCarServerDemo {

	public static void main(String[] args) throws Exception {

		int xRes = 1920;
		int yRes = 1080;

		final Properties properties = new Properties();
		properties.put(Cobra2DProperties.RESOLUTION_X, valueOf(xRes)); // 1920
		properties.put(Cobra2DProperties.RESOLUTION_Y, valueOf(yRes)); // 1080
		properties.put(Cobra2DProperties.BIT_DEPHT, "32");
		properties.put(Cobra2DProperties.REFRESH_REATE, "59");
		properties.put(Cobra2DProperties.REQUESTED_FPS, "59");
		properties.put(Cobra2DProperties.FULLSCREEN, "false");
		properties.put(Cobra2DProperties.MAP_UPDATE, "true");
		properties.put(Cobra2DProperties.RENDERER, RendererType.DEDICATED_SERVER.toString());
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

		Dimension cameraDimension = new Dimension(1920, 1080);

		Cobra2DServer server = (Cobra2DServer) engine.getRenderer();
		server.setNetworkCameraDimension(cameraDimension);
		server.setSpawnEntityFactory(() -> {
			Dimension policeDimension = engine.dimensionOf("police");
			PhysxPoliceCarEntity playerEntity = new PhysxPoliceCarEntity("police", "police-red-alarm-light",
			    "police-blue-alarm-light", "police-red-light", "police-blue-light", "front-light", "brake-light",
			    "brake-light-color", new Point(300, 300), policeDimension, 2, true);
			return playerEntity;
		});
		server.setPlayerCameraFactory((playerEntity) -> {
			BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(0, 0), cameraDimension, false);
			camera.follow(playerEntity);
			return camera;
		});

		PhysicsWallEntity wall1 = new PhysicsWallEntity(new Point(), new Dimension(20, yRes));
		PhysicsWallEntity wall2 = new PhysicsWallEntity(new Point(), new Dimension(20, yRes));
		PhysicsWallEntity wall3 = new PhysicsWallEntity(new Point(), new Dimension(xRes, 20));
		PhysicsWallEntity wall4 = new PhysicsWallEntity(new Point(), new Dimension(xRes, 20));
		wall1.setPositionByPoint(new Point());
		wall2.setPositionByPoint(RectanglePoint.TR, new Point(xRes, yRes));
		wall3.setPositionByPoint(RectanglePoint.TL, new Point(0, yRes));
		wall4.setPositionByPoint(RectanglePoint.BL, new Point(0, 0));
		engine.addEntity(wall1, wall2, wall3, wall4);

		Dimension floorDimension = engine.dimensionOf("floor");
		TexturedEntity t1 = new TexturedEntity("floor", new Point(), floorDimension, 0, false);
		engine.addEntity(t1);
		t1.setPositionByPoint(RectanglePoint.BL, new Point(20, 20));

		List<Placeable> floorEntities = t1
		    .placeWithCreator(engine, () -> new TexturedEntity("floor", new Point(), floorDimension, 0, false))
		    .placeEastOf()
		    .placeEastOf()
		    .placeEastOf()
		    .placeEastOf()
		    .placeEastOf()
		    .placeNorthOf()
		    .placeWesthOf()
		    .placeWesthOf()
		    .placeWesthOf()
		    .placeWesthOf()
		    .placeWesthOf()
		    .placeNorthOf()
		    .placeEastOf()
		    .placeEastOf()
		    .placeEastOf()
		    .placeEastOf()
		    .placeEastOf()
		    .getCreated();
		engine.addEntity(floorEntities);

		engine.start();
	}
}

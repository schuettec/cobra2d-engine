package com.github.schuettec.cobra2Dexamples.networking.simpleCar;

import static java.lang.String.valueOf;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.bouncingBalls.WallEntity;
import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape.RectanglePoint;
import com.github.schuettec.cobra2d.entity.skills.placement.Placeable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.network.server.Cobra2DServer;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class SimpleCarServerDemo {

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
		engine.addImage("car", new URL("resource:cars/Audi.png"));

		engine.initialize();

		Dimension cameraDimension = new Dimension(1920, 1080);

		Cobra2DServer server = (Cobra2DServer) engine.getRenderer();
		server.setNetworkCameraDimension(cameraDimension);
		server.setSpawnEntityFactory(() -> {
			Dimension rotatingDimension = engine.dimensionOf("car");
			SimpleCarEntity entity = new SimpleCarEntity("car", new Point(100, 100), rotatingDimension, 4, false);
			return entity;
		});

		WallEntity wall1 = new WallEntity(new Point(), new Dimension(20, yRes));
		WallEntity wall2 = new WallEntity(new Point(), new Dimension(20, yRes));
		WallEntity wall3 = new WallEntity(new Point(), new Dimension(xRes, 20));
		WallEntity wall4 = new WallEntity(new Point(), new Dimension(xRes, 20));
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
		    .placeNorthOf()
		    .placeNorthOf()
		    .placeEastOf()
		    .getCreated();
		engine.addEntity(floorEntities);

		engine.start();
	}
}

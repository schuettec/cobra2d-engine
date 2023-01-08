package com.github.schuettec.cobra2Dexamples.networking.bullet;

import static java.lang.String.valueOf;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.libgdx.person.HarveyEntity;
import com.github.schuettec.cobra2Dexamples.libgdx.sound.RadioEntity;
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

public class PersonServerDemo {

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
		engine.addImage("walkcyle_harvey", new URL("resource:walkcyle_harvey.png"));
		engine.addImage("walkcyle_harvey_static", new URL("resource:walkcyle_harvey_static.png"));
		engine.addImage("head", new URL("resource:head.png"));
		engine.addImage("radio", new URL("resource:radio.png"));

		engine.addSound("sound", new URL("resource:sounds/sound.ogg"));

		engine.initialize();

		Cobra2DServer server = (Cobra2DServer) engine.getRenderer();
		server.setSpawnEntityFactory(() -> {
			Dimension harveysDimension = engine.dimensionOf("walkcyle_harvey_static");
			HarveyEntity h = new HarveyEntity("head", "walkcyle_harvey_static", "walkcyle_harvey", new Point(0, 0),
			    harveysDimension, 4, 4, 2, false);
			h.setLayer(5);
			return h;
		});
		server.setPlayerCameraFactory((playerEntity) -> {
			Dimension cameraDimension = new Dimension(1920, 1080);
			BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(0, 0), cameraDimension, false);
			camera.setCenterOnScreen(false);
			camera.follow(playerEntity);
			return camera;
		});

		Dimension floorDimension = engine.dimensionOf("floor");
		TexturedEntity t1 = new TexturedEntity("floor", new Point(), floorDimension, 0);
		engine.addEntity(t1);
		t1.setPositionByPoint(RectanglePoint.BL, new Point(20, 20));

		List<Placeable> floorEntities = t1
		    .placeWithCreator(engine, () -> new TexturedEntity("floor", new Point(), floorDimension, 0))
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

		Dimension radioDimension = engine.dimensionOf("radio");
		RadioEntity s1 = new RadioEntity("sound", 500, "radio", new Point(200, 200), radioDimension, 4, false);
		engine.addEntity(s1);

		engine.start();
	}
}

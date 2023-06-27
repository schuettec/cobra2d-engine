package com.github.schuettec.cobra2Dexamples.libgdx.shader;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.schuettec.cobra2Dexamples.libgdx.blending.LightEntity;
import com.github.schuettec.cobra2Dexamples.libgdx.blending.PoliceCarEntity;
import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape.RectanglePoint;
import com.github.schuettec.cobra2d.entity.skills.placement.Placeable;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class ShaderDemo {
	public static void main(String[] args) throws Exception {

		// Shader config
		ShaderProgram.pedantic = false;

		final Properties properties = new Properties();
		properties.put(Cobra2DProperties.RESOLUTION_X, "1920"); // 1920
		properties.put(Cobra2DProperties.RESOLUTION_Y, "1080"); // 1080
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
		engine.addImage("sky", new URL("resource:sky.jpeg"));

		engine.initialize();

		Dimension floorDimension = engine.dimensionOf("floor");
		TexturedEntity floorEntity = new TexturedEntity("floor", new Point(), floorDimension, 0);
		engine.addEntity(floorEntity);
		floorEntity.setPositionByPoint(RectanglePoint.BL, new Point(20, 20));

		List<Placeable> floorEntities = floorEntity
				.placeWithCreator(engine, () -> new TexturedEntity("floor", new Point(), floorDimension, 0))
				.placeEastOf().placeEastOf().placeEastOf().placeEastOf().placeEastOf().placeNorthOf().placeWesthOf()
				.placeWesthOf().placeWesthOf().placeWesthOf().placeWesthOf().placeNorthOf().placeEastOf().placeEastOf()
				.placeEastOf().placeEastOf().placeEastOf().getCreated();
		engine.addEntity(floorEntities);

		Dimension policeDimension = engine.dimensionOf("police");
		PoliceCarEntity car = new PoliceCarEntity("police", "police-red-alarm-light", "police-blue-alarm-light",
				"police-red-light", "police-blue-light", "front-light", "brake-light", "brake-light-color",
				new Point(300, 300), policeDimension, 2, true);

		Dimension lightDimension = engine.dimensionOf("light");
		LightEntity l = new LightEntity("light", new Point(300, 300), lightDimension, 0, false);
		l.setLayer(4);

		BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(0, 0), new Dimension(1920, 1080), false);
		camera.setDrawCameraOutline(false);
		camera.setDrawCollisionShape(false);
		camera.setDrawEntityPoints(false);
		engine.setCameraForInput(camera);
		camera.follow(car);

		// Set screen position or the camera is centered automatically
		// camera.setScreenPosition(new Point(800, 400));
		// camera.scale(0.5);

		Dimension skyDimension = engine.dimensionOf("floor");
		ShaderEntity shaderEntity = new ShaderEntity("floor", new Point(), skyDimension, 1);

		engine.addEntity(floorEntity, l, car, camera, shaderEntity);

		engine.start();
	}
}

package com.github.schuettec.cobra2Dexamples.libgdx.person;

import java.awt.Dimension;
import java.net.URL;
import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class PersonDemo {

	public static void main(String[] args) throws Exception {

		final Properties properties = new Properties();
		properties.put(Cobra2DProperties.RESOLUTION_X, "1920"); // 1920
		properties.put(Cobra2DProperties.RESOLUTION_Y, "1080"); // 1080
		properties.put(Cobra2DProperties.BIT_DEPHT, "32");
		properties.put(Cobra2DProperties.REFRESH_REATE, "59");
		properties.put(Cobra2DProperties.REQUESTED_FPS, "59");
		properties.put(Cobra2DProperties.FULLSCREEN, "false");
		properties.put(Cobra2DProperties.MAP_UPDATE, "true");
		properties.put(Cobra2DProperties.RENDERER, RendererType.LIBGDX.toString());
		properties.put(Cobra2DProperties.CREATE_WORLD_UPDATER, "false");
		properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
		properties.put(Cobra2DProperties.RESOURCE_LOCATION, "classpath");

		Cobra2DEngine engine = new Cobra2DEngine(properties);
		engine.initialize();

		TexturedEntity t1 = new TexturedEntity("floor", new Point(0, 0), 0, false);
		TexturedEntity t2 = new TexturedEntity("floor", new Point(612, 300), 0, false);

		HarveyEntity h = new HarveyEntity("head", "walkcyle_harvey_static", "walkcyle_harvey", new Point(0, 0), 4, 4, 2,
		    false);
		h.setLayer(5);

		BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(0, 0), new Dimension(1920, 1080), false);
		camera.setDrawCameraOutline(true);
		camera.setDrawCollisionShape(true);
		camera.setDrawEntityPoints(true);

		engine.setCameraForInput(camera);

		engine.addEntity(t1, t2, h, camera);

		engine.addImage("floor", new URL("resource:floor.png"));
		engine.addImage("walkcyle_harvey", new URL("resource:walkcyle_harvey.png"));
		engine.addImage("walkcyle_harvey_static", new URL("resource:walkcyle_harvey_static.png"));
		engine.addImage("head", new URL("resource:head.png"));

		engine.start();
	}
}

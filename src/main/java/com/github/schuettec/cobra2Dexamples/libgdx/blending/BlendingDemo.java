package com.github.schuettec.cobra2Dexamples.libgdx.blending;

import java.awt.Dimension;
import java.net.URL;
import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class BlendingDemo {

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

		TexturedEntity t1 = new TexturedEntity("floor", new Point(300, 300), 0, false);
		TexturedEntity t2 = new TexturedEntity("floor", new Point(612, 300), 0, false);
		// TexturedEntity t2 = new TexturedEntity("police", new Point(300, 300), 1, true);
		PoliceCarEntity b1 = new PoliceCarEntity("police", "police-red-light", "police-blue-light", "front-light",
		    "brake-light", new Point(300, 300), 2, true);

		// LightEntity l = new LightEntity("light", new Point(300, 300), 0, false);
		// l.setLayer(4);

		BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(0, 0), new Dimension(1920, 1080), false);
		// Set screen position or the camera is centered automatically
		// camera.setScreenPosition(new Point(0, 0));
		// camera.scale(0.5);

		engine.addEntity(t1, t2, b1, camera);
		// engine.addEntity(camera);

		engine.addImage("floor", new URL("resource:floor.png"));
		engine.addImage("police", new URL("resource:cars/police.png"));
		engine.addImage("police-red-light", new URL("resource:cars/police-red-light.png"));
		engine.addImage("police-blue-light", new URL("resource:cars/police-blue-light.png"));
		engine.addImage("front-light", new URL("resource:cars/front-light.png"));
		engine.addImage("brake-light", new URL("resource:cars/brake-light.png"));
		engine.addImage("light", new URL("resource:light.png"));

		engine.start();
	}
}

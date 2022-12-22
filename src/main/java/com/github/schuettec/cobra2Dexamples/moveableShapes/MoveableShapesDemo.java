package com.github.schuettec.cobra2Dexamples.moveableShapes;

import java.awt.Dimension;
import java.util.Properties;

import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicMapCamera;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class MoveableShapesDemo {
	public static void main(String[] args) {

		final Properties properties = new Properties();
		properties.put(Cobra2DProperties.RESOLUTION_X, "1920"); // 1920
		properties.put(Cobra2DProperties.RESOLUTION_Y, "1080"); // 1080
		properties.put(Cobra2DProperties.BIT_DEPHT, "32");
		properties.put(Cobra2DProperties.REFRESH_REATE, "59");
		properties.put(Cobra2DProperties.REQUESTED_FPS, "60");
		properties.put(Cobra2DProperties.FULLSCREEN, "false");
		properties.put(Cobra2DProperties.MAP_UPDATE, "true");
		properties.put(Cobra2DProperties.RENDERER, RendererType.JAVA2D.toString());
		properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
		properties.put(Cobra2DProperties.RESOURCE_LOCATION, "classpath");

		Cobra2DEngine engine = new Cobra2DEngine(properties);
		engine.initialize();

		// BallEntity c1 = new BallEntity(new Point(200, 200), 120, false);
		// BallEntity c2 = new BallEntity(new Point(200, 260), 70, true);
		MoveablePolygonEntity p1 = new MoveablePolygonEntity(new Point(200, 200), new Dimension(100, 100), false);

		MoveablePolygonEntity p2 = new MoveablePolygonEntity(new Point(300, 300), new Dimension(200, 200), true);

		BasicMapCamera camera = new BasicMapCamera(new Point(200, 200), new Dimension(400, 400));

		engine.addEntity(p1, p2);
		engine.addEntity(camera);
		engine.start();
	}
}

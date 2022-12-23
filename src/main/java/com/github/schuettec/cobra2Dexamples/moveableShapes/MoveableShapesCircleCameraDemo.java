package com.github.schuettec.cobra2Dexamples.moveableShapes;

import java.awt.Dimension;
import java.util.Properties;

import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicCircleMapCamera;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class MoveableShapesCircleCameraDemo {
	public static void main(String[] args) {

		final Properties properties = new Properties();
		properties.put(Cobra2DProperties.RESOLUTION_X, "800"); // 1920
		properties.put(Cobra2DProperties.RESOLUTION_Y, "800"); // 1080
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

		MoveableCircleEntity c1 = new MoveableCircleEntity(new Point(200, 200), 120, false);
		MoveableCircleEntity c2 = new MoveableCircleEntity(new Point(100, 200), 70, false);
		MoveablePolygonEntity p1 = new MoveablePolygonEntity(new Point(120, 220), new Dimension(100, 100), false);

		MoveablePolygonEntity p2 = new MoveablePolygonEntity(new Point(130, 230), new Dimension(200, 200), false);

		BasicCircleMapCamera camera = new BasicCircleMapCamera(new Point(1, 1), 799, true);
		// Set screen position or the camera is centered automatically
		// camera.setScreenPosition(new Point(0, 0));
		camera.scale(0.5);

		engine.addEntity(c1, c2, p1, p2);
		engine.addEntity(camera);
		engine.start();
	}
}

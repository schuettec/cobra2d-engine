package com.github.schuettec.cobra2Dexamples.render3d;

import java.net.URL;
import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.DemoUtils;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.AnimatedEntity3D;
import com.github.schuettec.cobra2d.entity.Entity3D;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.math.Circle;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Math2D;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.RendererType;
import com.github.schuettec.cobra2d.world.WorldAccess;

public class Demo3D {
	public static void main(String[] args) throws Exception {
		// 2560x1440, bpp: 24, hz: 165
		final Properties properties = new Properties();
		// DemoUtils.windowMode(properties);
		DemoUtils.windowMode(properties);
		properties.put(Cobra2DProperties.MAP_UPDATE, "true");
		properties.put(Cobra2DProperties.RENDERER, RendererType.LIBGDX.toString());
		properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
		properties.put(Cobra2DProperties.RESOURCE_LOCATION, "classpath");

		Cobra2DEngine engine = new Cobra2DEngine(properties);
		engine.addModel("brickModel", new URL("resource:models/brick/brick.g3dj"));
		engine.addModel("brickModelUV", new URL("resource:models/brick/brick-uvmap.g3dj"));

		engine.addModel("brickAnimation", new URL("resource:models/animation/brick-anim.g3dj"));

		engine.addModel("uvCube", new URL("resource:models/uvmap/uvcube-blend-exporter.g3dj"));

		engine.initialize();

		Entity3D e1 = new Entity3D("brickModel", new Point(350, 350), 40) {
			@Override
			public void update(WorldAccess worldAccess, float deltaTime) {
				super.update(worldAccess, deltaTime);
				setDegrees(Math2D.normalizeAngle(getDegrees() + 1d));
			}

			@Override
			public void render(RendererAccess renderer, Point screenTranslation) {
				super.render(renderer, screenTranslation);
				Circle collisionShape = getCollisionShapeInWorldCoordinates();
				renderCircle(collisionShape, renderer, screenTranslation);
			}
		};
		e1.setScale(2d);

		AnimatedEntity3D e2 = new AnimatedEntity3D("brickAnimation", new Point(0, 0), 40);

		Entity3D e3 = new Entity3D("uvCube", new Point(150, 150), 40) {
			@Override
			public void update(WorldAccess worldAccess, float deltaTime) {
				super.update(worldAccess, deltaTime);
				setDegrees(Math2D.normalizeAngle(getDegrees() + 1d));
			}

			@Override
			public void render(RendererAccess renderer, Point screenTranslation) {
				super.render(renderer, screenTranslation);
				Circle collisionShape = getCollisionShapeInWorldCoordinates();
				renderCircle(collisionShape, renderer, screenTranslation);
			}
		};

		Entity3D e4 = new Entity3D("brickModelUV", new Point(250, 250), 40) {
			@Override
			public void update(WorldAccess worldAccess, float deltaTime) {
				super.update(worldAccess, deltaTime);
				setDegrees(Math2D.normalizeAngle(getDegrees() + 1d));
			}

			@Override
			public void render(RendererAccess renderer, Point screenTranslation) {
				super.render(renderer, screenTranslation);
				Circle collisionShape = getCollisionShapeInWorldCoordinates();
				renderCircle(collisionShape, renderer, screenTranslation);
			}
		};
		// e4.setScale(2d);

		BasicRectangleMapCamera camera = new BasicRectangleMapCamera(new Point(0, 0), new Dimension(799, 799), true);
		camera.setDrawCameraOutline(true);
		camera.setDrawCollisionShape(true);
		camera.setDrawEntityPoints(true);
		engine.setCameraForInput(camera);
		// Set screen position or the camera is centered automatically
		// camera.setScreenPosition(new Point(0, 0));

		engine.addEntity(camera, e1, e2, e3, e4);
		engine.start();
	}

}

package com.github.schuettec.cobra2Dexamples.networking.textureEntity;

import static java.lang.String.valueOf;

import java.net.URL;
import java.util.Properties;

import com.badlogic.gdx.Input.Keys;
import com.github.schuettec.cobra2Dexamples.bouncingBalls.WallEntity;
import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape.RectanglePoint;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.network.client.ClientCamera;
import com.github.schuettec.cobra2d.network.client.Cobra2DClient;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class NetworkClientDemo {

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
		properties.put(Cobra2DProperties.MAP_UPDATE, "false");
		properties.put(Cobra2DProperties.RENDERER, RendererType.LIBGDX.toString());
		properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
		properties.put(Cobra2DProperties.RESOURCE_LOCATION, "classpath");

		Cobra2DEngine engine = new Cobra2DEngine(properties);
		engine.addImage("floor", new URL("resource:floor.png"));
		engine.addImage("car", new URL("resource:cars/Audi.png"));

		engine.initialize();

		Dimension cameraDimension = new Dimension(1920, 1080);
		ClientCamera camera = new ClientCamera(new Point(0, 0), cameraDimension, false);
		camera.addKeyCodeToListen(Keys.LEFT);
		camera.addKeyCodeToListen(Keys.UP);
		camera.addKeyCodeToListen(Keys.RIGHT);
		camera.addKeyCodeToListen(Keys.DOWN);
		camera.setPositionByPoint(RectanglePoint.BL, new Point(0, 0));
		camera.setDrawCameraOutline(true);
		camera.setDrawCollisionShape(true);
		camera.setDrawEntityPoints(true);
		engine.addEntity(camera);

		Cobra2DClient client = new Cobra2DClient(engine);
		client.addEntityCreator(WallEntity.class, () -> new WallEntity(new Point(), new Dimension()));
		client.addEntityCreator(RotatingTextureEntity.class,
		    () -> new RotatingTextureEntity("car", new Point(), new Dimension(), 4, false));
		client.addEntityCreator(TexturedEntity.class, () -> {
			return new TexturedEntity("floor", new Point(), new Dimension(), 0, false);
		});
		client.connect("localhost");

		engine.start();
	}
}

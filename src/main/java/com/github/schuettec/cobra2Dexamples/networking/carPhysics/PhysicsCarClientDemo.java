package com.github.schuettec.cobra2Dexamples.networking.carPhysics;

import static java.lang.String.valueOf;

import java.net.URL;
import java.util.Properties;

import com.badlogic.gdx.Input.Keys;
import com.github.schuettec.cobra2Dexamples.libgdx.physics.bouncingballs.PhysicsWallEntity;
import com.github.schuettec.cobra2Dexamples.libgdx.physics.car.PhysxPoliceCarEntity;
import com.github.schuettec.cobra2Dexamples.libgdx.sound.RadioEntity;
import com.github.schuettec.cobra2Dexamples.textureRendering.TexturedEntity;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.skills.HasCollisionShape.RectanglePoint;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.network.client.ClientCamera;
import com.github.schuettec.cobra2d.network.client.Cobra2DClient;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class PhysicsCarClientDemo {

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
		engine.addImage("police", new URL("resource:cars/police.png"));
		engine.addImage("police-red-alarm-light", new URL("resource:cars/police-red-alarm-light.png"));
		engine.addImage("police-blue-alarm-light", new URL("resource:cars/police-blue-alarm-light.png"));
		engine.addImage("police-red-light", new URL("resource:cars/police-red-light.png"));
		engine.addImage("police-blue-light", new URL("resource:cars/police-blue-light.png"));
		engine.addImage("front-light", new URL("resource:cars/front-light.png"));
		engine.addImage("brake-light", new URL("resource:cars/brake-light.png"));
		engine.addImage("brake-light-color", new URL("resource:cars/brake-light-color.png"));
		engine.addImage("light", new URL("resource:light.png"));
		engine.addImage("radio", new URL("resource:radio.png"));

		engine.addSound("sound", new URL("resource:sounds/sound.ogg"));
		engine.initialize();

		Dimension cameraDimension = new Dimension(1920, 1080);
		ClientCamera camera = new ClientCamera(new Point(0, 0), cameraDimension, false);
		camera.addKeyCodeToListen(Keys.LEFT);
		camera.addKeyCodeToListen(Keys.UP);
		camera.addKeyCodeToListen(Keys.RIGHT);
		camera.addKeyCodeToListen(Keys.DOWN);
		camera.setPositionByPoint(RectanglePoint.BL, new Point(0, 0));
		engine.addEntity(camera);

		Cobra2DClient client = new Cobra2DClient(engine);
		client.addEntityCreator(PhysicsWallEntity.class, () -> new PhysicsWallEntity(new Point(), new Dimension()));
		client.addEntityCreator(TexturedEntity.class,
		    () -> new TexturedEntity("floor", new Point(), new Dimension(), 4, false));
		client.addEntityCreator(PhysxPoliceCarEntity.class, () -> {
			Dimension policeDimension = engine.dimensionOf("police");
			return new PhysxPoliceCarEntity("police", "police-red-alarm-light", "police-blue-alarm-light", "police-red-light",
			    "police-blue-light", "front-light", "brake-light", "brake-light-color", new Point(300, 300), policeDimension,
			    2, true);
		});
		client.addEntityCreator(RadioEntity.class, () -> {
			Dimension radioDimension = engine.dimensionOf("radio");
			return new RadioEntity("sound", 500, "radio", new Point(200, 200), radioDimension, 4, false);
		});
		client.setPlayerCamera(camera);

		String ip = args.length == 1 ? args[0] : "localhost";
		client.connect(ip);

		engine.start();
	}
}

package com.github.schuettec.cobra2Dexamples.networking.bullet;

import static java.lang.String.valueOf;

import java.net.URL;
import java.util.Properties;

import com.badlogic.gdx.Input.Keys;
import com.github.schuettec.cobra2Dexamples.libgdx.person.HarveyEntity;
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

public class PersonClientDemo {

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
		engine.addImage("walkcyle_harvey", new URL("resource:walkcyle_harvey.png"));
		engine.addImage("walkcyle_harvey_static", new URL("resource:walkcyle_harvey_static.png"));
		engine.addImage("head", new URL("resource:head.png"));
		engine.addImage("radio", new URL("resource:radio.png"));

		engine.addSound("sound", new URL("resource:sounds/sound.ogg"));

		engine.initialize();

		Dimension cameraDimension = new Dimension(1920, 1080);
		ClientCamera camera = new ClientCamera(new Point(0, 0), cameraDimension, false);
		camera.addKeyCodeToListen(Keys.W);
		camera.addKeyCodeToListen(Keys.S);
		camera.setPositionByPoint(RectanglePoint.BL, new Point(0, 0));
		engine.setCameraForInput(camera);
		engine.addEntity(camera);

		Cobra2DClient client = new Cobra2DClient(engine);
		client.addEntityCreator(HarveyEntity.class, () -> {
			Dimension harveysDimension = engine.dimensionOf("walkcyle_harvey_static");
			HarveyEntity h = new HarveyEntity("head", "walkcyle_harvey_static", "walkcyle_harvey", new Point(0, 0),
			    harveysDimension, 4, 4, 2, false);
			h.setLayer(5);
			return h;
		});
		client.addEntityCreator(TexturedEntity.class, () -> new TexturedEntity("floor", new Point(), new Dimension(), 4));
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

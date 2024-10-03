package com.github.schuettec.cobra2Dexamples.walkinganimation;

import java.util.Properties;

import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class WalkingEntityDemo {

  public static void main(String[] args) throws Exception {

    final Properties properties = new Properties();
    properties.put(Cobra2DProperties.RESOLUTION_X, "800"); // 1920
    properties.put(Cobra2DProperties.RESOLUTION_Y, "800"); // 1080
    properties.put(Cobra2DProperties.BIT_DEPHT, "32");
    properties.put(Cobra2DProperties.REFRESH_REATE, "59");
    properties.put(Cobra2DProperties.REQUESTED_FPS, "59");
    properties.put(Cobra2DProperties.FULLSCREEN, "false");
    properties.put(Cobra2DProperties.MAP_UPDATE, "true");
    properties.put(Cobra2DProperties.RENDERER,
        RendererType.LIBGDX.toString());
    properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
    properties.put(Cobra2DProperties.RESOURCE_LOCATION,
        "classpath");

    Cobra2DEngine engine = new Cobra2DEngine(properties);
    engine.initialize();

    WalkingEntity e = new WalkingEntity(new Point(300, 300), 200,
        true);

    BasicRectangleMapCamera camera = new BasicRectangleMapCamera(
        new Point(100, 100), new Dimension(800, 800), false);
    camera.setDrawCameraOutline(true);
    camera.setDrawCollisionShape(true);
    camera.setDrawEntityPoints(true);
    engine.setCameraForInput(camera);
    // Set screen position or the camera is centered
    // automatically
    // camera.setScreenPosition(new Point(0, 0));
    // camera.scale(0.5);

    engine.addEntity(e, camera);
    // engine.addEntity(camera);

    engine.start();
  }
}

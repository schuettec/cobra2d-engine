package com.github.schuettec.cobra2Dexamples.walkinganimation.withPhysics;

import java.util.Properties;

import com.github.schuettec.cobra2Dexamples.DemoUtils;
import com.github.schuettec.cobra2d.engine.Cobra2DEngine;
import com.github.schuettec.cobra2d.engine.Cobra2DProperties;
import com.github.schuettec.cobra2d.entity.camera.BasicRectangleMapCamera;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererType;

public class PhysicsWalkingDemo {
  public static void main(String[] args) {
    // 2560x1440, bpp: 24, hz: 165
    final Properties properties = new Properties();
    // DemoUtils.windowMode(properties);
    DemoUtils.windowMode(properties);
    properties.put(Cobra2DProperties.MAP_UPDATE, "true");
    properties.put(Cobra2DProperties.RENDERER,
        RendererType.LIBGDX.toString());
    properties.put(Cobra2DProperties.CREATE_CONTROLLER, "true");
    properties.put(Cobra2DProperties.RESOURCE_LOCATION,
        "classpath");

    Cobra2DEngine engine = new Cobra2DEngine(properties);
    engine.initialize();

    PhysicsWallEntity linkeWand = new PhysicsWallEntity(
        new Point(20, 400), new Dimension(20, 789));
    PhysicsWallEntity rechteWand = new PhysicsWallEntity(
        new Point(779, 400), new Dimension(20, 789));
    PhysicsWallEntity decke = new PhysicsWallEntity(
        new Point(400, 779), new Dimension(789, 20));
    PhysicsWallEntity boden = new PhysicsWallEntity(
        new Point(400, 19), new Dimension(789, 20));

    PhysicsWallEntity block = new PhysicsWallEntity(
        new Point(600, 100), new Dimension(200, 200));

    PhysicsWalkingEntity player = new PhysicsWalkingEntity(
        new Point(300, 200), 200, 90, 0);

    BasicRectangleMapCamera camera = new BasicRectangleMapCamera(
        new Point(400, 400), new Dimension(799, 799), false);
    camera.setDrawCameraOutline(true);
    camera.setDrawCollisionShape(true);
    camera.setDrawEntityPoints(true);
    camera.setDrawMouse(true);
    camera.setScreenPosition(new Point(400, 400));
    engine.setCameraForInput(camera);

    // Set screen position or the camera is centered
    // automatically
    // camera.setScreenPosition(new Point(0, 0));

    engine.addEntity(player, linkeWand, rechteWand, decke,
        boden);
    // block);
    engine.addEntity(camera);
    engine.setCameraForInput(camera);
    engine.start();
  }

}

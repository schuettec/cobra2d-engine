package com.github.schuettec.cobra2d.entity.skills;

import java.util.function.Function;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.ModelAccess;

public interface Renderable3D extends Renderable {

  void render3D(ModelAccess modelAccess, Environment environment, ModelBatch modelBatch,
      Function<Point, Point> worldTo3DScreen);
}

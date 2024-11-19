package com.github.schuettec.cobra2d.entity.skills;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.ModelAccess;

public interface Renderable3D extends Renderable {

  void render3D(ModelAccess modelAccess, Environment environment, ModelBatch modelBatch, Point screenTranslation);
}

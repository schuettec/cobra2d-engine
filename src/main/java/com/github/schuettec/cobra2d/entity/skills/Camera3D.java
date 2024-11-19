package com.github.schuettec.cobra2d.entity.skills;

import java.util.List;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.github.schuettec.cobra2d.renderer.ModelAccess;

public interface Camera3D extends Camera {
  public void render3D(ModelAccess modelAccess, Environment environment, ModelBatch modelBatch,
      List<Renderable3D> renderables3d);
}

package com.github.schuettec.cobra2d.entity.skills;

import java.util.List;

import com.badlogic.gdx.graphics.g3d.ModelBatch;

public interface Camera3D extends Camera {
	public void render3D(ModelBatch modelBatch, List<Renderable3D> renderables3d);
}

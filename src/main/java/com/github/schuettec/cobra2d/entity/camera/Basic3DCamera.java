package com.github.schuettec.cobra2d.entity.camera;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.github.schuettec.cobra2d.entity.skills.Renderable3D;
import com.github.schuettec.cobra2d.math.Dimension;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;
import com.github.schuettec.cobra2d.world.Collision;

public class Basic3DCamera extends BasicRectangleMapCamera {

	private PerspectiveCamera camera;
	private Environment environment;

	public Basic3DCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled, Point screenPosition) {
		super(worldCoordinates, dimension, playerControlled, screenPosition);
	}

	public Basic3DCamera(Point worldCoordinates, Dimension dimension, boolean playerControlled) {
		super(worldCoordinates, dimension, playerControlled);
	}

	@Override
	public void render(RendererAccess renderer, Cobra2DWorld map, List<Collision> capturedEntities) {
		super.render(renderer, map, capturedEntities);
	}

	public void render3D(ModelBatch modelBatch, List<Renderable3D> renderables3d) {
		// Clear the screen
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT | Gdx.gl.GL_DEPTH_BUFFER_BIT);

		// Render the model
		modelBatch.begin(camera);
		renderables3d.forEach(renderable -> renderable.render3D(environment));
		modelBatch.end();
	}

}

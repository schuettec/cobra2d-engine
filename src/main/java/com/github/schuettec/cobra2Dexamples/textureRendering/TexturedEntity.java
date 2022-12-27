package com.github.schuettec.cobra2Dexamples.textureRendering;

import java.awt.Dimension;

import com.github.schuettec.cobra2d.entity.BasicRectangleEntity;
import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public class TexturedEntity extends BasicRectangleEntity implements Renderable {

	private static final long serialVersionUID = 1L;

	public TexturedEntity(Point worldCoordinates, Dimension dimension) {
		super(worldCoordinates, dimension);
	}

	@Override
	public void render(RendererAccess renderer, Point position) {
		// renderer.drawTexture();
	}

	@Override
	public int getLayer() {
		return 0;
	}

}

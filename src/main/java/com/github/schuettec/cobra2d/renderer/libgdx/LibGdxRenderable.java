package com.github.schuettec.cobra2d.renderer.libgdx;

import com.github.schuettec.cobra2d.entity.skills.Renderable;
import com.github.schuettec.cobra2d.renderer.RendererAccess;

public interface LibGdxRenderable extends Renderable {

	public default LibGdxExtendedAccess getExtendedRenderer(RendererAccess rendererAccess) {
		return rendererAccess.extendedRenderer(LibGdxExtendedAccess.class);
	}

}

package com.github.schuettec.cobra2d.resource;

import static java.util.Objects.isNull;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.renderer.RendererAttached;

public class TextureMemory implements RendererAttached, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private transient HashMap<String, ResourceInfo> images;

	private RendererAccess renderer;

	public TextureMemory() {
		this.images = new HashMap<>();
	}

	@Override
	public void attachRenderer(RendererAccess renderer) {
		this.renderer = renderer;
		loadResources();
	}

	@Override
	public void detachRenderer() {
		disposeResources();
		this.renderer = null;
	}

	private void disposeResources() {
		images.values()
		    .stream()
		    .forEach(r -> r.getRendererResource()
		        .dispose());
	}

	private void loadResources() {
		images.values()
		    .stream()
		    .forEach(r -> renderer.loadTexture(r));
	}

	public ResourceInfo getImage(final String textureId) {
		denyDetached();
		ResourceInfo resource = images.get(textureId);
		return resource;
	}

	public void addImage(final String textureId, final URL ressourceURL) throws ImageMemoryException {
		ResourceInfo info = new ResourceInfo(textureId, ressourceURL);
		images.put(textureId, info);
	}

	public void removeImage(final String textureId) {
		images.remove(textureId);
	}

	private void denyDetached() {
		if (isNull(renderer)) {
			throw new IllegalStateException("This texture memory was not bound to an active renderer.");
		}
	}

	@Override
	public String toString() {
		String string = "ImageMemory has loaded following images:\n";
		final Iterator<String> keys = this.images.keySet()
		    .iterator();
		while (keys.hasNext()) {
			string += keys.next() + "\n";
		}

		return string;
	}

}

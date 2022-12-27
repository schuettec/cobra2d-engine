package com.github.schuettec.cobra2d.renderer;

import com.github.schuettec.cobra2d.resource.TextureMemory;

/**
 * This interface defines objects that do have a renderer related lifecycle:
 * <ul>
 * <li>Renderer detached: The object just holds information that are not resource consuming or in any other relation to
 * the renderer.</li>
 * <li>Renderer attached: The object interacts with a reference to the renderer and might be bound to ressources of the
 * rendering system.</li>
 * <ul>
 * 
 * An example of such an object is {@link TextureMemory}.
 */
public interface RendererAttached {

	/**
	 * Provides access to the {@link RendererAccess} and signals, that the renderer is available for operation.
	 * 
	 * @param renderer The renderer.
	 */
	public void attachRenderer(RendererAccess renderer);

	/**
	 * Signals that the renderer is now unavailable. The object should react in freeing all resources associated to the
	 * renderer and immediately stopping to communicate with the renderer.
	 */
	public void detachRenderer();

}

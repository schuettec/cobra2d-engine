package com.github.schuettec.cobra2d.renderer;

/**
 * A renderer resource is an object representing a texture, animation or other resource that is bound to graphic-,
 * sound-
 * or other subsystems.
 */
public interface RendererResource extends Disposable {

	<T> T getResource();
}

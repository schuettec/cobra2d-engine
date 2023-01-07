package com.github.schuettec.cobra2d.entity.skills;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Optional;

import com.github.schuettec.cobra2d.entity.camera.InputContext;
import com.github.schuettec.cobra2d.math.Point;
import com.github.schuettec.cobra2d.renderer.RendererAccess;
import com.github.schuettec.cobra2d.world.Cobra2DWorld;
import com.github.schuettec.cobra2d.world.Collision;
import com.github.schuettec.cobra2d.world.WorldAccess;

public interface Camera extends Updatable, HasCollisionShape {

	public String getFollowEntityId();

	public void setFollowEntityId(String followEntityId);

	public Entity getFollowEntity();

	public void setFollowEntity(Entity entity);

	/**
	 * Entity to follow.
	 * 
	 * @param followEntity Entity to follow.
	 */
	public void follow(Entity followEntity);

	/**
	 * Entity to follow by id.
	 * 
	 * @param followEntity Entity to follow by id.
	 */
	public void follow(String entityId);

	default Optional<Entity> getFollowEntity(WorldAccess worldAccess) {
		if (nonNull(getFollowEntity())) {
			return Optional.of(getFollowEntity());
		} else {
			if (nonNull(getFollowEntityId())) {
				return worldAccess.getEntityById(getFollowEntityId());
			}
		}
		return Optional.empty();
	}

	/**
	 * Called before {@link #render(RendererAccess, Cobra2DWorld, List)} to set/draw the clipping mask of this camera.
	 * 
	 * @param renderer The {@link RendererAccess}.
	 */
	void renderClippingMask(final RendererAccess renderer);

	/**
	 * Renders the scene with the specified captured entities.
	 * 
	 * @param renderer The {@link RendererAccess}.
	 * @param map The current map.
	 * @param capturedEntities The entities that colliding the viewport shape of
	 *        this camera.
	 */
	void render(final RendererAccess renderer, final Cobra2DWorld map, final List<Collision> capturedEntities);

	/**
	 * @return Returns the screen position of this camera.
	 */
	public Point getScreenPosition();

	/**
	 * Sets a new screen position for this camera.
	 * 
	 * @param screenPosition The new position-.
	 */
	public void setScreenPosition(Point screenPosition);

	/**
	 * Translates screen coordinates to world coordinates.
	 * 
	 * @param screenCoords The screen coordinates.
	 * @return Returns the {@link InputContext}.
	 */
	Point screenToWorldCoordinates(Point screenCoords);

	/**
	 * Translates worlds coordinates to screen coordinates.
	 * 
	 * @param worldCoords The worlds coordinates.
	 * @return Returns the {@link InputContext}.
	 */
	Point worldToScreenCoordinates(Point worldCoords);

}

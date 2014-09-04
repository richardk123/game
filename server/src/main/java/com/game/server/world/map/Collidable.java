package com.game.server.world.map;

/**
 * @author dohnal
 */

import com.game.server.world.geometry.AABB;

/**
 * Represents an object that can collide with other objects.
 */
public interface Collidable extends GameObject
{
	/**
	 * Get an AABB from this collidable.
	 */
	public AABB getAABB();
}

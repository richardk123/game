package com.game.server.world.map;

import com.game.server.world.geometry.AABB;

/**
 * Represents an object that can collide with other objects.
 */
public abstract class CollidableObject extends GameObject
{
	/**
	 * Get an AABB from this collidable.
	 */
	public abstract AABB getAABB();

}

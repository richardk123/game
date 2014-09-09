package com.game.server.world.material.base;

import com.game.server.world.geometry.AABB;

/**
 * @author dohnal
 */

/**
 * Materials define how game objects look like
 */
public interface Material
{
	/**
	 * Get bounding box
	 */
	public AABB getAABB();
}

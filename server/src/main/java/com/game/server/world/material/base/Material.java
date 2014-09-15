package com.game.server.world.material.base;

/**
 * @author dohnal
 */

import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Materials define how game objects look like
 */
public interface Material
{
	/**
	 * Get bounding box
	 */
	public Envelope getBoundingBox(double x, double y);

	/**
	 *  can gameObject go through this material?
	 */
	public boolean isPassable(GameObject gameObject);
}

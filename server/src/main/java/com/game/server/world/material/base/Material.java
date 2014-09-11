package com.game.server.world.material.base;

/**
 * @author dohnal
 */

import com.vividsolutions.jts.geom.Envelope;

/**
 * Materials define how game objects look like
 */
public interface Material
{
	/**
	 * Get bounding box
	 */
	public Envelope getBoundingBox();
}

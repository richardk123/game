package com.game.server.world.map;


/**
 * @author dohnal
 */

import com.game.server.world.geometry.Vector2;

/**
 * Represents an object that is movable.
 */
public interface Movable extends GameObject
{
	/**
	 * Moves the object the given amounts in the respective directions.
	 * @param x the translation in the x direction
	 * @param y the translation in the y direction
	 */
	public void move(double x, double y);

	/**
	 * Moves the object along the given vector.
	 * @param vector the translation along a vector
	 */
	public void move(Vector2 vector);
}

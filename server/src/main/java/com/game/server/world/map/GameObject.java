package com.game.server.world.map;

/**
 * @author dohnal
 */

import java.util.UUID;

/**
 * Represents all game objects.
 */
public interface GameObject
{
	public UUID getId();

	public double getX();

	public double getY();
}

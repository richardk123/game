package com.game.server.map.message;

/**
 * @author Richard Kolísek
 */
public class PositionDestroyMessage
{
	private final int id;

	public PositionDestroyMessage(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}
}

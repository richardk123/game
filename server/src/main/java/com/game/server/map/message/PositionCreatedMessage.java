package com.game.server.map.message;

/**
 * @author Richard Kolísek
 */
public class PositionCreatedMessage
{
	private final int id;

	public PositionCreatedMessage(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}
}

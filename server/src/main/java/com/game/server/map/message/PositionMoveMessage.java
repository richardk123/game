package com.game.server.map.message;

import com.game.server.map.data.Point;

/**
 * @author Richard Kolísek
 */
public class PositionMoveMessage
{
	private final int id;
	private final Point from;
	private final Point to;

	public PositionMoveMessage(int id, Point from, Point to)
	{
		this.id = id;
		this.from = from;
		this.to = to;
	}

	public int getId()
	{
		return id;
	}

	public Point getFrom()
	{
		return from;
	}

	public Point getTo()
	{
		return to;
	}
}

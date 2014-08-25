package com.game.server.map.message;

import com.game.server.map.data.Point;

/**
 * @author Richard Kolísek
 */
public class PositionCreateMessage
{
	private final int id;
	private final Point point;

	public PositionCreateMessage(int id, Point point)
	{
		this.id = id;
		this.point = point;
	}

	public int getId()
	{
		return id;
	}

	public Point getPoint()
	{
		return point;
	}
}

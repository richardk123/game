package com.game.server.map.message;

import com.game.server.map.data.Point;

/**
 * @author Richard Kolísek
 */
public class PositionCreateMessage
{
	private final int id;
	private final Point point;
	private final boolean needReply;

	public PositionCreateMessage(int id, Point point, boolean needReply)
	{
		this.id = id;
		this.point = point;
		this.needReply = needReply;
	}

	public int getId()
	{
		return id;
	}

	public Point getPoint()
	{
		return point;
	}

	public boolean isNeedReply()
	{
		return needReply;
	}
}

package com.game.server.map.message;

import java.util.Set;

import com.game.server.map.data.Square;

/**
 * @author Richard Kolísek
 */
public class QueryMessage
{

	private final Square findPointsInSquare;
	private ResultCallback resultCallback;

	public QueryMessage(Square findPointsInSquare, ResultCallback resultCallback)
	{
		this.findPointsInSquare = findPointsInSquare;
		this.resultCallback = resultCallback;
	}

	public Square getFindPointsInSquare()
	{
		return findPointsInSquare;
	}

	public ResultCallback getResultCallback()
	{
		return resultCallback;
	}

	public void setResultCallback(ResultCallback resultCallback)
	{
		this.resultCallback = resultCallback;
	}

	public interface ResultCallback
	{
		void resultCallback(Set<Integer> ids);
	}
}

package com.game.server.map.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Richard Kolísek
 */
public class Square
{
	private Point topLeftCorner;
	private Point topRightCorner;
	private Point bottomLeftCorner;
	private Point bottomRightCorner;
	private float squareSize;

	public Square(Point topCorner, float squareSize)
	{
		this.topLeftCorner = topCorner;
		this.topRightCorner = new Point(topCorner.getX() + squareSize, topCorner.getY());
		this.bottomLeftCorner = new Point(topCorner.getX(), topCorner.getY() + squareSize);
		this.bottomRightCorner = new Point(topCorner.getX() + squareSize, topCorner.getY() + squareSize);
		this.squareSize = squareSize;
	}

	public Point getTopLeftCorner()
	{
		return topLeftCorner;
	}

	public void setTopLeftCorner(Point topLeftCorner)
	{
		this.topLeftCorner = topLeftCorner;
	}

	public float getSquareSize()
	{
		return squareSize;
	}

	public void setSquareSize(float squareSize)
	{
		this.squareSize = squareSize;
	}

	public boolean isInSquare(Point point)
	{
		return topLeftCorner.getX() <= point.getX() && topLeftCorner.getY() <= point.getY() &&
				topLeftCorner.getX() + squareSize >= point.getX() && topLeftCorner.getY() + squareSize >= point.getY();
	}

	public boolean isInSquare(Square square)
	{
		for (Point point : square.getAllCorners())
		{
			if (isInSquare(point))
			{
				return true;
			}
		}

		return false;
	}

	public Point getTopRightCorner()
	{
		return topRightCorner;
	}

	public void setTopRightCorner(Point topRightCorner)
	{
		this.topRightCorner = topRightCorner;
	}

	public Point getBottomLeftCorner()
	{
		return bottomLeftCorner;
	}

	public void setBottomLeftCorner(Point bottomLeftCorner)
	{
		this.bottomLeftCorner = bottomLeftCorner;
	}

	public Point getBottomRightCorner()
	{
		return bottomRightCorner;
	}

	public void setBottomRightCorner(Point bottomRightCorner)
	{
		this.bottomRightCorner = bottomRightCorner;
	}

	public List<Point> getAllCorners()
	{
		List<Point> points = new ArrayList<>();
		points.add(topLeftCorner);
		points.add(topRightCorner);
		points.add(bottomLeftCorner);
		points.add(bottomRightCorner);
		return points;
	}
}

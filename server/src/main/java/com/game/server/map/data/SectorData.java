package com.game.server.map.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Richard Kolísek
 */
public class SectorData
{
	private int id;
	private Square square;
	private Map<Integer, Point> idPosMap = new HashMap<>();

	public SectorData(int id, Square square)
	{
		this.id = id;
		this.square = square;
	}

	public boolean isInSector(Point point)
	{
		return square.isInSquare(point);
	}

	public List<SectorData> splitSector(int times)
	{
		if (times % 2 != 0)
		{
			throw new RuntimeException("times must be / 2");
		}

		List<SectorData> result = new ArrayList<>();

		int count = 0;
		for (int x = 0; x < times; x++)
		{
			for (int y = 0; y < times; y++)
			{
				count ++;

				float newSquareSize = getSquare().getSquareSize() / times;

				Point newTopCorner = new Point();
				newTopCorner.setX(getSquare().getTopLeftCorner().getX() + (x * newSquareSize));
				newTopCorner.setY(getSquare().getTopLeftCorner().getY() + (y * newSquareSize));

				Square newSquare = new Square(newTopCorner, newSquareSize);

				result.add(new SectorData(id + count, newSquare));
			}
		}

		for (Integer key : idPosMap.keySet())
		{
			Point point = idPosMap.get(key);
			for (SectorData newSector : result)
			{
				boolean inSector = newSector.isInSector(point);

				if (inSector)
				{
					newSector.getIdPosMap().put(key, point);
					break;
				}
			}
		}

		this.idPosMap = new HashMap<>();

		return result;
	}


	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Map<Integer, Point> getIdPosMap()
	{
		return idPosMap;
	}

	public void setIdPosMap(Map<Integer, Point> idPosMap)
	{
		this.idPosMap = idPosMap;
	}

	public Square getSquare()
	{
		return square;
	}

	public void setSquare(Square square)
	{
		this.square = square;
	}
}

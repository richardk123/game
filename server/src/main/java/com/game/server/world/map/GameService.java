package com.game.server.world.map;

import com.game.server.world.behavior.ViewBehaviour;
import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author Richard Kolísek
 */
public class GameService
{
	private static GameService gameService;
	private WorldMap<GameObject> worldCollisionMap;
	private WorldMap<ViewBehaviour> worldViewMap;

	public synchronized static GameService get()
	{
		if (gameService == null)
		{
			gameService = new GameService();
		}

		return gameService;
	}

	public synchronized static GameService getNew()
	{
		gameService = new GameService();
		return gameService;
	}

	public WorldMap<GameObject> getWorldCollisionMap()
	{
		if (worldCollisionMap == null)
		{
			worldCollisionMap = new GameObjectMap<GameObject>(
					new WorldMap.MapAdapter<GameObject>()
					{
						@Override
						public Envelope getEnvelope(GameObject value)
						{
							return value.getBoundingBox();
						}
					}
			);
		}
		return worldCollisionMap;
	}

	public WorldMap<ViewBehaviour> getWorldViewMap()
	{
		if (worldViewMap == null)
		{
			worldViewMap = new GameObjectMap<ViewBehaviour>(
					new WorldMap.MapAdapter<ViewBehaviour>()
					{
						@Override
						public Envelope getEnvelope(ViewBehaviour value)
						{
							return value.getViewBox();
						}
					}
			);
		}
		return worldViewMap;
	}

}

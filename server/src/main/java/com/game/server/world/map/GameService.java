package com.game.server.world.map;

/**
 * @author Richard Kolísek
 */
public class GameService
{
	private static GameService gameService;
	private WorldMap<CollidableObject> worldMap;

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

	public WorldMap<CollidableObject> getWorldMap()
	{
		if (worldMap == null)
		{
			worldMap = new DynamicAABBTree<>();
		}
		return worldMap;
	}

}

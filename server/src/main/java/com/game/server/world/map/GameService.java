package com.game.server.world.map;

import com.game.server.world.object.base.GameObject;

/**
 * @author Richard Kolísek
 */
public class GameService
{
	private static GameService gameService;

	private WorldMap<GameObject> worldMap;

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

	private GameService()
	{
		worldMap = new SynchronizedWorldMap();
	}

	public WorldMap<GameObject> getWorldMap()
	{
		return worldMap;
	}
}

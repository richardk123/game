package com.game.server.world.service;

import com.game.server.world.map.SynchronizedWorldMap;
import com.game.server.world.map.WorldMap;
import com.game.server.world.object.base.GameObject;

/**
 * @author Richard Kolísek
 */
public class GameService
{
	private static GameService gameService;

	private WorldMap<GameObject> worldMap;

	private IdGeneratorService idGenerator;

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
		idGenerator = new IdGeneratorService();
	}

	public WorldMap<GameObject> getWorldMap()
	{
		return worldMap;
	}

	public IdGeneratorService getIdGenerator()
	{
		return idGenerator;
	}
}

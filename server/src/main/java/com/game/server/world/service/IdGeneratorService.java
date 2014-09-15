package com.game.server.world.service;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author dohnal
 */
public class IdGeneratorService
{
	private Map<Class<?>, Long> idMap;

	public IdGeneratorService()
	{
		idMap = Maps.newConcurrentMap();
	}

	public Long generateId(Class<?> gameObjectType)
	{
		if (idMap.containsKey(gameObjectType))
		{
			Long id = idMap.get(gameObjectType) + 1;

			idMap.put(gameObjectType, id);

			return id;
		}
		else
		{
			idMap.put(gameObjectType, 1L);

			return 1L;
		}
	}
}

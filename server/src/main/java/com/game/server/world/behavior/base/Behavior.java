package com.game.server.world.behavior.base;

import com.game.server.world.map.GameService;
import com.game.server.world.map.WorldMap;
import com.game.server.world.object.base.GameObject;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * @author dohnal
 */
public abstract class Behavior
{
	private GameObject self;
	private GameObject currentSender;
	private PartialFunction<Object, BoxedUnit> behaviour;

	public Behavior(final GameObject self)
	{
		this.self = self;
	}

	public boolean isDefinedAt(final Object message)
	{
		return behaviour != null && behaviour.isDefinedAt(message);
	}

	public void apply(final Object message)
	{
		behaviour.apply(message);
	}

	protected void onCreated()
	{

	}

	protected void onDestroyed()
	{

	}

	protected GameObject getSender()
	{
		return currentSender;
	}

	protected GameObject getSelf()
	{
		return self;
	}

	protected WorldMap<GameObject> getWorld()
	{
		return GameService.get().getWorldMap();
	}

	protected void behaviour(PartialFunction<Object, BoxedUnit> behaviour)
	{
		this.behaviour = behaviour;
	}

	public void setCurrentSender(GameObject currentSender)
	{
		this.currentSender = currentSender;
	}
}

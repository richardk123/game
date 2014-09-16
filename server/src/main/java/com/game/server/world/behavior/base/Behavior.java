package com.game.server.world.behavior.base;

import com.game.server.world.map.WorldMap;
import com.game.server.world.object.base.GameObject;
import com.game.server.world.service.GameService;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import javax.annotation.Nonnull;

/**
 * @author dohnal
 */
public abstract class Behavior
{
	private GameObject self;
	private GameObject currentSender;
	private PartialFunction<Object, BoxedUnit> behaviour;

	protected Behavior()
	{ }

	public static <T extends Behavior> T create(final @Nonnull GameObject self, final @Nonnull BehaviorProps<T> props)
	{
		T behavior = props.create();

		behavior.setSelf(self);

		return behavior;
	}

	public boolean isDefinedAt(final Object message)
	{
		return behaviour != null && behaviour.isDefinedAt(message);
	}

	public void apply(final Object message)
	{
		behaviour.apply(message);
	}

	public void onCreate()
	{ }

	public void onDestroy()
	{ }

	protected void setSelf(GameObject self)
	{
		this.self = self;
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

	public static final class AddBehaviorMessage extends Message
	{
		private BehaviorProps behaviorProps;

		public AddBehaviorMessage(BehaviorProps behaviorProps)
		{
			this.behaviorProps = behaviorProps;
		}

		public BehaviorProps getBehaviorProps()
		{
			return behaviorProps;
		}
	}

	public static final class RemoveBehaviorMessage extends Message
	{
		private Class<? extends Behavior> behaviorType;

		public RemoveBehaviorMessage(Class<? extends Behavior> behaviorType)
		{
			this.behaviorType = behaviorType;
		}

		public Class<? extends Behavior> getBehavior()
		{
			return behaviorType;
		}
	}
}

package com.game.server.world.behavior.base;

import javax.annotation.Nonnull;

/**
 * @author dohnal
 */
public class BehaviorProps<T extends Behavior>
{
	private final BehaviorCreator<T> creator;

	public static <T extends Behavior> BehaviorProps<T> create(final @Nonnull BehaviorCreator<T> creator)
	{
		return new BehaviorProps<>(creator);
	}

	private BehaviorProps(final @Nonnull BehaviorCreator<T> creator)
	{
		this.creator = creator;
	}

	public T create()
	{
		return creator.create();
	}

	public interface BehaviorCreator<T extends Behavior>
	{
		public T create();
	}
}

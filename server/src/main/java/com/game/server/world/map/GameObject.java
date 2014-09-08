package com.game.server.world.map;

/**
 * @author dohnal
 */

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.Message;
import com.game.server.world.geometry.AABB;
import com.game.server.world.geometry.Vector2;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Represents all game objects.
 */
public abstract class GameObject
{
	static final Logger LOG = Logger.getLogger(GameObject.class);

	private UUID id;
	private Vector2 position;

	private List<Behavior> behaviors;

	public GameObject()
	{
		this.id = UUID.randomUUID();

		behaviors = getBehaviours();
	}

	public UUID getId()
	{
		return id;
	}

	public double getX()
	{
		return position.getX();
	}

	public double getY()
	{
		return position.getY();
	}

	/**
	 * Get an AABB from this collidable.
	 */
	@Nullable
	public abstract AABB getAABB();

	public Vector2 getPosition()
	{
		return position;
	}

	public void setPosition(Vector2 position)
	{
		this.position = position;
	}

	@Nullable
	protected abstract List<Behavior> getBehaviours();

	@Nullable
	@SuppressWarnings("unchecked")
	public <T extends Behavior> T getBehavior(Class<T> behavior)
	{
		if (behaviors != null)
		{
			for (Behavior b : behaviors)
			{
				if (b.getClass().equals(behavior))
				{
					return (T)b;
				}
			}
		}

		return null;
	}

	public void tell(@Nonnull Message message, @Nullable GameObject sender)
	{
		for (Behavior behavior : behaviors)
		{
			if (behavior.isDefinedAt(message))
			{
				behavior.setCurrentSender(sender);

				behavior.apply(message);
			}
		}
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + ":" + getId().toString();
	}

}

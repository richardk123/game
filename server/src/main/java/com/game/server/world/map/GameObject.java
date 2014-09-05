package com.game.server.world.map;

/**
 * @author dohnal
 */

import java.util.List;
import java.util.UUID;

import com.game.server.world.geometry.AABB;
import com.game.server.world.geometry.Vector2;
import com.game.server.world.map.behaviour.Behavior;
import com.game.server.world.map.behaviour.Message;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.apache.log4j.Logger;

/**
 * Represents all game objects.
 */
public abstract class GameObject
{

	static final Logger LOG = Logger.getLogger(GameObject.class);

	private UUID id;
	private Vector2 position;

	public GameObject()
	{
		this.id = UUID.randomUUID();
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


	public void tell(@NotNull Message message, @Nullable GameObject sender)
	{
		List<Behavior> behaviors = getBehaviours();

		boolean behaviourFound = false;

		for (Behavior behavior : behaviors)
		{
			if (behavior.getMessageType().isAssignableFrom(message.getClass()))
			{
				behaviourFound = true;
				LOG.info(
						String.format("behaviour found! message %s from: %s to: %s",
								message.getClass().getSimpleName(),
								sender == null ? "null" : sender.toString(),
								this.toString())
				);
				behavior.behave(message, sender, this);
			}
		}

		if (!behaviourFound)
		{
			LOG.info(
					String.format("behaviour not found! message %s from: %s to: %s",
							message.getClass().getSimpleName(),
							sender == null ? "null" : sender.toString(),
							this.toString())
			);
		}
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + ":" + getId().toString();
	}

}

package com.game.server.world.behavior;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.BehaviorProperty;
import com.game.server.world.behavior.base.Message;
import com.game.server.world.object.base.GameObject;
import com.game.server.world.service.GameService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author dohnal
 */
public abstract class MoveBehaviour extends Behavior
{
	private static final Logger LOG = Logger.getLogger(MoveBehaviour.class);

	private BehaviorProperty<Double> movementSpeed;

	/**
	 * Object I currently collide with
	 */
	private List<GameObject> collisionObjects = Lists.newArrayList();

	public MoveBehaviour(GameObject self, double movementSpeed)
	{
		super(self);

		this.movementSpeed = new BehaviorProperty<>(movementSpeed);
	}

	protected void moveHandler(final MoveMessage moveMessage)
	{

		// find objects on next move
		List<GameObject> objects = GameService.get().getWorldMap().findObjects(getSelf().getCollisionBox(
				getSelf().getX() + moveMessage.getX(),
				getSelf().getY() + moveMessage.getY()));

		List<GameObject> notPassableObjects = Lists.newArrayList();

		for (GameObject object : objects)
		{

			// don't collide with myself
			if (object.equals(getSelf()))
			{
				continue;
			}

			// can i pass through this object?
			if (!object.getMaterial().isPassable(getSelf()))
			{
				notPassableObjects.add(object);
			}

			// collision enter
			if (!collisionObjects.contains(object))
			{
				LOG.debug(getSelf() + " - collision enter with " + object);

				object.tell(new CollisionEnterMessage(getSelf()), getSelf());
			}

			// collision
			object.tell(new CollisionMessage(getSelf()), getSelf());
		}

		// collision leave
		for (GameObject object : collisionObjects)
		{
			if (!objects.contains(object))
			{
				LOG.debug(getSelf() + " - collision leave with " + object);

				object.tell(new CollisionLeaveMessage(getSelf()), getSelf());
			}
		}

		// move my self
		if (CollectionUtils.isEmpty(notPassableObjects))
		{
			getSelf().move(moveMessage.getX(), moveMessage.getY());
			GameService.get().getWorldMap().update(getSelf());
		}
		else
		{
			//collision emerges, move to collision point
			collisionObjects = Lists.newArrayList();
		}

		collisionObjects = objects;
	}

	public BehaviorProperty<Double> getMovementSpeed()
	{
		return movementSpeed;
	}

	public static class MoveMessage extends Message
	{
		private final double x;
		private final double y;

		public MoveMessage(double x, double y)
		{
			this.x = x;
			this.y = y;
		}

		public double getX()
		{
			return x;
		}

		public double getY()
		{
			return y;
		}
	}

	public static class CollisionMessage extends Message
	{
		protected GameObject collisionObject;

		public CollisionMessage(GameObject collisionObject)
		{
			this.collisionObject = collisionObject;
		}

		public GameObject getCollisionObject()
		{
			return collisionObject;
		}
	}

	public static class CollisionEnterMessage extends CollisionMessage
	{
		public CollisionEnterMessage(GameObject collisionObject)
		{
			super(collisionObject);
		}
	}

	public static class CollisionLeaveMessage extends CollisionMessage
	{
		public CollisionLeaveMessage(GameObject collisionObject)
		{
			super(collisionObject);
		}
	}
}

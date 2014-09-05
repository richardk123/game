package com.game.server.world.map.behaviour;


/**
 * @author dohnal
 */

import java.util.List;

import com.game.server.world.geometry.Vector2;
import com.game.server.world.map.CollidableObject;
import com.game.server.world.map.GameObject;
import com.game.server.world.map.GameService;
import com.game.server.world.map.WorldMap;

/**
 * Represents an object that is movable.
 */
public class MoveBehaviour implements Behavior<MoveBehaviour.MoveMessage, GameObject>
{

	@Override
	public Class<MoveMessage> getMessageType()
	{
		return MoveMessage.class;
	}

	@Override
	public void behave(MoveMessage message, GameObject sender, GameObject self)
	{

		// tell others that i collide with them
		if (self instanceof CollidableObject)
		{
			CollidableObject collidable = (CollidableObject) self;

			WorldMap<CollidableObject> worldMap = GameService.get().getWorldMap();
			List<CollidableObject> collisions = worldMap.find(collidable.getAABB());

			for (CollidableObject collision : collisions)
			{
				collision.tell(new CollideBehaviour.CollideMessage(), self);
			}
		}

		// move my self
		if (message.hasVector())
		{
			self.getPosition().add(message.getVector());
		}
		else
		{
			self.getPosition().add(message.getX(), message.getY());
		}

		// update him in collidable world map
		if (self instanceof CollidableObject)
		{
			GameService.get().getWorldMap().update((CollidableObject) self);
		}


	}

	public static class MoveMessage implements Message
	{

		private final Vector2 vector;
		private final Double x;
		private final Double y;

		public MoveMessage(Vector2 vector)
		{
			this.vector = vector;
			this.x = null;
			this.y = null;
		}

		public MoveMessage(double x, double y)
		{
			this.x = x;
			this.y = y;
			this.vector = null;
		}

		public Vector2 getVector()
		{
			return vector;
		}

		private boolean hasVector()
		{
			return vector != null;
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

}

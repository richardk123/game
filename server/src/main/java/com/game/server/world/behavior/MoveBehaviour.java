package com.game.server.world.behavior;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.Message;
import com.game.server.world.map.GameService;
import com.game.server.world.object.base.GameObject;

/**
 * @author dohnal
 */
public abstract class MoveBehaviour extends Behavior
{
	public MoveBehaviour(GameObject self)
	{
		super(self);
	}

	protected void moveHandler(final MoveMessage moveMessage)
	{
		getSelf().move(moveMessage.getX(), moveMessage.getY());

		GameService.get().getWorldCollisionMap().update(getSelf());

		getSelf().tell(new PositionChangedMessage(), getSelf());
	}

	public static class PositionChangedMessage extends Message
	{

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

	public static class CollisionEnterMessage extends Message
	{

	}

	public static class CollisionMessage
	{

	}

	public static class CollisionLeaveMessage
	{

	}
}

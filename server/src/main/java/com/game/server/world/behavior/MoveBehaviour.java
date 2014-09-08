package com.game.server.world.behavior;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.Message;
import com.game.server.world.geometry.Vector2;
import com.game.server.world.map.GameObject;
import com.game.server.world.map.GameService;

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
		getSelf().getPosition().add(moveMessage.getX(), moveMessage.getY());

		GameService.get().getWorldMap().update(getSelf());
	}

	public static class MoveMessage extends Message
	{
		private final Vector2 move;

		public MoveMessage(Vector2 move)
		{
			this.move = move;
		}

		public MoveMessage(double x, double y)
		{
			move = new Vector2(x, y);
		}

		public double getX()
		{
			return move.getX();
		}

		public double getY()
		{
			return move.getY();
		}
	}
}

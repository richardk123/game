package com.game.server.world.behavior.internal;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.Message;
import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author Richard Kolísek
 */
public final class ViewBehaviour extends Behavior
{
	private int width;
	private int height;

	public ViewBehaviour(GameObject self, int width, int height)
	{
		super(self);

		this.width = width;
		this.height = height;
	}

	public Envelope getViewBox()
	{
		return new Envelope(
				getSelf().getX() - width * 0.5, getSelf().getX() + width * 0.5,
				getSelf().getY() - height * 0.5, getSelf().getY() + height * 0.5);
	}

	public static class ViewMessage extends InternalMessage
	{
		private final Message message;

		public ViewMessage(Message message)
		{
			this.message = message;
		}

		public Message getMessage()
		{
			return message;
		}
	}
}

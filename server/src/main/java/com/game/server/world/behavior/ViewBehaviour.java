package com.game.server.world.behavior;

import java.util.ArrayList;
import java.util.List;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.BehaviorBuilder;
import com.game.server.world.behavior.base.Message;
import com.game.server.world.map.GameService;
import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author Richard Kolísek
 */
public class ViewBehaviour extends Behavior
{

	private final Envelope viewBox;
	private List<ViewChangedMessage> mineViewMessages = new ArrayList<>();

	public ViewBehaviour(GameObject self, Envelope viewBox)
	{
		super(self);
		this.viewBox = viewBox;

		behaviour(BehaviorBuilder.match(Message.class, this::handleMessage)
				.match(ViewChangedMessage.class, this::handleViewChangedMessage)
				.build());
	}

	protected void handleMessage(Message message)
	{

		// handle only mine messages
		if (!getSender().equals(getSelf()) || message instanceof ViewChangedMessage)
		{
			return;
		}

		List<ViewBehaviour> views = GameService.get().getWorldViewMap().find(viewBox);

		// send to all object that i see mine messages
		for (ViewBehaviour go : views)
		{
			GameObject gameObject = go.getSelf();

			// do not send this message to me
			if (getSelf().equals(gameObject))
			{
				continue;
			}
			gameObject.tell(new ViewChangedMessage(message), getSelf());
		}
	}

	protected void handleViewChangedMessage(ViewChangedMessage message)
	{
		this.mineViewMessages.add(message);
	}

	public List<ViewChangedMessage> getMineViewMessages()
	{
		return mineViewMessages;
	}

	public Envelope getViewBox()
	{
		return viewBox;
	}

	/**
	 * view has changed
	 */
	public static class ViewChangedMessage extends Message
	{
		private final Message message;

		public ViewChangedMessage(Message message)
		{
			this.message = message;
		}

		public Message getMessage()
		{
			return message;
		}
	}
}

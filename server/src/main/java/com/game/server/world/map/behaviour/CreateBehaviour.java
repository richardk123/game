package com.game.server.world.map.behaviour;

import com.game.server.world.geometry.Vector2;
import com.game.server.world.map.GameObject;
import com.game.server.world.map.GameService;

/**
 * @author Richard Kolísek
 */
public class CreateBehaviour implements Behavior<CreateBehaviour.CreateMessage, GameObject>
{
	@Override
	public Class<CreateMessage> getMessageType()
	{
		return CreateMessage.class;
	}

	@Override
	public void behave(CreateMessage message, GameObject sender, GameObject self)
	{
		self.setPosition(message.getVector2());

		//add to collidable world map
		GameService.get().getWorldMap().add(self);
	}

	public static class CreateMessage implements Message
	{
		private final Vector2 vector2;

		public CreateMessage(Vector2 vector2)
		{
			this.vector2 = vector2;
		}

		public Vector2 getVector2()
		{
			return vector2;
		}
	}
}

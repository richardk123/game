package com.game.server.world.map.behaviour;

import com.game.server.world.map.GameObject;
import com.game.server.world.map.GameService;

/**
 * @author Richard Kolísek
 */
public class RemoveBehaviour implements Behavior<RemoveBehaviour.RemoveMessage, GameObject>
{

	@Override
	public Class<RemoveMessage> getMessageType()
	{
		return RemoveMessage.class;
	}

	@Override
	public void behave(RemoveMessage message, GameObject sender, GameObject self)
	{
		//add to collidable world map
		GameService.get().getWorldMap().remove(self);
	}

	public static class RemoveMessage implements Message
	{

	}

}

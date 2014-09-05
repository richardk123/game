package com.game.server.world.map.behaviour;

import com.game.server.world.map.GameObject;

/**
 * @author Richard Kolísek
 */
public class CollideBehaviour implements Behavior<CollideBehaviour.CollideMessage, GameObject>
{

	@Override
	public Class<CollideMessage> getMessageType()
	{
		return CollideMessage.class;
	}

	@Override
	public void behave(CollideMessage message, GameObject sender, GameObject self)
	{

	}

	public static class CollideMessage implements Message
	{

	}

}

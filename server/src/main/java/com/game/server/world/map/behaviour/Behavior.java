package com.game.server.world.map.behaviour;

import com.game.server.world.map.GameObject;

/**
 * @author Richard Kolísek
 */
public interface Behavior<T extends Message, K extends GameObject>
{
	/**
	 * type of deliverable behaviour
	 */
	Class<T> getMessageType();

	/**
	 *  what to do when behaviour is received
	 */
	void behave(T message, GameObject sender, K self);
}

package com.game.server.world.behavior;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.BehaviorBuilder;
import com.game.server.world.behavior.debuff.MovementDecreaseDebuff;
import com.game.server.world.object.base.GameObject;

/**
 * @author dohnal
 */
public class SlowBehavior extends Behavior
{
	/**
	 * Decrease speed in percentage
	 */
	private double decreaseSpeed;

	public SlowBehavior(GameObject self, double decreaseSpeed)
	{
		super(self);

		this.decreaseSpeed = decreaseSpeed;

		behaviour(BehaviorBuilder
				.match(MoveBehaviour.CollisionEnterMessage.class, m -> onCollisionEnter(m.getCollisionObject()))
				.match(MoveBehaviour.CollisionLeaveMessage.class, m -> onCollisionLeave(m.getCollisionObject()))
				.build());
	}

	private void onCollisionEnter(GameObject object)
	{
		if (object.hasBehavior(MoveBehaviour.class))
		{
			object.tell(new AddBehaviorMessage(new MovementDecreaseDebuff(object, decreaseSpeed)), getSelf());
		}
	}

	private void onCollisionLeave(GameObject object)
	{
		if (object.hasBehavior(MoveBehaviour.class))
		{
			object.tell(new RemoveBehaviorMessage(MovementDecreaseDebuff.class), getSelf());
		}
	}
}

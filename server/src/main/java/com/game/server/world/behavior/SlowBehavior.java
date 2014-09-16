package com.game.server.world.behavior;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.BehaviorBuilder;
import com.game.server.world.behavior.base.BehaviorProps;
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

	public static BehaviorProps<SlowBehavior> props(double decreaseSpeed)
	{
		return BehaviorProps.create(() -> new SlowBehavior(decreaseSpeed));
	}

	protected SlowBehavior(double decreaseSpeed)
	{
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
			object.tell(new AddBehaviorMessage(MovementDecreaseDebuff.props(decreaseSpeed)), getSelf());
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

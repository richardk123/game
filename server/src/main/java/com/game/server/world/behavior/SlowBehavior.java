package com.game.server.world.behavior;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.BehaviorBuilder;
import com.game.server.world.behavior.base.Debuff;
import com.game.server.world.behavior.debuff.MovementDecreaseDebuff;
import com.game.server.world.object.base.GameObject;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author dohnal
 */
public class SlowBehavior extends Behavior
{
	/**
	 * Decrease speed in percentage
	 */
	private double decreaseSpeed;

	private Map<GameObject, Debuff> debuffs = Maps.newHashMap();

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
		Debuff debuff = new MovementDecreaseDebuff(object, decreaseSpeed);

		debuffs.put(object, debuff);

		object.tell(new AddBehavior(debuff), getSelf());
	}

	private void onCollisionLeave(GameObject object)
	{
		Debuff debuff = debuffs.get(object);

		if (debuff != null)
		{
			debuffs.remove(object, debuff);

			object.tell(new RemoveBehavior(debuff), getSelf());
		}
	}
}

package com.game.server.world.object;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.internal.KeyInputBehavior;
import com.game.server.world.object.base.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dohnal
 */
public class EventManager extends GameObject
{
	@Override
	protected List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<>();

		behaviors.add(new KeyInputBehavior(this));

		return behaviors;
	}
}

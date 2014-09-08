package com.game.server.world.objects;

import com.game.server.world.behavior.KeyInputBehavior;
import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.geometry.AABB;
import com.game.server.world.map.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dohnal
 */
public class EventManager extends GameObject
{
	@Override
	public AABB getAABB()
	{
		return null;
	}

	@Override
	protected List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<>();

		behaviors.add(new KeyInputBehavior(this));

		return behaviors;
	}
}

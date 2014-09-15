package com.game.server.world.object;

import com.game.server.world.behavior.SlowBehavior;
import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.material.RectangleMaterial;
import com.game.server.world.object.base.GameObject;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.List;

/**
 * @author dohnal
 */
public class Water extends GameObject
{
	public Water(Coordinate position)
	{
		setMaterial(new RectangleMaterial(this, Color.blue, 100, 250));
		setCoordinate(position);
	}

	@Nullable
	@Override
	protected List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = Lists.newArrayList();

		behaviors.add(new SlowBehavior(this, 50));

		return behaviors;
	}
}

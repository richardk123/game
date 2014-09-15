package com.game.server.world.object;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.material.RectangleMaterial;
import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Coordinate;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dohnal
 */
public class Wall extends GameObject
{
	public Wall(Coordinate position)
	{
		setMaterial(new RectangleMaterial(Color.gray, 50, 50, false));
		setCoordinate(position);
	}

	@Override
	protected List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<>();

		//behaviors.add(new ViewBehaviour(this, 70, 70));
		//behaviors.add(new ReplicationTestBehavior(this));

		return behaviors;
	}
}

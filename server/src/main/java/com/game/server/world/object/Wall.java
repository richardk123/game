package com.game.server.world.object;

import com.game.server.world.behavior.ReplicationTestBehavior;
import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.internal.ViewBehaviour;
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
	public Wall(Long id, Coordinate position)
	{
		super(id);

		setMaterial(new RectangleMaterial(this, Color.gray, 50, 50));
		setCoordinate(position);
	}

	@Override
	protected List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<>();

		behaviors.add(new ViewBehaviour(this, 70, 70));
		behaviors.add(new ReplicationTestBehavior(this));

		return behaviors;
	}
}

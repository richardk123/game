package com.game.server.world.object;

import com.game.server.world.behavior.PlayerMoveBehavior;
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
public class Player extends GameObject
{
	public Player(Coordinate position)
	{
		setMaterial(new RectangleMaterial(this, Color.yellow, 10, 10));
		setCoordinate(position);
	}

	@Override
	protected List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<>();

		behaviors.add(new ViewBehaviour(this, 50, 50));
		behaviors.add(new PlayerMoveBehavior(this, 1));

		return behaviors;
	}
}

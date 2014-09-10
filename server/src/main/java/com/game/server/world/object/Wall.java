package com.game.server.world.object;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.game.server.world.behavior.ViewBehaviour;
import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.material.RectangleMaterial;
import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author dohnal
 */
public class Wall extends GameObject
{
	public Wall(Coordinate position)
	{
		setMaterial(new RectangleMaterial(this, Color.gray, 50, 50));
		setCoordinate(position);
	}

	@Override
	protected List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<>();

		behaviors.add(new ViewBehaviour(this, new Envelope(-10, 10, -10, 10)));

		return behaviors;
	}
}

package com.game.server.world.object;

import com.game.server.world.behavior.PlayerMoveBehavior;
import com.game.server.world.behavior.ViewBehaviour;
import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.geometry.Vector2;
import com.game.server.world.material.RectangleMaterial;
import com.game.server.world.object.base.GameObject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dohnal
 */
public class Player extends GameObject
{
	public Player(Vector2 position)
	{
		setPosition(position);
		setMaterial(new RectangleMaterial(this, Color.yellow, 10, 10));
	}

	@Override
	protected List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<>();

		behaviors.add(new PlayerMoveBehavior(this, 1));

		return behaviors;
	}
}

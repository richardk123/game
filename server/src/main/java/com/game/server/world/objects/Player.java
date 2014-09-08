package com.game.server.world.objects;

import com.game.server.world.behavior.PlayerMoveBehavior;
import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.geometry.AABB;
import com.game.server.world.geometry.Vector2;
import com.game.server.world.map.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dohnal
 */
public class Player extends GameObject
{
	private double width;
	private double height;

	public Player(Vector2 position)
	{
		setPosition(position);

		this.width = 10;
		this.height = 10;
	}

	@Override
	public AABB getAABB()
	{
		return new AABB(
				getPosition().getX() - width * 0.5, getPosition().getY() - height * 0.5,
				getPosition().getX() + width * 0.5, getPosition().getY() + height * 0.5);
	}

	public double getWidth()
	{
		return width;
	}

	public double getHeight()
	{
		return height;
	}

	@Override
	protected List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<Behavior>();

		behaviors.add(new PlayerMoveBehavior(this, 0.5));

		return behaviors;
	}
}

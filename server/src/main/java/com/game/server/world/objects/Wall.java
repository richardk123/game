package com.game.server.world.objects;

import java.util.ArrayList;
import java.util.List;

import com.game.server.world.geometry.AABB;
import com.game.server.world.map.CollidableObject;
import com.game.server.world.map.behaviour.MoveBehaviour;
import com.game.server.world.map.behaviour.Behavior;

/**
 * @author dohnal
 */
public class Wall extends CollidableObject
{
	private double width;
	private double height;

	public Wall(double width, double height)
	{
		this.width = width;
		this.height = height;
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
		behaviors.add(new MoveBehaviour());
		return behaviors;
	}
}

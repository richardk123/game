package com.game.server.world.objects;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.geometry.AABB;
import com.game.server.world.geometry.Vector2;
import com.game.server.world.map.GameObject;

import java.util.ArrayList;
import java.util.List;
/**
 * @author dohnal
 */
public class Wall extends GameObject
{
	private double width;
	private double height;

	public Wall(Vector2 position, double width, double height)
	{
		setPosition(position);

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

		return behaviors;
	}
}

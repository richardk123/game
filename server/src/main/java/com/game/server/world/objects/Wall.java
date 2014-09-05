package com.game.server.world.objects;

import com.game.server.world.geometry.AABB;
import com.game.server.world.geometry.Vector2;
import com.game.server.world.map.Collidable;
import com.game.server.world.map.Movable;

import java.util.UUID;

/**
 * @author dohnal
 */
public class Wall implements Collidable, Movable
{
	private Vector2 position;

	private double width;

	private double height;

	private UUID id;

	private AABB aabb;

	public Wall(double x, double y, double width, double height)
	{
		this.id = UUID.randomUUID();

		this.position = new Vector2(x, y);
		this.width = width;
		this.height = height;
	}

	@Override
	public AABB getAABB()
	{
		return new AABB(
				position.getX() - width * 0.5, position.getY() - height * 0.5,
				position.getX() + width * 0.5, position.getY() + height * 0.5);
	}

	@Override
	public UUID getId()
	{
		return id;
	}

	@Override
	public double getX()
	{
		return position.getX();
	}

	@Override
	public double getY()
	{
		return position.getY();
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
	public void move(double x, double y)
	{
		position.add(x, y);
	}

	@Override
	public void move(Vector2 vector)
	{
		position.add(vector);
	}
}

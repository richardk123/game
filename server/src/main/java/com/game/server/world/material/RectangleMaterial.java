package com.game.server.world.material;

import com.game.server.world.material.base.Material;
import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Envelope;

import java.awt.Color;

/**
 * @author dohnal
 */
public class RectangleMaterial implements Material
{

	private Color color;

	private int width;
	private int height;
	private final boolean passable;

	public RectangleMaterial(final int width, final int height, final boolean passable)
	{
		this(Color.white, width, height, passable);
	}

	public RectangleMaterial(final Color color, final int width, final int height, final boolean passable)
	{
		this.color = color;
		this.width = width;
		this.height = height;
		this.passable = passable;
	}

	@Override
	public Envelope getBoundingBox(double x, double y)
	{
		return new Envelope(
				x - width * 0.5, x + width * 0.5,
				y - height * 0.5, y + height * 0.5);
	}

	@Override
	public boolean isPassable(GameObject gameObject)
	{
		return passable;
	}

	public Color getColor()
	{
		return color;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}
}

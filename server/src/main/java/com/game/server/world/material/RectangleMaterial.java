package com.game.server.world.material;

import java.awt.*;

import com.game.server.world.material.base.Material;
import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author dohnal
 */
public class RectangleMaterial implements Material
{
	private final GameObject self;

	private Color color;

	private int width;
	private int height;

	public RectangleMaterial(final GameObject self, final int width, final int height)
	{
		this(self, Color.white, width, height);
	}

	public RectangleMaterial(final GameObject self, final Color color, final int width, final int height)
	{
		this.self = self;
		this.color = color;
		this.width = width;
		this.height = height;
	}

	@Override
	public com.vividsolutions.jts.geom.Envelope getBoundingBox()
	{
		return new Envelope(
				self.getX() - width * 0.5, self.getX() + width * 0.5,
				self.getY() - height * 0.5, self.getY() + height * 0.5);
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

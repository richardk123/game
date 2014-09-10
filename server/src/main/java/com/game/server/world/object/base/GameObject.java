package com.game.server.world.object.base;

/**
 * @author dohnal
 */

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.Message;
import com.game.server.world.material.base.Material;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.apache.log4j.Logger;

/**
 * Represents all game objects.
 */
public abstract class GameObject
{
	static final Logger LOG = Logger.getLogger(GameObject.class);

	private UUID id;

	private Coordinate coordinate;

	private List<Behavior> behaviors;

	private Material material;

	private Envelope boundingBoxMoving;

	public GameObject()
	{
		this.id = UUID.randomUUID();

		behaviors = getBehaviours();
	}

	public UUID getId()
	{
		return id;
	}

	public double getX()
	{
		return coordinate.getOrdinate(Coordinate.X);
	}

	public double getY()
	{
		return coordinate.getOrdinate(Coordinate.Y);
	}

	/**
	 *  get box used for collisions
	 */
	@Nullable
	public Envelope getBoundingBox()
	{
		return material != null ? material.getBoundingBox() : null;
	}

	public Envelope getBoundingBoxMoving()
	{
		return boundingBoxMoving;
	}

	@Nullable
	protected abstract List<Behavior> getBehaviours();

	@Nullable
	@SuppressWarnings("unchecked")
	public <T extends Behavior> T getBehavior(Class<T> behavior)
	{
		if (behaviors != null)
		{
			for (Behavior b : behaviors)
			{
				if (b.getClass().equals(behavior))
				{
					return (T)b;
				}
			}
		}

		return null;
	}

	public void tell(@Nonnull Message message, @Nullable GameObject sender)
	{
		for (Behavior behavior : behaviors)
		{
			if (behavior.isDefinedAt(message))
			{
				behavior.setCurrentSender(sender);

				behavior.apply(message);
			}
		}
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + ":" + getId().toString();
	}

	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}


	public void move(double x, double y)
	{
		coordinate = new Coordinate(getX() + x, getY() + y);
		recalculateBoundingBoxMoving();
	}

	private void recalculateBoundingBoxMoving()
	{
		boundingBoxMoving = new Envelope(getBoundingBox().getMinX() + getX(), getBoundingBox().getMaxX() + getX(),
				getBoundingBox().getMinY() + getY(), getBoundingBox().getMaxY() + getY());
	}

	public Coordinate getCoordinate()
	{
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate)
	{
		this.coordinate = coordinate;
		recalculateBoundingBoxMoving();
	}
}

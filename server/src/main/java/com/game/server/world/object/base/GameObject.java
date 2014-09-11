package com.game.server.world.object.base;

/**
 * @author dohnal
 */

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.Message;
import com.game.server.world.behavior.internal.InternalMessage;
import com.game.server.world.behavior.internal.ViewBehaviour;
import com.game.server.world.map.GameService;
import com.game.server.world.material.base.Material;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents all game objects.
 */
public abstract class GameObject
{
	static final Logger LOG = Logger.getLogger(GameObject.class);

	private Long id;

	private Coordinate coordinate;

	private List<Behavior> behaviors;

	private Material material;

	public GameObject(Long id)
	{
		this.id = id;
	}

	public Long getId()
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

	@Nullable
	public Envelope getCollisionBox()
	{
		return material != null ? material.getBoundingBox() : null;
	}

	@Nullable
	public Envelope getViewBox()
	{
		ViewBehaviour viewBehaviour = getBehavior(ViewBehaviour.class);

		if (viewBehaviour != null)
		{
			return viewBehaviour.getViewBox();
		}

		return null;
	}

	@Nullable
	protected abstract List<Behavior> getBehaviours();

	private List<Behavior> getBehaviorsLazy()
	{
		if (behaviors == null)
		{
			behaviors = getBehaviours();
		}

		return behaviors;
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public <T extends Behavior> T getBehavior(Class<T> behavior)
	{
		if (getBehaviorsLazy() != null)
		{
			for (Behavior b : getBehaviorsLazy())
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
		// handle incoming message
		getBehaviorsLazy().stream().filter(behavior -> behavior.isDefinedAt(message)).forEach(behavior -> {
			behavior.setCurrentSender(sender);
			behavior.apply(message);
		});

		// replicate incoming message to viewers (skip replicating internal messages)
		if (!InternalMessage.class.isAssignableFrom(message.getClass()))
		{
			GameService.get().getWorldMap().findViewedObjects(this).forEach(object -> {
				if (!object.getId().equals(getId()))
				{
					object.tell(new ViewBehaviour.ViewMessage(message), this);
				}
			});
		}
	}

	public void onCreate()
	{
		for (Behavior behavior : getBehaviorsLazy())
		{
			behavior.onCreate();
		}
	}

	public void onDestroy()
	{
		for (Behavior behavior : getBehaviorsLazy())
		{
			behavior.onDestroy();
		}
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "[" + getId().toString() + "]";
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
	}

	public Coordinate getCoordinate()
	{
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate)
	{
		this.coordinate = coordinate;
	}
}

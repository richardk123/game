package com.game.server.world.object.base;

/**
 * @author dohnal
 */

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.BehaviorBuilder;
import com.game.server.world.behavior.base.Message;
import com.game.server.world.behavior.internal.InternalMessage;
import com.game.server.world.behavior.internal.ViewBehaviour;
import com.game.server.world.material.base.Material;
import com.game.server.world.service.GameService;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.apache.log4j.Logger;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents all game objects.
 */
public abstract class GameObject
{
	static final Logger LOG = Logger.getLogger(GameObject.class);

	private Long id;

	private Coordinate coordinate;

	private PartialFunction<Object, BoxedUnit> defaultMessageHandler;

	private List<Behavior> behaviors;

	private List<Behavior> added;

	private List<Behavior> removed;

	private Material material;

	public GameObject()
	{
		this.id = GameService.get().getIdGenerator().generateId(getClass());

		this.added = Lists.newArrayList();
		this.removed = Lists.newArrayList();

		defaultMessageHandler = BehaviorBuilder
				.match(Behavior.AddBehaviorMessage.class, m -> addBehavior(m.getBehavior()))
				.match(Behavior.RemoveBehaviorMessage.class, m -> removeBehavior(m.getBehavior()))
				.build();
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
		return material != null ? material.getBoundingBox(getX(), getY()) : null;
	}

	public Envelope getCollisionBox(double newX, double newY)
	{
		return material != null ? material.getBoundingBox(newX, newY) : null;
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

	/**
	 * List of behaviors must be synchronized
	 */
	private List<Behavior> getBehaviorsLazy()
	{
		if (behaviors == null)
		{
			List<Behavior> definedBehaviors = getBehaviours();

			if (definedBehaviors == null)
			{
				behaviors = new CopyOnWriteArrayList<>(new ArrayList<>());
			}
			else
			{
				behaviors = new CopyOnWriteArrayList<>(definedBehaviors);
			}
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
				if (behavior.isAssignableFrom(b.getClass()))
				{
					return (T)b;
				}
			}
		}

		return null;
	}

	public boolean hasBehavior(Class behavior)
	{
		return getBehavior(behavior) != null;
	}

	public void tell(@Nonnull Message message, @Nullable GameObject sender)
	{
		defaultMessageHandler(message);

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

	private void defaultMessageHandler(@Nonnull Message message)
	{
		if (defaultMessageHandler.isDefinedAt(message))
		{
			defaultMessageHandler.apply(message);
		}
	}

	private void addBehavior(Behavior behavior)
	{
		getBehaviorsLazy().add(behavior);
		behavior.onCreate();
	}

	private void removeBehavior(Class<? extends Behavior> behavior)
	{
		getBehaviorsLazy().stream()
				.filter(b -> b.getClass().equals(behavior))
				.forEach(b -> {
					b.onDestroy();
					getBehaviorsLazy().remove(b);
				});
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

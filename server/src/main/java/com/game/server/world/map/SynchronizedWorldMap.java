package com.game.server.world.map;

import com.game.server.world.object.base.GameObject;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Richard Kolísek
 */
public class SynchronizedWorldMap implements WorldMap<GameObject>
{
	/**
	 * All game objects
	 */
	private List<GameObject> objects = new ArrayList<>();

	/**
	 * Quad tree used to collision detection and spatial queries
	 */
	private Quadtree collisionTree = new Quadtree();

	/**
	 * Quad tree used to view queries
	 */
	private Quadtree viewTree = new Quadtree();

	@Override
	public void add(GameObject object)
	{
		add(object, true);
	}

	@Override
	public void remove(GameObject object)
	{
		remove(object, true);
	}

	@Override
	public void update(GameObject object)
	{
		remove(object, false);
		add(object, false);
	}

	private void add(GameObject object, boolean isCreated)
	{
		objects.add(object);

		if (object.getCollisionBox() != null)
		{
			collisionTree.insert(object.getCollisionBox(), object);
		}

		if (object.getViewBox() != null)
		{
			viewTree.insert(object.getViewBox(), object);
		}

		if (isCreated)
		{
			object.onCreate();
		}
	}

	private void remove(GameObject object, boolean isRemoved)
	{
		objects.remove(object);

		if (object.getCollisionBox() != null)
		{
			collisionTree.remove(object.getCollisionBox(), object);
		}

		if (object.getViewBox() != null)
		{
			viewTree.remove(object.getViewBox(), object);
		}

		if (isRemoved)
		{
			object.onDestroy();
		}
	}

	@Override
	public void clear()
	{
		objects = new ArrayList<>();
		collisionTree = new Quadtree();
		viewTree = new Quadtree();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GameObject> findObjects(Envelope envelope)
	{
		return FluentIterable
				.from(collisionTree.query(envelope))
				.filter(new Predicate<GameObject>()
				{
					@Override
					public boolean apply(@Nullable GameObject object)
					{
						if (object != null && object.getCollisionBox() != null)
						{
							if (envelope.intersects(object.getCollisionBox()))
							{
								return true;
							}
						}

						return false;
					}
				}).toImmutableList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GameObject> findViewedObjects(GameObject object)
	{
		final Envelope envelope = (object.getCollisionBox() != null) ? object.getCollisionBox() :
				new Envelope(object.getCoordinate());

		if (envelope != null)
		{
			return FluentIterable
					.from(viewTree.query(envelope))
					.filter(new Predicate<GameObject>()
					{
						@Override
						public boolean apply(@Nullable GameObject object)
						{
							if (object != null && object.getViewBox() != null)
							{
								if (envelope.intersects(object.getViewBox()))
								{
									return true;
								}
							}

							return false;
						}
					}).toImmutableList();
		}

		return new ArrayList<>();
	}

	@Override
	public synchronized List<GameObject> getObjects()
	{
		return ImmutableList.copyOf(objects);
	}
}

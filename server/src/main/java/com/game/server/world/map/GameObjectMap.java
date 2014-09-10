package com.game.server.world.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;

/**
 * @author Richard Kolísek
 */
public class GameObjectMap<T> implements WorldMap<T>
{
	private Quadtree quadtree = new Quadtree();
	Map<T, Envelope> objectEnvelopeMap = new HashMap<>();
	private final MapAdapter<T> adapter;

	public GameObjectMap(MapAdapter<T> adapter)
	{
		this.adapter = adapter;
	}

	@Override
	public void add(T object)
	{
		Envelope envelope = adapter.getEnvelope(object);

		quadtree.insert(envelope, object);
		objectEnvelopeMap.put(object, envelope);
	}

	@Override
	public void remove(T object)
	{
		Envelope envelope = adapter.getEnvelope(object);

		quadtree.remove(envelope, object);
		objectEnvelopeMap.remove(object);
	}

	@Override
	public void update(T object)
	{
		this.remove(object);
		this.add(object);
	}

	@Override
	public void clear()
	{
		quadtree = new Quadtree();
	}

	@Override
	public List<T> find(Envelope envelope)
	{
		// quad tree possibly return also object outside of envelope
		List<T> nonFiltered = quadtree.query(envelope);
		List<T> result = new ArrayList<>();

		for (T object : nonFiltered)
		{
			Envelope e = adapter.getEnvelope(object);

			if (envelope.intersects(e))
			{
				result.add(object);
			}
		}

		return result;
	}

	@Override
	public MapAdapter<T> getAdapter()
	{
		return adapter;
	}

	@Override
	public Iterator<T> iterator()
	{
		return quadtree.queryAll().iterator();
	}
}

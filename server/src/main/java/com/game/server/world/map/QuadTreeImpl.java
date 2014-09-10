package com.game.server.world.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;

/**
 * @author Richard Kolísek
 */
public class QuadTreeImpl<T extends GameObject> implements WorldMap<T>
{
	private Quadtree quadtree = new Quadtree();
	Map<T, Envelope> objectEnvelopeMap = new HashMap<>();


	@Override
	public void add(T object)
	{
		quadtree.insert(object.getEnvelope(), object);
		objectEnvelopeMap.put(object, object.getEnvelope());
	}

	@Override
	public void remove(T object)
	{
		quadtree.remove(object.getEnvelope(), object);
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
			if (envelope.intersects(object.getEnvelope()))
			{
				result.add(object);
			}
		}

		return result;
	}

	@Override
	public Iterator<T> iterator()
	{
		return quadtree.queryAll().iterator();
	}
}

package com.game.server.world.map;

import com.vividsolutions.jts.geom.Envelope;

import java.util.List;

/**
 * @author dohnal
 */
public interface WorldMap<T>
{
	/**
	 * Adds a new object to the map.
	 */
	public void add(T object);

	/**
	 * Removes the given object from the map.
	 */
	public void remove(T object);

	/**
	 * Updates the given object.
	 * Used when the object has moved or rotated.
	 */
	public void update(T object);

	/**
	 * Clears all objects from the map.
	 */
	public void clear();

	/**
	 * Finds all objects which lies or overlaps given envelope
	 */
	public List<T> findObjects(Envelope envelope);

	/**
	 * Finds all objects which can view given object
	 */
	public List<T> findViewedObjects(T object);

	/**
	 * Get all objects in world
	 */
	public List<T> getObjects();
}

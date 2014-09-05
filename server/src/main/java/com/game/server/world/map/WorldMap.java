package com.game.server.world.map;

import com.game.server.world.geometry.AABB;

import java.util.Iterator;
import java.util.List;

/**
 * @author dohnal
 */
public interface WorldMap<T extends CollidableObject> extends Iterable<T>
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
	 * Find all objects which lies or overlaps given AABB
	 */
	public List<T> find(AABB aabb);

	/**
	 * Only for debugging
	 */
	public Iterator<AABB> getAABBIterator();
}

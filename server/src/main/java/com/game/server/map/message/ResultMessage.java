package com.game.server.map.message;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Richard Kolísek
 */
public class ResultMessage
{

	private Set<Integer> ids = new HashSet<>();

	public Set<Integer> getIds()
	{
		return ids;
	}

	public void setIds(Set<Integer> ids)
	{
		this.ids = ids;
	}

}

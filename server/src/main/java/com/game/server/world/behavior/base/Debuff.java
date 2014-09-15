package com.game.server.world.behavior.base;

import com.game.server.world.object.base.GameObject;

/**
 * @author dohnal
 */
public abstract class Debuff extends Behavior
{
	public Debuff(GameObject self)
	{
		super(self);
	}

	protected abstract void onAttach();
	protected abstract void onDetach();

	@Override
	public void onCreate()
	{
		super.onCreate();

		onAttach();
	}

	@Override
	public void onDestroy()
	{
		onDetach();

		super.onDestroy();
	}
}

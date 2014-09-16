package com.game.server.world.behavior.base;

/**
 * @author dohnal
 */
public abstract class Buff extends Behavior
{
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

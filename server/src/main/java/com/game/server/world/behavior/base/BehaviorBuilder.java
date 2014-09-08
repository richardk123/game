package com.game.server.world.behavior.base;

import akka.japi.pf.FI;
import akka.japi.pf.UnitMatch;
import akka.japi.pf.UnitPFBuilder;

/**
 * @author dohnal
 */
public class BehaviorBuilder
{
	private BehaviorBuilder()
	{ }

	/**
	 * Return a new {@link akka.japi.pf.UnitPFBuilder} with a case statement added.
	 *
	 * @param type   a type to match the argument against
	 * @param apply  an action to apply to the argument if the type matches
	 * @return       a builder with the case statement added
	 */
	public static <P> UnitPFBuilder<Object> match(final Class<P> type, FI.UnitApply<P> apply)
	{
		return UnitMatch.match(type, apply);
	}

	/**
	 * Return a new {@link UnitPFBuilder} with a case statement added.
	 *
	 * @param type       a type to match the argument against
	 * @param predicate  a predicate that will be evaluated on the argument if the type matches
	 * @param apply      an action to apply to the argument if the type matches and the predicate returns true
	 * @return           a builder with the case statement added
	 */
	public static <P> UnitPFBuilder<Object> match(final Class<P> type,
												  FI.TypedPredicate<P> predicate,
												  FI.UnitApply<P> apply)
	{
		return UnitMatch.match(type, predicate, apply);
	}

	/**
	 * Return a new {@link UnitPFBuilder} with a case statement added.
	 *
	 * @param apply      an action to apply to the argument
	 * @return           a builder with the case statement added
	 */
	public static UnitPFBuilder<Object> matchAny(FI.UnitApply<Object> apply)
	{
		return UnitMatch.matchAny(apply);
	}
}

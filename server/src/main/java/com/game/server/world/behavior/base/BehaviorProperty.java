package com.game.server.world.behavior.base;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author dohnal
 */
public class BehaviorProperty<T>
{
	private final T defaultValue;

	private List<PropertyModifier<T>> modifiers = Lists.newArrayList();

	public BehaviorProperty(final @Nonnull T defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	public T getValue()
	{
		T value = defaultValue;

		for (PropertyModifier<T> modifier : modifiers)
		{
			value = modifier.modify(value);
		}

		return value;
	}

	public void addModifier(final @Nonnull PropertyModifier<T> modifier)
	{
		modifiers.add(modifier);
	}

	public void removeModifier(final @Nonnull PropertyModifier<T> modifier)
	{
		modifiers.remove(modifier);
	}

	public interface PropertyModifier<T>
	{
		public T modify(T input);
	}
}

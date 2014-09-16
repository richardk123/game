package com.game.server.world.behavior.internal;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.BehaviorProps;
import com.game.server.world.object.base.GameObject;
import org.apache.log4j.Logger;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dohnal
 */
public final class KeyInputBehavior extends Behavior implements KeyListener
{
	private static final Logger LOG = Logger.getLogger(KeyInputBehavior.class);

	private final Set<Integer> pressedKeys = new HashSet<>();

	public static BehaviorProps<KeyInputBehavior> props()
	{
		return BehaviorProps.create(KeyInputBehavior::new);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{ }

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (!pressedKeys.contains(e.getKeyCode()))
		{
			pressedKeys.add(e.getKeyCode());

			for (GameObject object : getWorld().getObjects())
			{
				object.tell(new KeyPressedMessage(e.getKeyCode()), getSelf());
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		for (GameObject object : getWorld().getObjects())
		{
			object.tell(new KeyReleasedMessage(e.getKeyCode()), getSelf());
		}

		pressedKeys.remove(e.getKeyCode());
	}

	public static class KeyMessage extends InternalMessage
	{
		protected final int keyCode;

		public KeyMessage(int keyCode)
		{
			this.keyCode = keyCode;
		}

		public int getKeyCode()
		{
			return keyCode;
		}
	}

	public static class KeyPressedMessage extends KeyMessage
	{
		public KeyPressedMessage(int keyCode)
		{
			super(keyCode);
		}
	}

	public static class KeyReleasedMessage extends KeyMessage
	{
		public KeyReleasedMessage(int keyCode)
		{
			super(keyCode);
		}
	}
}

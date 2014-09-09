package com.game.server.world.behavior;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.Message;
import com.game.server.world.object.base.GameObject;
import org.apache.log4j.Logger;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dohnal
 */
public class KeyInputBehavior extends Behavior implements KeyListener
{
	private static final Logger LOG = Logger.getLogger(GameObject.class);

	private final Set<Integer> pressedKeys = new HashSet<>();

	public KeyInputBehavior(GameObject self)
	{
		super(self);
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

			for (GameObject object : getWorld())
			{
				object.tell(new KeyPressedMessage(e.getKeyCode()), getSelf());
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		for (GameObject object : getWorld())
		{
			object.tell(new KeyReleasedMessage(e.getKeyCode()), getSelf());
		}

		pressedKeys.remove(e.getKeyCode());
	}

	protected class KeyMessage extends Message
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

	protected class KeyPressedMessage extends KeyMessage
	{
		public KeyPressedMessage(int keyCode)
		{
			super(keyCode);
		}
	}

	protected class KeyReleasedMessage extends KeyMessage
	{
		public KeyReleasedMessage(int keyCode)
		{
			super(keyCode);
		}
	}
}

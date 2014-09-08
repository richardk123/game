package com.game.server.world.behavior;

import com.game.server.world.behavior.base.BehaviorBuilder;
import com.game.server.world.map.GameObject;
import org.apache.log4j.Logger;

import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author dohnal
 */
public class PlayerMoveBehavior extends MoveBehaviour
{
	private static final Logger LOG = Logger.getLogger(PlayerMoveBehavior.class);

	private double speed = 1;

	private int KEY_UP = KeyEvent.VK_W;
	private int KEY_DOWN = KeyEvent.VK_S;
	private int KEY_LEFT = KeyEvent.VK_A;
	private int KEY_RIGHT = KeyEvent.VK_D;

	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;

	private boolean isMoving = false;

	private Timer movingTimer;

	public PlayerMoveBehavior(GameObject self, double speed)
	{
		super(self);

		this.speed = speed;

		behaviour(BehaviorBuilder.match(KeyInputBehavior.KeyPressedMessage.class, this::handleKeyPressed)
								 .match(KeyInputBehavior.KeyReleasedMessage.class, this::handleKeyReleased)
								 .match(MoveMessage.class, this::moveHandler)
								 .build());
	}

	protected void handleKeyPressed(final KeyInputBehavior.KeyPressedMessage message)
	{
		if (message.getKeyCode() == KEY_UP)
		{
			upPressed = true;
			downPressed = false;

			startMoving();
		}
		else if (message.getKeyCode() == KEY_DOWN)
		{
			downPressed = true;
			upPressed = false;

			startMoving();
		}
		else if (message.getKeyCode() == KEY_LEFT)
		{
			leftPressed = true;
			rightPressed = false;

			startMoving();
		}
		else if (message.getKeyCode() == KEY_RIGHT)
		{
			rightPressed = true;
			leftPressed = false;

			startMoving();
		}
	}

	protected void handleKeyReleased(final KeyInputBehavior.KeyReleasedMessage message)
	{
		if (message.getKeyCode() == KEY_UP)
		{
			upPressed = false;

			stopMoving();
		}
		else if (message.getKeyCode() == KEY_DOWN)
		{
			downPressed = false;

			stopMoving();
		}
		else if (message.getKeyCode() == KEY_LEFT)
		{
			leftPressed = false;

			stopMoving();
		}
		else if (message.getKeyCode() == KEY_RIGHT)
		{
			rightPressed = false;

			stopMoving();
		}
	}

	private void startMoving()
	{
		if (!isMoving)
		{
			isMoving = true;

			movingTimer = new Timer();

		    movingTimer.scheduleAtFixedRate(new TimerTask()
			{
				@Override
				public void run()
				{
					getSelf().tell(new MoveMessage(
							rightPressed ? speed : (leftPressed ? -speed : 0),
							upPressed ? speed : (downPressed ? -speed : 0)), getSelf());
				}
			}, 0, 10);
		}
	}

	private void stopMoving()
	{
		if (isMoving && !upPressed && ! downPressed && !leftPressed && !rightPressed)
		{
			isMoving = false;

			movingTimer.cancel();
		}
	}
}

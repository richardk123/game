package com.game.server.world.behavior;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.behavior.base.BehaviorBuilder;
import com.game.server.world.behavior.internal.ViewBehaviour;
import com.game.server.world.object.base.GameObject;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;

/**
 * @author dohnal
 */
public class ReplicationTestBehavior extends Behavior
{
	private static final Logger LOG = Logger.getLogger(ReplicationTestBehavior.class);

	public ReplicationTestBehavior(GameObject self)
	{
		super(self);

		behaviour(BehaviorBuilder.match(ViewBehaviour.ViewMessage.class, this::logMessage).build());
	}

	private void logMessage(final @Nonnull ViewBehaviour.ViewMessage message)
	{
		LOG.info(getSelf() + " - received " + message.getMessage().getClass().getSimpleName() + " from " + getSender());
	}
}

package com.game.server.world.behavior.debuff;

import com.game.server.world.behavior.MoveBehaviour;
import com.game.server.world.behavior.base.BehaviorProperty;
import com.game.server.world.behavior.base.Debuff;
import com.game.server.world.object.base.GameObject;
import org.apache.log4j.Logger;

/**
 * @author dohnal
 */
public class MovementDecreaseDebuff extends Debuff
{
	private static final Logger LOG = Logger.getLogger(MovementDecreaseDebuff.class);

	/**
	 * Speed decrease in percentage
	 */
	private double decreaseSpeed;
	
	private BehaviorProperty.PropertyModifier<Double> propertyModifier;

	public MovementDecreaseDebuff(GameObject self, double decreaseSpeed)
	{
		super(self);

		this.decreaseSpeed = decreaseSpeed;
		
		propertyModifier = input -> input * (decreaseSpeed / 100.0);
	}

	@Override
	protected void onAttach()
	{
		LOG.info("Movement speed decreased by " + decreaseSpeed + "%");

		MoveBehaviour moveBehaviour = getSelf().getBehavior(MoveBehaviour.class);
		
		if (moveBehaviour != null)
		{
			moveBehaviour.getMovementSpeed().addModifier(propertyModifier);
		}
	}

	@Override
	protected void onDetach()
	{
		LOG.info("Movement speed increased by " + decreaseSpeed + "%");

		MoveBehaviour moveBehaviour = getSelf().getBehavior(MoveBehaviour.class);    

		if (moveBehaviour != null)
		{
			moveBehaviour.getMovementSpeed().removeModifier(propertyModifier);
		}
	}
}

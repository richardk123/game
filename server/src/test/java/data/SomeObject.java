package data;

import com.game.server.world.geometry.AABB;
import com.game.server.world.map.CollidableObject;
import com.game.server.world.map.GameObject;
import com.game.server.world.map.behaviour.CollideBehaviour;
import com.game.server.world.map.behaviour.CreateBehaviour;
import com.game.server.world.map.behaviour.Message;
import com.game.server.world.map.behaviour.MoveBehaviour;
import com.game.server.world.map.behaviour.Behavior;
import com.game.server.world.map.behaviour.RemoveBehaviour;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dohnal
 */
public class SomeObject extends CollidableObject
{
	private double width;
	private double height;


	public SomeObject(double width, double height)
	{
		this.width = width;
		this.height = height;
	}

	@Override
	public AABB getAABB()
	{
		return new AABB(
				getPosition().getX() - width * 0.5, getPosition().getY() - height * 0.5,
				getPosition().getX() + width * 0.5, getPosition().getY() + height * 0.5);
	}

	@Override
	public List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<>();
		behaviors.add(new MoveBehaviour());
		behaviors.add(new RemoveBehaviour());
		behaviors.add(new CreateBehaviour());
		behaviors.add(new CollideBehaviour());
		return behaviors;
	}

	public double getWidth()
	{
		return width;
	}

	public double getHeight()
	{
		return height;
	}
}

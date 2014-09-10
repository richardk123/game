package data;

import java.util.ArrayList;
import java.util.List;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author dohnal
 */
public class SomeObject extends GameObject
{
	private double width;
	private double height;


	public SomeObject(Coordinate position, double width, double height)
	{
		setCoordinate(position);

		this.width = width;
		this.height = height;
	}

	@Override
	public Envelope getBoundingBox()
	{
		return new Envelope(getX() - width * 0.5, getX() + width * 0.5,
				getY() - height * 0.5, getY() + height * 0.5);
	}

	@Override
	public List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<>();

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

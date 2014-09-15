package data;

import com.game.server.world.behavior.base.Behavior;
import com.game.server.world.material.RectangleMaterial;
import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dohnal
 */
public class SomeObject extends GameObject
{
	public SomeObject(Coordinate position, int width, int height)
	{
		setCoordinate(position);
		setMaterial(new RectangleMaterial(width, height, true));
	}

	@Override
	public List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<>();

		return behaviors;
	}
}

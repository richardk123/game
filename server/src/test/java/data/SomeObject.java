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
	public SomeObject(Long id, Coordinate position, int width, int height)
	{
		super(id);

		setCoordinate(position);
		setMaterial(new RectangleMaterial(this, width, height));
	}

	@Override
	public List<Behavior> getBehaviours()
	{
		List<Behavior> behaviors = new ArrayList<>();

		return behaviors;
	}
}

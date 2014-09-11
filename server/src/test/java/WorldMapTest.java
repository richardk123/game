import com.game.server.world.map.GameService;
import com.game.server.world.map.SynchronizedWorldMap;
import com.game.server.world.map.WorldMap;
import com.game.server.world.object.base.GameObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import data.SomeObject;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


/**
 * @author dohnal
 */
public class WorldMapTest
{
	@Test
	public void createMapTest()
	{
		WorldMap<GameObject> map = new SynchronizedWorldMap();

		assertNotNull(map);
	}


	@Test
	public void addObjectTest()
	{
		WorldMap<GameObject> map = GameService.getNew().getWorldMap();

		SomeObject object = new SomeObject(1L, new Coordinate(0, 0), 5, 10);
		map.add(object);

		List<GameObject> result = map.findObjects(createEnvelope(new Coordinate(10, 0), 6));

		assertNotNull(result);
		assertEquals(0, result.size());

		result = map.findObjects(createEnvelope(new Coordinate(10, 0), 8));

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(object.getId(), result.get(0).getId());
	}

	@Test
	public void removeObjectTest()
	{
		WorldMap<GameObject> map = GameService.getNew().getWorldMap();

		SomeObject object = new SomeObject(1L, new Coordinate(0, 0), 5, 10);
		map.add(object);
		map.remove(object);

		List<GameObject> result = map.findObjects(createEnvelope(new Coordinate(10, 0), 8));

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	public void updateObjectTest()
	{
		WorldMap<GameObject> map = GameService.getNew().getWorldMap();

		SomeObject object = new SomeObject(1L, new Coordinate(0, 0), 5, 10);
		map.add(object);

		List<GameObject> result = map.findObjects(createEnvelope(new Coordinate(10, 0), 6));

		assertNotNull(result);
		assertEquals(0, result.size());

		object.move(2, 0);
		map.update(object);
		result = map.findObjects(createEnvelope(new Coordinate(10, 0), 6));

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(object.getId(), result.get(0).getId());
	}

	@Test
	public void clearMapTest()
	{
		WorldMap<GameObject> map = GameService.getNew().getWorldMap();

		SomeObject object = new SomeObject(1L, new Coordinate(0, 0), 5, 10);
		map.add(object);

		List<GameObject> result = map.findObjects(createEnvelope(new Coordinate(10, 0), 8));

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(object.getId(), result.get(0).getId());

		map.clear();

		result = map.findObjects(createEnvelope(new Coordinate(10, 0), 8));

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	public Envelope createEnvelope(Coordinate center, double radius)
	{

		if (radius < 0) throw new IllegalArgumentException("Invalid radius");

		if (center == null)
		{
			return new Envelope(-radius, -radius,  radius,  radius);
		}
		else
		{
			return new Envelope(center.getOrdinate(Coordinate.X) - radius, center.getOrdinate(Coordinate.X) + radius,  
					center.getOrdinate(Coordinate.Y) - radius, center.getOrdinate(Coordinate.Y) + radius);
		}
		
	}
}

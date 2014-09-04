import com.game.server.world.geometry.AABB;
import com.game.server.world.geometry.Vector2;
import com.game.server.world.map.DynamicAABBTree;
import com.game.server.world.map.WorldMap;
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
		WorldMap<SomeObject> map = new DynamicAABBTree<>();

		assertNotNull(map);
	}

	@Test
	public void addObjectTest()
	{
		WorldMap<SomeObject> map = new DynamicAABBTree<>();

		SomeObject object = new SomeObject(0, 0, 5, 10);

		map.add(object);

		List<SomeObject> result = map.find(new AABB(new Vector2(10, 0), 6));

		assertNotNull(result);
		assertEquals(0, result.size());

		result = map.find(new AABB(new Vector2(10, 0), 8));

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(object.getId(), result.get(0).getId());
	}

	@Test
	public void removeObjectTest()
	{
		WorldMap<SomeObject> map = new DynamicAABBTree<>();

		SomeObject object = new SomeObject(0, 0, 5, 10);

		map.add(object);
		map.remove(object);

		List<SomeObject> result = map.find(new AABB(new Vector2(10, 0), 8));

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	public void updateObjectTest()
	{
		WorldMap<SomeObject> map = new DynamicAABBTree<>();

		SomeObject object = new SomeObject(0, 0, 5, 10);

		map.add(object);

		List<SomeObject> result = map.find(new AABB(new Vector2(10, 0), 6));

		assertNotNull(result);
		assertEquals(0, result.size());

		object.move(2, 0);
		map.update(object);

		result = map.find(new AABB(new Vector2(10, 0), 6));

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(object.getId(), result.get(0).getId());
	}

	@Test
	public void clearMapTest()
	{
		WorldMap<SomeObject> map = new DynamicAABBTree<>();

		SomeObject object = new SomeObject(0, 0, 5, 10);

		map.add(object);

		List<SomeObject> result = map.find(new AABB(new Vector2(10, 0), 8));

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(object.getId(), result.get(0).getId());

		map.clear();

		result = map.find(new AABB(new Vector2(10, 0), 8));

		assertNotNull(result);
		assertEquals(0, result.size());
	}
}

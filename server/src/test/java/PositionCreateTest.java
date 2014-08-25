import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.game.server.map.SectorActor;
import com.game.server.map.data.Point;
import com.game.server.map.data.SectorData;
import com.game.server.map.data.Square;
import com.game.server.map.message.PositionCreateMessage;
import com.game.server.map.message.PositionMoveMessage;
import org.junit.Test;

/**
 * @author Richard Kolísek
 */
public class PositionCreateTest
{

	public static final int COUNT = 100;
	public static final int SECTOR_SIZE_COEF = 1;

	@Test
	public void test()
	{
		ActorSystem system = ActorSystem.create("Test");
		SectorData sd = new SectorData(0, new Square(new Point(0, 0), COUNT * SECTOR_SIZE_COEF));
		ActorRef sector = system.actorOf(SectorActor.props(sd), SectorActor.ROOT_SECTOR_NAME);

		int count = 0;


		for (int x = 0; x < COUNT; x++)
		{
			for (int y = 0; y < COUNT; y++)
			{
				count++;
				sector.tell(new PositionCreateMessage(count, new Point(x * SECTOR_SIZE_COEF, y * SECTOR_SIZE_COEF)), null);
			}
		}

		count = 0;

		for (int x = 0; x < COUNT; x++)
		{
			for (int y = 0; y < COUNT; y++)
			{
				count++;
				sector.tell(new PositionMoveMessage(count, new Point(x, y),
						new Point(getRandom(x), getRandom(y))), null);
			}
		}

	}

	public static float getRandom(int position)
	{
		float value = 50 + (float) (Math.random() * 30);

		if (value > COUNT)
		{
			return COUNT;
		}

		return value;
	}
}

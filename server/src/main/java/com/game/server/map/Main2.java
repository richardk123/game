package com.game.server.map;

import java.util.Set;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.game.server.map.data.Point;
import com.game.server.map.data.SectorData;
import com.game.server.map.data.Square;
import com.game.server.map.message.PositionCreateMessage;
import com.game.server.map.message.PositionMoveMessage;
import com.game.server.map.message.QueryMessage;
import org.apache.log4j.Logger;

/**
 * @author Richard Kolísek
 */
public class Main2
{

	static final Logger LOG = Logger.getLogger(Main2.class);

	public static final int COUNT = 101;
	public static final int SECTOR_SIZE_COEF = 1;

	public static void main(String[] args)
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
				sector.tell(new PositionCreateMessage(count, new Point(x * SECTOR_SIZE_COEF, y * SECTOR_SIZE_COEF), false), null);
			}
		}


		try
		{
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
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

		try
		{
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		sector.tell(new QueryMessage(
						new Square(new Point(25, 25), 50),
						new QueryMessage.ResultCallback()
						{
							@Override
							public void resultCallback(Set<Integer> ids)
							{
								LOG.info(ids.size());
							}
						}
				),
				null
		);

	}

	public static float getRandom(int position)
	{
		float value = 25 + (float) (Math.random() * 30);

		if (value > COUNT)
		{
			return COUNT;
		}

		return value;
	}

}

package com.game.server.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.japi.pf.ReceiveBuilder;
import com.game.server.map.data.Point;
import com.game.server.map.data.SectorData;
import com.game.server.map.data.Square;
import com.game.server.map.message.PositionCreateMessage;
import com.game.server.map.message.PositionCreatedMessage;
import com.game.server.map.message.PositionDestroyMessage;
import com.game.server.map.message.PositionMoveMessage;
import com.game.server.map.message.QueryMessage;
import com.game.server.map.message.ResultMessage;
import com.game.server.map.message.SectorDestroyMessage;
import akka.pattern.Patterns;
import org.apache.log4j.Logger;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import static akka.pattern.Patterns.ask;
import static akka.dispatch.Futures.sequence;

/**
 * @author Richard Kolísek
 */
public class SectorActor extends AbstractActor
{
	static final Logger LOG = Logger.getLogger(SectorActor.class);

	public static final int MAX_OBJECT_COUNT = 2500;
	public static final int SUB_SECTOR_COUNT = 2;   // must %2 == 0
	public static final String ROOT_SECTOR_NAME = "rootSector";

	private final SectorData sectorData;
//	private final List<ActorRef> subSectorActors;
	private final Map<Square, ActorRef> squareActorRefMap;

	private SectorActor(SectorData sectorData)
	{

		this.sectorData = sectorData;
		this.squareActorRefMap = new HashMap<>();

		receive(ReceiveBuilder.
				 match(SectorDestroyMessage.class, this::sectorDestroy)
				.match(PositionCreateMessage.class, this::positionCreate)
				.match(PositionMoveMessage.class, this::positionMove)
				.match(PositionDestroyMessage.class, this::positionDestroy)
				.match(QueryMessage.class, this::sectorFind)
				.build());

	}

	public static Props props(SectorData sectorData) {
		return Props.create(SectorActor.class, () -> new SectorActor(sectorData));
	}

	public void sectorFind(final QueryMessage m)
	{

		final ResultMessage resultMessage = new ResultMessage();;

		// do not have child sectors
		if (squareActorRefMap.size() == 0)
		{
			Map<Integer, Point> idPosMap = sectorData.getIdPosMap();

			for (Integer id : idPosMap.keySet())
			{
				Point point = idPosMap.get(id);
				boolean inSquare = m.getFindPointsInSquare().isInSquare(point);

				if (inSquare)
				{
					resultMessage.getIds().add(id);
				}

			}

			// send it back to sender
			if (m.getResultCallback() == null)
			{
				sender().tell(resultMessage, self());
			}
			else
			{
				m.getResultCallback().resultCallback(resultMessage.getIds());
			}

		}
		else
		{

			List<Future<Object>> futures = new ArrayList<>();

			for (Square square : squareActorRefMap.keySet())
			{
				if (square.isInSquare(m.getFindPointsInSquare()))
				{
					futures.add(Patterns.ask(squareActorRefMap.get(
							square), new QueryMessage(m.getFindPointsInSquare(), null), 10000));
				}
			}

			ExecutionContext ec = context().dispatcher();

			Future<Iterable<Object>> futureResults = sequence(futures, ec);

			futureResults.onSuccess(
					new OnSuccess<Iterable<Object>>()
					{
						@Override
						public void onSuccess(Iterable<Object> objects) throws Throwable
						{
							Set<Integer> result = new HashSet<>();

							objects.forEach(new Consumer<Object>()
							{
								@Override
								public void accept(Object o)
								{
									result.addAll(((ResultMessage) o).getIds());
								}
							});

							resultMessage.setIds(result);

							// send it back to sender
							if (m.getResultCallback() == null)
							{
								sender().tell(resultMessage, self());
							}
							else
							{
								m.getResultCallback().resultCallback(resultMessage.getIds());
							}
						}
					},
					ec
			);


		}
	}


	private void sectorDestroy(SectorDestroyMessage m)
	{
//		LOG.info("sector destroyed!");
		//TODO: impl
	}

	private void positionMove(PositionMoveMessage message)
	{
		Point to = message.getTo();
		Point from = message.getFrom();
		Point topCorner = sectorData.getSquare().getTopLeftCorner();

		// do not have child sectors
		if (squareActorRefMap.size() == 0)
		{
			boolean canIChangePosition = sectorData.isInSector(to);
			boolean isInSector = sectorData.getIdPosMap().get(message.getId()) != null;

			if (!isInSector)
			{
				boolean inSector = sectorData.isInSector(from);
				throw new RuntimeException(
						String.format("object with id %s not found in sector (x: %s y: %s size: %s) position: (x: %s y: %s) should be in sector? %s",
								message.getId(), topCorner.getX(), topCorner.getY(), sectorData.getSquare().getSquareSize(), from.getX(), from.getY(), inSector));
			}

			if (canIChangePosition)
			{
				sectorData.getIdPosMap().put(message.getId(), to);
			}
			else
			{
				ActorSelection root = getContext().actorSelection(ROOT_SECTOR_NAME);
				Future<Object> createFuture = Patterns.ask(root, new PositionCreateMessage(message.getId(), message.getTo()), 10000);
				ExecutionContext ec = context().dispatcher();

				createFuture.onSuccess(
						new OnSuccess<Object>()
						{
							@Override
							public void onSuccess(Object o)
							{
								sectorData.getIdPosMap().remove(((PositionCreateMessage) o).getId());
								LOG.info("removed: " + ((PositionCreateMessage) o).getId());
							}
						},
						ec
				);

			}
		}
		else
		{
			// forward move message to right actor
			for (Square sectorSquare : squareActorRefMap.keySet())
			{
				if (sectorSquare.isInSquare(from))
				{
					squareActorRefMap.get(sectorSquare).forward(message, context());
					break;
				}
			}
		}
	}

	private void positionCreate(PositionCreateMessage message)
	{

		if (squareActorRefMap.size() == 0)
		{
			// create sub sectors and move old data to childs
			if (sectorData.getIdPosMap().size() > MAX_OBJECT_COUNT)
			{
				sectorData.getIdPosMap().put(message.getId(), message.getPoint());
				List<SectorData> newSectors = sectorData.splitSector(SUB_SECTOR_COUNT);
				for (SectorData newSector : newSectors)
				{
					ActorRef sectorActor = getContext().actorOf(SectorActor.props(newSector));
					squareActorRefMap.put(newSector.getSquare(), sectorActor);
				}

			}
			// add to this sector
			else
			{
				sectorData.getIdPosMap().put(message.getId(), message.getPoint());
			}
		}
		else
		{
			// forward move message to right actor
			for (Square sectorSquare : squareActorRefMap.keySet())
			{
				if (sectorSquare.isInSquare(message.getPoint()))
				{
					squareActorRefMap.get(sectorSquare).forward(message, context());
					break;
				}
			}
		}

		sender().tell(new PositionCreatedMessage(message.getId()), self());
	}

	private void positionDestroy(PositionDestroyMessage message)
	{
		sectorData.getIdPosMap().remove(message.getId());
	}

}

package com.game.server;

import com.game.server.world.geometry.AABB;
import com.game.server.world.geometry.Vector2;
import com.game.server.world.map.DynamicAABBTree;
import com.game.server.world.map.GameObject;
import com.game.server.world.map.WorldMap;
import com.game.server.world.objects.Wall;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author dohnal
 */

public class GUI extends JFrame
{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private static Random generator = new Random();

	public GUI(WorldMap<GameObject> map)
	{
		super("Game simulation");

		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(new Canvas(map, WIDTH, HEIGHT));
	}

	protected class Canvas extends JComponent
	{
		private int width;

		private int height;

		private WorldMap<GameObject> map;

		public Canvas(WorldMap<GameObject> map, int width, int height)
		{
			this.map = map;
			this.width = width;
			this.height = height;

			// TODO: repaint only on events in map
			new Timer().scheduleAtFixedRate(new TimerTask()
			{
				@Override
				public void run()
				{
					repaint();
				}
			}, 20, 20);
		}

		@Override
		public void paint(Graphics g)
		{
			renderAABBs(g);
			renderObjects(g);
		}

		private void renderAABBs(final Graphics g)
		{
			Iterator<AABB> aabbIterator = map.getAABBIterator();

			while (aabbIterator.hasNext())
			{
				AABB aabb = aabbIterator.next();

				Vector2 camPosition = worldToCanvas(new Vector2(aabb.getMin().getX(), aabb.getMax().getY()));

				g.drawRect((int) camPosition.getX(), (int) camPosition.getY(), (int) aabb.getWidth(), (int) aabb.getHeight());
			}
		}

		private void renderObjects(final Graphics g)
		{
			for (GameObject object : map)
			{
				AABB aabb = object.getAABB();

				Vector2 camPosition = worldToCanvas(new Vector2(aabb.getMin().getX(), aabb.getMax().getY()));

				g.fillRect((int) camPosition.getX(), (int) camPosition.getY(), (int) aabb.getWidth(), (int) aabb.getHeight());
			}
		}

		/**
		 * Transfer vector from world map coordinates to canvas coordinates
		 */
		private Vector2 worldToCanvas(Vector2 worldVector)
		{
			return new Vector2(worldVector.getX(), height - worldVector.getY() - 30);
		}
	}

	public static void main(String[] args)
	{
		new GUI(createMap());
	}

	private static WorldMap<GameObject> createMap()
	{
		final WorldMap<GameObject> map = new DynamicAABBTree<>();

		// add walls to map
		for (int i = 0; i < 5; i++)
		{
			int x = generator.nextInt(GUI.WIDTH - 200) + 100;
			int y = generator.nextInt(GUI.HEIGHT - 200) + 100;

/*			Wall wall = new Wall(x, y, 50, 50);

			map.add(wall);

			// add ability to move walls
			new Timer().scheduleAtFixedRate(new TimerTask()
			{
				@Override
				public void run()
				{
					wall.move(getRandomMove());

					map.update(wall);
				}
			}, 200, 200);*/
		}
		return map;
	}

	private static List<Vector2> getMoves()
	{
		List<Vector2> moves = new ArrayList<>();

		moves.add(new Vector2(0, 5));
		moves.add(new Vector2(0, -5));
		moves.add(new Vector2(5, 0));
		moves.add(new Vector2(-5, 0));

		return moves;
	}

	private static Vector2 getRandomMove()
	{
		List<Vector2> moves = getMoves();

		return moves.get(generator.nextInt(moves.size()));
	}
}

package com.game.server;

import com.game.server.world.behavior.KeyInputBehavior;
import com.game.server.world.geometry.Vector2;
import com.game.server.world.map.GameService;
import com.game.server.world.map.WorldMap;
import com.game.server.world.material.RectangleMaterial;
import com.game.server.world.material.base.Material;
import com.game.server.world.object.EventManager;
import com.game.server.world.object.Player;
import com.game.server.world.object.Wall;
import com.game.server.world.object.base.GameObject;
import com.jogamp.opengl.util.FPSAnimator;
import com.vividsolutions.jts.geom.Envelope;
import org.apache.log4j.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 * @author dohnal
 */

public class OpenGL
{
	private static final Logger LOG = Logger.getLogger(OpenGL.class);

	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;

	private static Random generator = new Random();

	private static WorldMap<GameObject> map;

	protected static void setup(GL2 gl2, int width, int height)
	{
		gl2.glMatrixMode( GL2.GL_PROJECTION );
		gl2.glLoadIdentity();

		// coordinate system origin at lower left with width and height same as the window
		GLU glu = new GLU();
		glu.gluOrtho2D( 0.0f, width, 0.0f, height );

		gl2.glMatrixMode( GL2.GL_MODELVIEW );
		gl2.glLoadIdentity();

		gl2.glViewport( 0, 0, width, height );
	}

	protected static void render(GL2 gl2, int width, int height)
	{
		gl2.glClear( GL.GL_COLOR_BUFFER_BIT );

		// draw a triangle filling the window
		gl2.glLoadIdentity();

		// render objects
		for (GameObject object : map)
		{
			drawRect(gl2, object.getMaterial());
		}
	}

	protected static void drawRect(GL2 gl2, Material material)
	{
		gl2.glBegin( GL.GL_POLYGON_OFFSET_FACTOR );


		if (material instanceof RectangleMaterial)
		{
			Color color = ((RectangleMaterial) material).getColor();
			gl2.glColor3d(color.getRed() / 256.0, color.getGreen() / 256.0, color.getBlue() / 256.0);
		}

		Envelope aabb = material.getBoundingBox();

		gl2.glRectd(aabb.getMinX(), aabb.getMaxY(), aabb.getMaxX(), aabb.getMinY());

		gl2.glEnd();
	}

	public static void main(String [] args)
	{
		map = createMap();

		GLProfile glprofile = GLProfile.getDefault();
		GLCapabilities glcapabilities = new GLCapabilities(glprofile);
		final GLCanvas glcanvas = new GLCanvas(glcapabilities);

		glcanvas.addGLEventListener( new GLEventListener()
		{
			@Override
			public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height)
			{
				OpenGL.setup(glautodrawable.getGL().getGL2(), width, height);
			}

			@Override
			public void init(GLAutoDrawable glautodrawable)
			{

			}

			@Override
			public void dispose(GLAutoDrawable glautodrawable)
			{
			}

			@Override
			public void display(GLAutoDrawable glautodrawable)
			{
				OpenGL.render(glautodrawable.getGL().getGL2(), glautodrawable.getSurfaceWidth(), glautodrawable.getSurfaceHeight());
			}
		});

		// add event manager game object
		EventManager eventManager = new EventManager();

		glcanvas.addKeyListener(eventManager.getBehavior(KeyInputBehavior.class));

		final JFrame jframe = new JFrame("Game simulation");
		jframe.addWindowListener( new WindowAdapter()
		{
			public void windowClosing(WindowEvent windowevent)
			{
				jframe.dispose();
				System.exit(0);
			}
		});

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("Info"));
		infoPanel.setPreferredSize(new Dimension(250, 400));

		JPanel scenePanel = new JPanel(new BorderLayout());
		scenePanel.setBorder(BorderFactory.createTitledBorder("Scene"));
		scenePanel.setPreferredSize(new Dimension(400, 100));

		jframe.getContentPane().add(scenePanel, BorderLayout.NORTH);
		jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
		jframe.getContentPane().add(infoPanel, BorderLayout.EAST);
		jframe.setSize(800, 600);
		jframe.setVisible(true);

		glcanvas.requestFocus();

		FPSAnimator animator = new FPSAnimator(glcanvas, 60);
		animator.start();
	}

	private static WorldMap<GameObject> createMap()
	{
		final WorldMap<GameObject> map = GameService.get().getWorldCollisionMap();

		map.add(new Player(new Vector2(OpenGL.WIDTH / 2 - 10, OpenGL.HEIGHT / 2 - 10)));

		// add walls to map
		for (int i = 0; i < 5; i++)
		{
			int x = generator.nextInt(OpenGL.WIDTH - 200) + 100;
			int y = generator.nextInt(OpenGL.HEIGHT - 200) + 100;

			map.add(new Wall(new Vector2(x, y)));
		}
		return map;
	}
}

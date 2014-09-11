package com.game.server;

import com.game.server.world.behavior.internal.KeyInputBehavior;
import com.game.server.world.map.GameService;
import com.game.server.world.map.WorldMap;
import com.game.server.world.material.RectangleMaterial;
import com.game.server.world.material.base.Material;
import com.game.server.world.object.EventManager;
import com.game.server.world.object.Player;
import com.game.server.world.object.Wall;
import com.game.server.world.object.base.GameObject;
import com.jogamp.opengl.util.FPSAnimator;
import com.vividsolutions.jts.geom.Coordinate;
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

	private static WorldMap<GameObject> world;

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

		gl2.glLoadIdentity();

		// render objects
		world.getObjects().forEach(object -> {
			if (object.getMaterial() != null)
			{
				fillRect(gl2, object.getMaterial());
			}
		});

		// render views
		world.getObjects().forEach(object -> {
			if (object.getViewBox() != null)
			{
				drawRect(gl2, object.getViewBox(), Color.green);
			}
		});
	}

	protected static void fillRect(GL2 gl2, Material material)
	{
		gl2.glBegin(GL.GL_POLYGON_OFFSET_FACTOR);

		if (material instanceof RectangleMaterial)
		{
			Color color = ((RectangleMaterial) material).getColor();
			gl2.glColor3d(color.getRed() / 256.0, color.getGreen() / 256.0, color.getBlue() / 256.0);
		}

		Envelope envelope = material.getBoundingBox();

		gl2.glRectd(envelope.getMinX(), envelope.getMaxY(), envelope.getMaxX(), envelope.getMinY());

		gl2.glEnd();
	}

	protected static void drawRect(GL2 gl2, Envelope envelope, Color color)
	{
		gl2.glBegin(GL.GL_LINE_LOOP);

		gl2.glColor3d(color.getRed() / 256.0, color.getGreen() / 256.0, color.getBlue() / 256.0);

		gl2.glVertex2d(envelope.getMinX(), envelope.getMinY());
		gl2.glVertex2d(envelope.getMaxX(), envelope.getMinY());
		gl2.glVertex2d(envelope.getMaxX(), envelope.getMaxY());
		gl2.glVertex2d(envelope.getMinX(), envelope.getMaxY());

		gl2.glEnd();
	}

	public static void main(String [] args)
	{
		world = createWorld();

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

		glcanvas.setPreferredSize(new Dimension(640, 480));

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("Info"));
		infoPanel.setPreferredSize(new Dimension(160, 480));

		JPanel scenePanel = new JPanel(new BorderLayout());
		scenePanel.setBorder(BorderFactory.createTitledBorder("Scene"));
		scenePanel.setPreferredSize(new Dimension(800, 120));

		jframe.getContentPane().add(scenePanel, BorderLayout.NORTH);
		jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
		jframe.getContentPane().add(infoPanel, BorderLayout.EAST);
		jframe.setSize(800, 600);
		jframe.setVisible(true);

		glcanvas.requestFocus();

		FPSAnimator animator = new FPSAnimator(glcanvas, 60);
		animator.start();
	}

	private static WorldMap<GameObject> createWorld()
	{
		WorldMap<GameObject> world = GameService.get().getWorldMap();

		world.add(new Player(new Coordinate(OpenGL.WIDTH / 2 - 10, OpenGL.HEIGHT / 2 - 10)));

		// add walls to map
		for (int i = 0; i < 5; i++)
		{
			int x = generator.nextInt(OpenGL.WIDTH - 200) + 100;
			int y = generator.nextInt(OpenGL.HEIGHT - 200) + 100;

			world.add(new Wall(new Coordinate(x, y)));
		}

		return world;
	}
}

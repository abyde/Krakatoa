package game.world3d.iso;

import game.world3d.Block;
import game.world3d.HeightBlock;
import game.world3d.IterableBlockWorld;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

import util.BufferCanvas;
import util.MergeIterators;

/**
 * A canvas that draws HeightBlocks in an isometric stylee.
 * 
 * @author abyde
 */
@SuppressWarnings("serial")
public class IsoBlockCanvas extends BufferCanvas implements MouseListener,
		MouseMotionListener {
	public static final double L = Math.sqrt(3.0) / 2.0;

	public static JFrame makeFrame(IsoBlockCanvas c) {
		JFrame f = new JFrame("Isometric World!");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(800, 600));
		c.xCenter = 400;
		c.yCenter = 300;
		Container pane = f.getContentPane();
		pane.setLayout(new BorderLayout());
		pane.add(c, BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);
		return f;
	}

	// the world in which stuff happens
	private IterableBlockWorld world;

	// a ball rolling around in it
	private Ball ball = null;
	private List<Ball> ballA = new ArrayList<Ball>();

	// pixels per logical unit.
	private double scale = 40.0;

	// screen location of the origin (0, 0, 0)
	private int xCenter, yCenter;

	// rotation angle
	private double theta = 0.0;

	// cosine of rotation angle
	private double tc;

	// sin of rotation angle
	private double ts;

	// for mouse movement
	private double startTheta;
	private int startX, startY, startXCenter, startYCenter;

	// height of a block
	private double zScale = 0.5;

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public double getZScale() {
		return zScale;
	}

	public void setZScale(double zScale) {
		this.zScale = zScale;
	}

	Comparator<Block>[][] cmp = new Comparator[2][2];

	public IsoBlockCanvas(IterableBlockWorld world) {
		this.world = world;
		setBall(null);
		setBackground(Color.white);
		setTheta(0);
		addMouseListener(this);
		addMouseMotionListener(this);

		cmp[0][0] = Block.VIEW_ORDER(1, 1);
		cmp[0][1] = Block.VIEW_ORDER(1, -1);
		cmp[1][0] = Block.VIEW_ORDER(-1, 1);
		cmp[1][1] = Block.VIEW_ORDER(-1, -1);
	}

	/**
	 * Set the ball to draw.
	 */
	public void setBall(Ball ball) {
		this.ball = ball;
		ballA.clear();
		if (ball != null) {
			ballA.add(ball);
		}
	}

	/**
	 * Set the angle of rotation. Calls repaint()
	 */
	void setTheta(double th) {
		theta = Math.min(Math.PI, Math.max(-Math.PI, th));
		tc = Math.cos(theta);
		ts = Math.sin(theta);
		repaint();
	}

	/**
	 * Draw all the blocks in the world.
	 * 
	 * @see java.awt.Canvas#paint(java.awt.Graphics)
	 */
	@SuppressWarnings("unchecked")
	public void paint(Graphics g) {

		// avoid memory allocation costs by assigning once per paint.
		double[] sx = new double[4];
		double[] sy = new double[4];
		int[] screenx = new int[4];
		int[] screeny = new int[4];
		// Color[] c = new Color[] { new Color(200, 200, 200), new Color(100,
		// 100, 100),
		// new Color(150, 150, 150) };
		Color[] c = new Color[] { new Color(200, 200, 200), Color.red,
				Color.green, Color.orange, Color.cyan };
		int[] faces = new int[3];

		// zone depends on the rotation. 0 ... 4 inclusive.
		int zone = (int) (theta / Math.PI * 2.0 + 2.5);

		// .. and which faces to draw depends on the zone!
		faces[1] = 1 + ((zone + 2) % 4);
		faces[2] = 1 + ((zone + 3) % 4);

		// which order to iterate in also depends on the zone.
		Iterator<Block> it;
		Comparator<Block> comp;
		if (zone == 2) {
			comp = cmp[0][0];
			it = world.allBlocksXY();
		} else if (zone == 3) {
			comp = cmp[1][0];
			it = world.allBlocksRevXY();
		} else if (zone == 1) {
			comp = cmp[0][1];
			it = world.allBlocksXRevY();
		} else {
			// 0 or 4
			comp = cmp[1][1];
			it = world.allBlocksRevXRevY();
		}
		it = new MergeIterators<Block>(comp, new Iterator[] { it, ballA.iterator() },
				new Block[2]);

		while (it.hasNext()) {
			Block block = it.next();
			
			if (block instanceof Ball) {
				// draw the ball
				Ball b = (Ball)block;
				ball(b, sx, sy);
				convertToScreen(sx, sy, screenx, screeny);
				int d = (int)(scale);
				g.setColor(Color.blue);
				g.fillOval(screenx[0], screeny[0], d, d);
				g.setColor(Color.black);
				g.drawOval(screenx[0], screeny[0], d, d);
			} else {
				IsoBlock b = (IsoBlock) block;
	
				for (int f = 0; f < 3; f++) {
					face(b, faces[f], sx, sy);
	
					// convert to screen coordinates.
					convertToScreen(sx, sy, screenx, screeny);
					// g.setColor(c[f]);
					g.setColor(c[faces[f]]);
					g.fillPolygon(screenx, screeny, 4);
	
					// edges
					g.setColor(Color.black);
					g.drawPolygon(screenx, screeny, 4);
				}
			}
		}
	}

	/**
	 * Convert logical to screen coordinates by scaling and shifting.
	 */
	private void convertToScreen(double[] sx, double[] sy, int[] screenx,
			int[] screeny) {
		for (int i = 0; i < 4; i++) {
			screenx[i] = xCenter + (int) (scale * sx[i]);
			screeny[i] = yCenter - (int) (scale * sy[i]);
		}
	}

	/**
	 * Get the coordinates for an isometric face.
	 */
	private void face(IsoBlock b, int faceType, double[] sx, double[] sy) {
		for (int corner = 0; corner < 4; corner++) {
			double xx = b.fx[faceType][corner];
			double yy = b.fy[faceType][corner];
			double z = b.fz[faceType][corner] * zScale;

			// rotate...
			double x = tc * xx + ts * yy;
			double y = -ts * xx + tc * yy;

			isometric(corner, sx, sy, x, y, z);
		}
	}

	private void isometric(int index, double[] sx, double[] sy, double x, double y, double z) {
		sx[index] = (y - x) * L;
		sy[index] = (-x / 2 - y / 2 + z) * L;
	}
	
	/**
	 * First "corner" is the center..
	 */
	private void ball(Ball b, double[] sx, double[] sy) {

		// rotate...
		double x = tc * b.p.x + ts * b.p.y;
		double y = -ts * b.p.x + tc * b.p.y;
		
		// project
		isometric(0, sx, sy, x, y, b.p.z);
	}

	/**
	 * Extend HeightBlock to explicitly construct geometry of all non-bottom
	 * faces.
	 */
	public static class IsoBlock extends HeightBlock {

		// 0 is x-y top;
		// 1 is x-z, 2 is y-z, 3 is far x-z, 4 is far y-z
		final double[][] fx = new double[][] { { 1, 0, 0, 1 }, { 1, 1, 1, 1 },
				{ 1, 1, 0, 0 }, { 0, 0, 0, 0 }, { 1, 1, 0, 0 } };
		final double[][] fy = new double[][] { { 1, 1, 0, 0 }, { 1, 0, 0, 1 },
				{ 1, 1, 1, 1 }, { 1, 0, 0, 1 }, { 0, 0, 0, 0 } };
		final double[][] fz;

		public IsoBlock(int x, int y, int z) {
			this(new double[][] { { 1, 1 }, { 1, 1 } }, x, y, z);
		}

		public IsoBlock(double[][] height, int x, int y, int z) {
			super(height, x, y, z);

			// construct faces.
			fz = new double[][] {
					{ height[1][1], height[0][1], height[0][0], height[1][0] },
					{ height[1][1], height[1][0], 0, 0 },
					{ height[1][1], 0, 0, height[0][1] },
					{ height[0][1], height[0][0], 0, 0 },
					{ height[1][0], 0, 0, height[0][0] } };

			for (int f = 0; f < fx.length; f++) {
				for (int c = 0; c < 4; c++) {
					fx[f][c] += x;
					fy[f][c] += y;
					fz[f][c] += z;
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isShiftDown()) {
			startXCenter = xCenter;
			startYCenter = yCenter;
			startX = e.getX();
			startY = e.getY();
		} else {
			startX = e.getX();
			startTheta = theta;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.isShiftDown()) {
			xCenter = startXCenter + (e.getX() - startX);
			yCenter = startYCenter + (e.getY() - startY);
			repaint();
		} else {
			double dtheta = (startX - e.getX()) / 200.0;
			setTheta(startTheta + dtheta);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}

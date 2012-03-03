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
import java.util.Iterator;

import javax.swing.JFrame;

import util.BufferCanvas;

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

	private double scale = 40.0;

	private IterableBlockWorld world;
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

	public IsoBlockCanvas(IterableBlockWorld world) {
		this.world = world;
		setBackground(Color.white);
		setTheta(0);
		addMouseListener(this);
		addMouseMotionListener(this);
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
		if (zone == 2)
			it = world.allBlocksXY();
		else if (zone == 3)
			it = world.allBlocksRevXY();
		else if (zone == 1)
			it = world.allBlocksXRevY();
		else
			// 0 or 4
			it = world.allBlocksRevXRevY();

		while (it.hasNext()) {
			IsoBlock b = (IsoBlock) it.next();

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

			double x = tc * xx + ts * yy;
			double y = -ts * xx + tc * yy;

			// rotate...
			sx[corner] = (y - x) * L;
			sy[corner] = (-x / 2 - y / 2 + z) * L;
		}
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
			xCenter = startXCenter + (e.getX()-startX);
			yCenter = startYCenter + (e.getY()-startY);
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

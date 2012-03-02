package game.world3d.iso;

import game.world3d.Block;
import game.world3d.BlockWorld;
import game.world3d.BlockWorldImpl;
import game.world3d.HeightBlock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JFrame;

import util.BufferCanvas;

/**
 * A canvas that draws HeightBlocks in isometric stylee.
 * 
 * @author abyde
 */
@SuppressWarnings("serial")
public class IsoBlockCanvas extends BufferCanvas {
	public static final double L = Math.sqrt(3.0) / 2.0;

	public static void main(String[] args) {
		IsoBlockCanvas c = new IsoBlockCanvas();
		c.world = new BlockWorldImpl();

		int w = 4;
		int n = 3;
		Random r = new Random(1);
		for (int i = -w; i <= w; i++) {
			c.world.add(new HeightBlock(i, -w - 1, 0));
			c.world.add(new HeightBlock(i, w + 1, 0));
			c.world.add(new HeightBlock(-w - 1, i, 0));
			c.world.add(new HeightBlock(w + 1, i, 0));

			if (Math.abs(i) <= 1) {
				c.world.add(rand(r, i, -w - 2, 0));
				c.world.add(rand(r, i, w + 2, 0));
				c.world.add(rand(r, -w - 2, i, 0));
				c.world.add(rand(r, w + 2, i, 0));
			}
		}

		for (int i = 0; i < n; i++) {
			c.world.add(new HeightBlock(-w - 1, -w - 1, i));
			c.world.add(new HeightBlock(-w - 1, w + 1, i));
			c.world.add(new HeightBlock(w + 1, -w - 1, i));
			c.world.add(new HeightBlock(w + 1, w + 1, i));
		}

		makeFrame(c);
	}

	private static HeightBlock rand(Random r, int x, int y, int z) {
		return new HeightBlock(new double[][] {
				{ r.nextDouble(), r.nextDouble() },
				{ r.nextDouble(), r.nextDouble() } }, x, y, z);
	}

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

	private double xScale = 40.0;
	private double yScale = 20.0;

	private BlockWorld world;
	private double xCenter = 0;
	private double yCenter = 0;

	IsoBlockCanvas() {
		setBackground(Color.white);
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
		Color[] c = new Color[] { new Color(100, 100, 100),
				new Color(150, 150, 150), new Color(200, 200, 200) };

		Iterator<Block> it = world.allBlocks();
		while (it.hasNext()) {
			HeightBlock b = (HeightBlock) it.next();

			for (int f = 0; f < 3; f++) {
				face(b, f, sx, sy);

				// convert to screen coordinates.
				convertToScreen(sx, sy, screenx, screeny);
				g.setColor(c[f]);
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
			screenx[i] = (int) (xCenter + xScale * sx[i]);
			screeny[i] = (int) (yCenter - yScale * sy[i]);
		}
	}

	/**
	 * Get the coordinates for an isometric face!
	 * 
	 * @return coords relative to the common central point (whose location is
	 *         x=1, y=1, z=1).
	 * @param faceType
	 *            0 = x,z; 1= y,z; 2=x,y
	 */
	public void face(HeightBlock b, int faceType, double[] sx, double[] sy) {

		double dx = (b.x() - b.y()) * L;
		double dy = (-b.x() / 2.0 - b.y() / 2.0 + b.z());

		switch (faceType) {
		case 0:
			// x-z
			sx[0] = dx + 0;
			sx[1] = dx + 0;
			sx[2] = dx + L;
			sx[3] = dx + L;
			sy[0] = dy - 1 + b.height[1][1];
			sy[1] = dy - 1;
			sy[2] = dy - 0.5;
			sy[3] = dy - 0.5 + b.height[0][1];
			break;
		case 1:
			// y-z
			sx[0] = dx;
			sx[1] = dx - L;
			sx[2] = dx - L;
			sx[3] = dx;
			sy[0] = dy - 1 + b.height[1][1];
			sy[1] = dy - 0.5 + b.height[1][0];
			sy[2] = dy - 0.5;
			sy[3] = dy - 1;
			break;
		case 2:
			// x-y
			sx[0] = dx;
			sx[1] = dx + L;
			sx[2] = dx;
			sx[3] = dx - L;
			sy[0] = dy - 1 + b.height[1][1];
			sy[1] = dy - 0.5 + b.height[0][1];
			sy[2] = dy + b.height[0][0];
			sy[3] = dy - 0.5 + b.height[1][0];
			break;
		}
	}

}

package game.world3d;

import game.world3d.iso.IsoBlockCanvas;
import game.world3d.iso.IsoBlockCanvas.IsoBlock;

import java.util.Random;

public class BigTest {

	public static void main(String[] args) {
		IterableBlockWorld world = new BlockWorldImpl();
		IsoBlockCanvas c = new IsoBlockCanvas(world);
		c.setScale(10);
		c.setZScale(1.0);

		// 900 blocks!
		int w = 30;
		int zz = 30;
		Random r = new Random(1);
		for (int i = -w; i <= w; i++) {
			for(int j = -w; j <= w; j++ ) {
				double x = 2*i / (double)w;
				double y = 2*j / (double)w;
				double f = Math.exp(-x*x-y*y);
				int z = (int)(zz * f * (1 + 0.3*r.nextDouble()));
				world.add(rand(r, i, j, z));
			}
		}

		IsoBlockCanvas.makeFrame(c);
	}

	private static IsoBlock rand(Random r, int x, int y, int z) {
		return new IsoBlock(new double[][] {
				{ r.nextDouble(), r.nextDouble() },
				{ r.nextDouble(), r.nextDouble() } }, x, y, z);
	}

}

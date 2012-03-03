package game.world3d;

import game.world3d.iso.IsoBlockCanvas;
import game.world3d.iso.IsoBlockCanvas.IsoBlock;

import java.util.Random;

public class SandCastleTest {

	public static void main(String[] args) {
		IterableBlockWorld world = new BlockWorldImpl();
		IsoBlockCanvas c = new IsoBlockCanvas(world);
		
		int w = 4;
		int n = 3;
		Random r = new Random(1);
		for (int i = -w; i <= w; i++) {
			world.add(new IsoBlock(i, -w - 1, 0));
			world.add(new IsoBlock(i, w + 1, 0));
			world.add(new IsoBlock(-w - 1, i, 0));
			world.add(new IsoBlock(w + 1, i, 0));

			if (Math.abs(i) <= 1) {
				world.add(rand(r, i, -w - 2, 0));
				world.add(rand(r, i, w + 2, 0));
				world.add(rand(r, -w - 2, i, 0));
				world.add(rand(r, w + 2, i, 0));
			}
		}

		for (int i = 0; i < n; i++) {
			world.add(new IsoBlock(-w - 1, -w - 1, i));
			world.add(new IsoBlock(-w - 1, w + 1, i));
			world.add(new IsoBlock(w + 1, -w - 1, i));
			world.add(new IsoBlock(w + 1, w + 1, i));
		}

		IsoBlockCanvas.makeFrame(c);
	}

	private static IsoBlock rand(Random r, int x, int y, int z) {
		return new IsoBlock(new double[][] {
				{ r.nextDouble(), r.nextDouble() },
				{ r.nextDouble(), r.nextDouble() } }, x, y, z);
	}

}

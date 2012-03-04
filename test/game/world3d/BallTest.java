package game.world3d;

import game.world3d.iso.Ball;
import game.world3d.iso.Dynamics;
import game.world3d.iso.IsoBlockCanvas;
import game.world3d.iso.IsoBlockCanvas.IsoBlock;

import javax.vecmath.Vector3d;

public class BallTest {

	public static void main(String[] args) {
		IterableBlockWorld world = new BlockWorldImpl();
		IsoBlockCanvas c = new IsoBlockCanvas(world);
		c.setScale(22);
		c.setZScale(1.0);

		int zz = 4;
		int w = 10;
		for (int i = -w; i <= w; i++) {
			if (Math.abs(i) != w) {
				for (int z = 1; z < zz; z++) {
					world.add(new IsoBlock(i, -w, z));
					world.add(new IsoBlock(i, w, z));
				}
			}
			for (int j = -w; j <= w; j++) {
				if (Math.abs(i) == w) {
					for (int z = 1; z < zz; z++) {
						world.add(new IsoBlock(i, j, z));
						world.add(new IsoBlock(i, j, z));
					}
				}
				world.add(new IsoBlock(i, j, 0));
			}
		}

		Ball b = new Ball(0, 0, 10);
		b.a = new Vector3d(0.12, 0.45, -1);
		Dynamics d = new Dynamics(world, b);

		c.setBall(b);
		IsoBlockCanvas.makeFrame(c);

		try {
			while (true) {
				Thread.sleep(10);
				d.move();
				c.repaint();
			}
		} catch (InterruptedException e) {
			System.exit(1);
		}
	}

}

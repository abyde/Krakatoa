package game.world3d.iso;

import game.world3d.Block;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Ball of diameter 1.
 * 
 * @author abyde
 */
public class Ball extends Block {

	public Ball(double x, double y, double z) {
		super(0, (int) x, (int) y, (int) z);
		p = new Point3d(x, y, z);
		v = new Vector3d(0, 0, 0);
		a = new Vector3d(0, 0, 0);
	}

	public Point3d p;
	public Vector3d v;
	public Vector3d a;

}

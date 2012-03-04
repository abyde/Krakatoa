package game.world3d.iso;

import game.world3d.BlockWorld;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Dynamics {

	private double dt = 0.1;
	private Point3d nextCenter = new Point3d();
	private Ball ball;
	private BlockWorld world;
	private double r = 0.5;

	public Dynamics(BlockWorld world, Ball ball) {
		this.world = world;
		this.ball = ball;
	}

	/**
	 * Move the ball in the world.
	 * 
	 * @param world
	 * @param ball
	 */
	public void move() {
		// work out the next center
		ball.v.get(nextCenter);
		nextCenter.scaleAdd(dt, ball.p);

		nextCenter.get(ball.p);

		ball.a.get(nextCenter);
		nextCenter.scaleAdd(dt, ball.v);
		nextCenter.get(ball.v);

		// System.out.println(" now a = " + ball.a + ", v = " + ball.v + ", p="
		// + ball.p);
	}

	/* "top" face bounce */
	private void checkBounceZPlus(double z) {
		if (nextCenter.z - r < z) {
			ball.v.z = -ball.v.z;
			ball.v.scale(0.9);
			nextCenter.z = z + r;
		}
	}

	private void checkBounceXPlus(double x) {
		if (nextCenter.x - r < x) {
			ball.v.x = -ball.v.x;
			ball.v.scale(0.9);
			nextCenter.x = x + r;
		}
	}

	private void checkBounceXMinus(double x) {
		if (nextCenter.x + r > x) {
			ball.v.x = -ball.v.x;
			ball.v.scale(0.9);
			nextCenter.x = x - r;
		}
	}

	/**
	 * If it has crossed the plane defined by normal.v = p then bounce back.
	 */
	private void checkBouncePlane(Vector3d normal, double p) {
		double bp = normal.dot(new Vector3d(nextCenter));

		// TODO work out how much of the time was spent getting to the plane,
		// and use the rest of the time rebounding.
		if (bp > p) {
			ball.v.x = -ball.v.x;
			ball.v.scale(0.9);
			nextCenter.x = x - r;
		}
	}

}

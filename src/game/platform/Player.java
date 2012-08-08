package game.platform;

public class Player {
	DLocation loc;
	DLocation v;
	int dax, day;
	MotionState motion;
	Platform support;
	
	enum MotionState {
		WALKING, FALLING
	}
	
	Player(DLocation loc) {
		this.loc = loc;
		v = new DLocation(0, 0);
	}
	
	public void move(double dt) {
		v.x += dt * dax;
		v.y += dt * day;
		loc.x += dt * v.x;
		loc.y += dt * v.y;
	}
	
	public void run(int dx) {
		switch (motion) {
		case FALLING:
			break;
		case WALKING:
			dax = dx;
		}
	}
	
	/**
	 * If walking, make us suddenly jump!
	 */
	public void jump() {
		switch (motion) {
		case FALLING:
			break;
		case WALKING:
			// impulse upwards
			v.y = 2.7;
			fall();
		}
	}
	
	public void fall() {
		support = null;
		motion = MotionState.FALLING;
		dax = 0;
		day = -1;
	}
	
	public void land(Platform support) {
	//	System.out.println("Land at " + loc + " on platform " + support);
		dax = 0;
		day = 0;
		v.y = 0;
		loc.y = support.y+1;
		this.support = support;
		motion = MotionState.WALKING;
		
		// bit of landing friction...
		v.x /= 2;
	}
	
	/**
	 * Obstructed from one of four sides.  nx, ny is the normal vector.
	 */
	public void reflect(int nx, int ny) {
		if (ny == 0) {
			// wall
		}
	}
}

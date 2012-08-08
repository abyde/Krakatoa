package game.platform;

public class DLocation {
	double x, y;

	public DLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}

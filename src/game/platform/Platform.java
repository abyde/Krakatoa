package game.platform;

public class Platform {
	final int x, y, length;

	Platform(int x, int y, int length) {
		this.x = x;
		this.y = y;
		this.length = length;
	}

	public boolean xIntersec(double x) {
		return (x >= this.x) && (x < this.x + length);
	}

	public boolean yIntersec(double y) {
		return (y >= this.y) && (y < this.y + 1);
	}

	public String toString() {
		return "(x=[" + x + ", " + (length + x - 1) + "], y=" + y + ")";
	}
	
}

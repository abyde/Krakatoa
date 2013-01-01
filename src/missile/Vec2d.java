package missile;

public class Vec2d {
	public static Vec2d NOT_A_VECTOR = new Vec2d(Double.NaN, Double.NaN);
	
	private double x, y;
	
	public Vec2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double x() {
		return x;
	}
	
	public double y() {
		return y;
	}
	
	public Vec2d add(Vec2d v) {
		if (this == NOT_A_VECTOR || v == NOT_A_VECTOR)
			return NOT_A_VECTOR;
		
		return new Vec2d(x+v.x, y+v.y);
	}
	
	public Vec2d scale(double lambda) {
		if (this == NOT_A_VECTOR)
			return NOT_A_VECTOR;
		
		return new Vec2d(lambda*x, lambda*y);
	}
	
	public static Vec2d diff(Vec2d v1, Vec2d v2) {
		if (v1 == NOT_A_VECTOR || v2 == NOT_A_VECTOR)
			return NOT_A_VECTOR;
					
		return new Vec2d(v1.x - v2.x, v1.y - v2.y);
	}
	
	public Vec2d normal() {
		if (this == NOT_A_VECTOR)
			return NOT_A_VECTOR;
		
		double length = length();
		if (length == 0.0)
			return NOT_A_VECTOR;
		return new Vec2d(x/length, y/length);
	}
	
	/**
	 * Calculate the projection of the provided vector onto this one -- result is a multiple of this vector.
	 */
	public Vec2d parallelCompOf(Vec2d v) {
		Vec2d n = normal();
		if (n == NOT_A_VECTOR)
			return NOT_A_VECTOR;
		double l = n.dot(v);
		return n.scale(l);
	}
	
	/**
	 * Calculate the perpendicular component of the given vector from this one.
	 */
	public Vec2d perpCompOf(Vec2d v) {
		return diff(v, parallelCompOf(v));
	}
	
	public double dot(Vec2d v) {
		if (this == NOT_A_VECTOR || v == NOT_A_VECTOR)
			return Double.NaN;
		
		return x*v.x + y*v.y;
	}
	
	public double length() {
		return Math.sqrt(dot(this));
	}
}

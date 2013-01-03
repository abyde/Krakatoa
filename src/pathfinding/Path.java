package pathfinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import pathfinding.WorldAStar.Node;

/**
 * A path determined by some path finding algorithm. A series of steps from the
 * starting location to the target location. This includes a step for the
 * initial location.
 * 
 * @author Kevin Glass
 */
public class Path {
	
	/** The list of steps building up this path */
	private List<Point> steps = new ArrayList<Point>();

	/**
	 * Create an empty path
	 */
	public Path() {}

	public Path(List<Point> list) {
		this.steps = list;
	}
	
	/**
	 * Get the length of the path, i.e. the number of steps
	 * 
	 * @return The number of steps in this path
	 */
	public int getLength() {
		return steps.size();
	}

	/**
	 * Get the step at a given index in the path
	 * 
	 * @param index
	 *            The index of the step to retrieve. Note this should be >= 0
	 *            and < getLength();
	 * @return The step information, the position on the map.
	 */
	public Point getPoint(int index) {
		return (Point) steps.get(index);
	}

	/**
	 * Get the x coordinate for the step at the given index
	 * 
	 * @param index
	 *            The index of the step whose x coordinate should be retrieved
	 * @return The x coordinate at the step
	 */
	public int getX(int index) {
		return getPoint(index).x;
	}

	/**
	 * Get the y coordinate for the step at the given index
	 * 
	 * @param index
	 *            The index of the step whose y coordinate should be retrieved
	 * @return The y coordinate at the step
	 */
	public int getY(int index) {
		return getPoint(index).y;
	}

	/**
	 * Append a step to the path.
	 * 
	 * @param x
	 *            The x coordinate of the new step
	 * @param y
	 *            The y coordinate of the new step
	 */
	public void appendPoint(int x, int y) {
		steps.add(new Point(x, y));
	}

	/**
	 * Prepend a step to the path.
	 * 
	 * @param x
	 *            The x coordinate of the new step
	 * @param y
	 *            The y coordinate of the new step
	 */
	public void prependPoint(int x, int y) {
		steps.add(0, new Point(x, y));
	}

	/**
	 * Check if this path contains the given step
	 * 
	 * @param x
	 *            The x coordinate of the step to check for
	 * @param y
	 *            The y coordinate of the step to check for
	 * @return True if the path contains the given step
	 */
	public boolean contains(int x, int y) {
		return steps.contains(new Point(x, y));
	}
	
	/**
	 * Build a path out of a list of nodes.
	 */
	public static Path pathFromNodes(List<Node> l){
		Path p = new Path();
		for(int i = 1; i < l.size(); i++){
			Node c = l.get(i);
			p.appendPoint(c.x, c.y);
		}
		return p;
	}
	
}

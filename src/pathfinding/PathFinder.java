package pathfinding;

import java.awt.Point;

/**
 * An object capable of calculating paths between two points.
 * 
 * @author Lucas
 */
public interface PathFinder {

	Path findLandPath(Point start, Point end);
	Path findSeaPath(Point start, Point end);
}

package pathfinding;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

public class DirectPathFinder implements PathFinder {

	@Override
	public Path findLandPath(Point start, Point end) {
		List<Point> pointList = (List<Point>)Arrays.asList(start, end);
		return new Path(pointList);
	}

	@Override
	public Path findSeaPath(Point start, Point end) {
		List<Point> pointList = (List<Point>)Arrays.asList(start, end);
		return new Path(pointList);
	}

}

package pathfinding;

import java.awt.Point;

//import riseOfDots.Static;

/**
 * An implementation of {@link PathFinder} using the A-Star algorithm via
 * {@link WorldAStar}.
 * 
 * @author Lucas
 */
public class AStarPathFinder implements PathFinder {

	public AStarPathFinder() {
	}

	public Path findLandPath(Point start, Point end) {
		//WorldAStar pf = new WorldAStar(Static.world.walkable, end);
		//return Path.pathFromNodes(pf.compute(new WorldAStar.Node(start.x, start.y)));
		return null;
	}

	@Override
	public Path findSeaPath(Point start, Point end) {
		//WorldAStar pf = new WorldAStar(Static.world.navigable, end);
		//return Path.pathFromNodes(pf.compute(new WorldAStar.Node(start.x, start.y)));
		return null;
	}

}

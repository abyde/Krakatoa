package riseOfDots.pathFinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import riseOfDots.World;
import riseOfDots.entity.Mover;
import util.Utils;

public class PathFinder {

	/** A queue of partial paths, from shortest to longest. */
	PriorityQueue<Node> openList = new PriorityQueue<Node>();

	HashMap<Point, Node> locationMap = new HashMap<Point, Node>();

	/** List of locations that we no longer need to evaluate. */
	Set<Point> closedList = new HashSet<Point>();

	Node begin;
	Point end;
	World w;
	Mover m;
	int distance = 1000;

	/**
	 * Find a path between 'start' and 'finish' in the given world.
	 */
	public PathFinder(Point start, Point end, World w, Mover m) {
		begin = new Node(null, start, end);
		this.end = end;
		this.w = w;
		this.m = m;
	}

	/**
	 * set path search distance, 1000 by default
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * Retrieve the shortest path.
	 */
	private Node pollShortest() {
		Node n = openList.poll();
		if (n == null)
			return null;
		locationMap.remove(n.location);
		return n;
	}

	/**
	 * Add a new path to a totally new location -- neither in the closed list
	 * nor in the open list.
	 */
	private void addNewPath(Node node) {
		assert !locationMap.containsKey(node.location);
		assert !closedList.contains(node.location);
		openList.add(node);
		locationMap.put(node.location, node);
	}

	private void update(Node existingPath, Node newPath) {
		assert (existingPath.location.equals(newPath.location));
		existingPath.parent = newPath.parent;
		existingPath.f = newPath.f;
		existingPath.g = newPath.g;
		existingPath.h = newPath.h;
	}

	/**
	 * Run the algorithm.
	 */
	public ArrayList<Point> getPath() {
		this.addNewPath(begin);

		ArrayList<Point> path = null;
		for (int distanceSearched = 0; distanceSearched < distance; distanceSearched++) {
			// get the most promising path so far.
			Node n = pollShortest();
			if (n == null)
				return null;

			// mark the location closed before proceeding.
			closedList.add(n.location);

			System.out.println("d = " + distanceSearched + ", n = " + n + ", open = " + openList + ", openMap = " + locationMap + ", closed = "
					+ closedList);

			n = explore(n);
			if (n != null) {
				// found the target!
				path = new ArrayList<Point>();
				Point p;
				do {
					p = n.location;
					path.add(0, p);
					n = n.parent;
				} while (n != null);
				break;
			}
		}
		return path;
	}

	/**
	 * put all movable nodes that are not on the closed list into the open list
	 */
	private Node explore(Node n) {
		for (int x = Math.max(0, n.location.x - 1); x <= Math.min(n.location.x + 1, w.size() - 1); x++) {
			for (int y = Math.max(0, n.location.y - 1); y <= Math.min(n.location.y + 1, w.size() - 1); y++) {

				// displacement must be non-zero
				if (x == 0 && y == 0)
					continue;

				// blocked
				if (!w.canMove(m, x, y))
					continue;

				// where is this new point?
				Point newLocation = new Point(x, y);

				// visited it already
				if (closedList.contains(newLocation))
					continue;

				// if there is an existing node for this location then
				// update the costs in place.
				Node newNode = new Node(n, newLocation, end);
				Node existingNode = locationMap.get(newLocation);
				if (existingNode == null) {
					addNewPath(newNode);
				} else {

					// shorter path => update.
					// System.out.println("    Old path = " +
					// existingNode.toStringPath() + " cost = " +
					// existingNode.toStringCost());
					// System.out.println("    New path = " +
					// newNode.toStringPath() + " cost = " +
					// newNode.toStringCost());
					if (existingNode.f > newNode.f) {
						update(existingNode, newNode);
					}
				}

				// check if this node is the target
				if (newNode.h == 0)
					return newNode;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		/*
		 * remember that this array is the transpose of what will appear:
		 * terrain[0] is the first *column* not the first *row*.
		 */
		int[][] terrain = new int[][] { //
		{ 3, 3, 3, 3, 3, 3, 3 }, //
				{ 3, 3, 3, 1, 1, 3, 3 }, //
				{ 1, 1, 3, 1, 1, 1, 3 }, //
				{ 1, 3, 3, 3, 3, 3, 3 }, //
				{ 3, 3, 1, 1, 1, 1, 1 }, //
				{ 3, 1, 1, 3, 3, 3, 3 }, //
				{ 3, 3, 3, 3, 3, 3, 3 } };
		World w = new World(7, terrain);
		Mover walker = new Mover() {
			@Override
			public boolean canWalk() {
				return true;
			}

			@Override
			public boolean canNavigate() {
				return false;
			}
		};

		Point start = new Point(0, 0);
		Point end = new Point(6, 6);
		PathFinder pf = new PathFinder(start, end, w, walker);
		ArrayList<Point> path = pf.getPath();
		System.out.println("Path = " + path);
		if (path == null)
			System.out.println("UNREACHABLE");

		for (int y = 0; y < terrain.length; y++) {
			for (int x = 0; x < terrain.length; x++) {
				Point p = new Point(x, y);
				if (p.equals(start) || p.equals(end)) {
					System.out.print(" o ");
					continue;
				}
				if (path.contains(p))
					System.out.print(Utils.pad(2, "" + path.indexOf(p)) + " ");
				else {
					if (terrain[x][y] == 3)
						System.out.print("   ");
					else
						System.out.print(" # ");
				}
			}
			System.out.println();
		}
	}

}

class Node implements Comparable<Node> {

	public Node parent;
	Point location;
	int f, g, h;

	public Node(Node parent, Point location, Point target) {
		this.parent = parent;
		this.location = location;
		if (parent != null) {
			int parentDistance = manhatten(parent, this);
			assert parentDistance > 0;
			assert parentDistance <= 2;
			if (parentDistance == 2)
				g = parent.g + 14;
			else
				g = parent.g + 10;
		} else
			g = 0;
		h = 10 * manhatten(target, location);
		f = g + h;
	}

	/**
	 * Manhatten distance between two.
	 */
	public static int manhatten(Point p1, Point p2) {
		return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
	}

	/**
	 * Manhatten distance between two.
	 */
	public static int manhatten(Node n1, Node n2) {
		return Math.abs(n1.location.x - n2.location.x) + Math.abs(n1.location.y - n2.location.y);
	}

	@Override
	public int compareTo(Node node) {
		return f - node.f;
	}

	public String toString() {
		return "(x=" + location.x + ",y=" + location.y + ",f=" + f + ",g=" + g + ",h=" + h + ")";
	}

	public String toStringCost() {
		return "g=" + g + " + h=" + h + " = f=" + f;
	}

	public String toStringPath() {
		String me = "(x=" + location.x + ",y=" + location.y + ")";
		if (parent == null)
			return me;
		else
			return parent.toStringPath() + "=" + me;
	}
}
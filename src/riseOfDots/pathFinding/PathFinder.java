package riseOfDots.pathFinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

import riseOfDots.World;
import riseOfDots.entity.Mover;

public class PathFinder {
	
	PriorityQueue<Node> openList = new PriorityQueue<Node>();
	ArrayList<Point> closedList = new ArrayList<Point>();
	
	Node begin;
	Point end;
	World w;
	Mover m;
	int distance = 1000;
	/*
	 * ox, oy are origin coordinates. tx and ty are target coordinate
	 */
	public PathFinder(int ox, int oy, int tx, int ty, World w, Mover m){
		begin = new Node(null, ox, oy, ox, oy, tx, ty);
		end = new Point(tx, ty);
		this.w = w;
		this.m = m;
	}
	
	/*
	 * set path search distance, 1000 by default
	 */
	public void setDistance(int distance){
		this.distance = distance;
	}
	
	/*
	 * begins the algorithm
	 */
	public ArrayList<Point> getPath(){
		openList.add(begin);
		ArrayList<Point> path = null;
		for(int distanceSearched = 0; distanceSearched < distance; distanceSearched++){
			Node n = openList.poll();
			closedList.add(new Point(n.x, n.y));
			Node target = explore(n);
			if(target != null){
				path = new ArrayList<Point>();
				path.add(end);
				Node prev = target;
				while(true){
					prev = prev.parent;
					if(prev == null)
						break;
					if(prev.equals(begin))
						break;
					path.add(new Point(prev.x, prev.y));
					Collections.reverse(path);
				}
				break;
			}
		}
		return path;
	}
	
	/*
	 * put all movable nodes that are not on the closed list into the open list
	 */
	private Node explore(Node n){
		for(int x = n.x - 1; x <= n.x + 1; x++)
			for(int y = n.y - 1; y <= n.y + 1; y++){
				//check if coordinates are out of bounds, in closed list or unmovable
				if(x < 0 | x >= w.size() | y < 0 | y >= w.size()
						| closedList.contains(new Point(x, y)) | !w.canMove(m, x, y)) continue;
				
				Node c = new Node(n, x, y, begin.x, begin.y, end.x, end.y);
				if(!openList.contains(c))
					openList.add(c);
				else{
					openList.remove(c);
					int g1 = c.g;
					int g2 = n.g + 10;
					if(n.x != c.x & n.y != c.y)
						g2 += 4;
					if(g2 < g1)
						c.parent = n;
					openList.add(c);
				}
				
				//check if this node is the target
				if(c.x == end.x & c.y == end.y)
					return c;
			}
		return null;
	}
	
	public static void main(String[] args){
		int[][] terrain = new int[][]{
				{3, 3, 3, 3, 3, 3, 3},
				{3, 3, 3, 3, 3, 3, 3},
				{3, 3, 3, 1, 3, 3, 3},
				{3, 3, 3, 1, 3, 3, 3},
				{3, 3, 3, 1, 3, 3, 3},
				{3, 3, 3, 3, 3, 3, 3},
				{3, 3, 3, 3, 3, 3, 3}
		};
		World w = new World(7, terrain);
		PathFinder pf = new PathFinder(3, 1, 3, 5, w, null);
		ArrayList<Point> path = pf.getPath();
		for(int x = 0; x < 7; x++){
			for(int y = 0; y < 7; y++){
				if((x == 3 & y == 1) | (x == 3 & y == 5)){
					System.out.print("o ");
					continue;
				}
				Point p = new Point(x, y);
				if(path.contains(p)) System.out.print(path.indexOf(p)+" ");
				else{
					if(terrain[x][y] == 3)
						System.out.print("  ");
					else
						System.out.print("| ");
				}
			}
			System.out.println();
		}
	}
	
}

class Node implements Comparable<Node>{
	
	public Node parent;
	public int x, y, g, h, f, tx, ty;
	
	public Node(Node parent, int x, int y, int ox, int oy, int tx, int ty){
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.tx = tx;
		this.ty = ty;
		if(parent != null){
			g = parent.g + 10;
			if(parent.x != x & parent.y != y) g += 4;
		}
		else
			g = 0;
		h = Math.abs(tx - x) + Math.abs(ty - y);
		f = g + h;
	}

	@Override
	public int compareTo(Node node) {
		if(node.x == x & node.y == y) return 0;
		if(node.f > f) return 1;
		else if(node.f == f) return 0;
		else return -1;
	}
	
	public void recalculate(){
		if(parent != null){
			g = parent.g + 10;
			if(parent.x != x & parent.y != y) g += 4;
		}
		else
			g = 0;
		h = Math.abs(tx - x) + Math.abs(ty - y);
		f = g + h;
	}
	
}
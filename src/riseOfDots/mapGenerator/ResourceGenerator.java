package riseOfDots.mapGenerator;

//import java.awt.Point;
import java.util.Random;

import riseOfDots.Static;
import riseOfDots.World;
import riseOfDots.entity.Tree;

public class ResourceGenerator {
	
	public static void genResources(World w, long seed){
		Random r = new Random();
		r.setSeed(seed);
		
//		int maxForests = 50;
//		int maxTrees = 500;
//		int minTreeDistance = 1;
//		int maxTreeDistance = 5;
//		
//		int forests = (int) (r.nextDouble() * maxForests);
//		for(int i = 0; i < forests; i++){
//			Point p = newLocation(w, r);
//			int trees = (int) (r.nextDouble() * maxTrees);
//			System.out.println(p.x+" "+p.y);
//			for(int t = 0; t < trees; t++){
//				w.add(new Tree(p.x, p.y));
//				Point newTreeLocation = nextNearLocation(w, p.x, p.y, minTreeDistance,
//						maxTreeDistance, r);
//				p = newTreeLocation;
//			}
//			System.out.println(i+" forests generated.");
//		}
		for(int x = 0; x < w.size(); x++)
			for(int y = 0; y < w.size(); y++)
				if(w.terrainAt(x, y) == Static.TERRAIN_EARTH & w.at(x, y) == null &
				r.nextDouble() < 0.01)
					w.add(new Tree(x, y));
	}
	
//	private static Point newLocation(World w, Random r){
//		boolean valid = false;
//		int x = 0, y = 0;
//		while(!valid){
//			x = (int) (r.nextDouble() * w.size());
//			y = (int) (r.nextDouble() * w.size());
//			if(w.at(x, y) == null & w.terrainAt(x, y) == Static.TERRAIN_EARTH)
//				valid = true;
//		}
//		return new Point(x, y);
//	}
	
//	private static Point nextNearLocation(World w, int tx, int ty, int min, int max,
//			Random r){
//		boolean valid = false;
//		int x = 0;
//		int y = 0;
//		while(!valid){
//			x = (int) ((r.nextDouble() - 0.5) * (max - min)) + tx;
//			y = (int) ((r.nextDouble() - 0.5) * (max - min)) + ty;
//			if(w.at(x, y) == null & w.terrainAt(x, y) == Static.TERRAIN_EARTH)
//				valid = true;
//		}
//		return new Point(x, y);
//	}
	
}

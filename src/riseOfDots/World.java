package riseOfDots;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import riseOfDots.entity.Buildable;
import riseOfDots.entity.Entity;
import riseOfDots.entity.Mover;

public class World {
	
	private int size;
	
	private HashMap<Integer, Entity> map = new HashMap<Integer, Entity>();
	private GridLocation[][] world;
	private int[][] terrain;
	
	public int[][] walkable;
	public int[][] navigable;
	Mover walker;
	Mover navigator;
	
	/** All the things that are selected. */
	public Set<Entity> selected = new HashSet<Entity>();
	
	public World(int size, int[][] terrain){
		this(size);
		this.terrain = terrain;
		createNavigationMaps();
	}
	
	public World(int size){
		this.size = size;
		world = new GridLocation[size][size];
		walkable = new int[size][size];
		navigable = new int[size][size];
	}
	
	/**
	 * Set terrain
	 */
	public void setTerrain(int[][] terrain){
		this.terrain = terrain;
		createNavigationMaps();
	}
	
	/**
	 * Make an entity selected.
	 */
	public void select(Entity e) {
		e.setSelected(true);
		selected.add(e);
	}

	/**
	 * De-select an entity.
	 */
	public void deSelect(Entity e) {
		e.setSelected(false);
		selected.remove(e);
	}
	
	public void add(Entity e){
		add(e, (int) e.x, (int) e.y);
	}
	
	public synchronized void add(Entity e, int x, int y){
		int entityId = newEntityId();
		map.put(entityId, e);
		for(int cx = x; cx < e.width + x; cx++)
			for(int cy = y; cy < e.height + y; cy++){
				if(world[cx][cy] == null) world[cx][cy] = new GridLocation();
				world[cx][cy].add(entityId);
			}
		e.id = entityId;
		e.x = x;
		e.y = y;
		
		if(!canMove(walker, x, y)) walkable[x][y] = 0;
		if(!canMove(navigator, x, y)) navigable[x][y] = 0;
		
		System.out.println("Entity "+e.id+" added at "+x+" "+y);
	}
	
	public synchronized void move(Entity e, int x, int y){
		int id = e.id;
		int w = e.width;
		int h = e.height;
		
		for(int cx = 0; cx < w; cx++)
			for(int cy = 0; cy < h; cy++){
				int cxex = cx + (int) e.x;
				int cyey = cy + (int) e.y;
				int cxx = cx + x;
				int cyy = cy + y;
				world[cxex][cyey].remove(id);
				if(world[cxex][cyey].isEmpty())
					world[cxex][cyey] = null;
				if(world[cxx][cyy] == null)
					world[cxx][cyy] = new GridLocation();
				world[cxx][cyy].add(id);
				
				if(canMove(walker, cxex, cyey)) walkable[cxex][cyey] = 1;
				if(canMove(navigator, cxex, cyey)) navigable[cxex][cyey] = 1;
				if(!canMove(walker, cxx, cyy)) walkable[cxx][cyy] = 0;
				if(!canMove(navigator, cxx, cyy)) navigable[cxx][cyy] = 0;
			}
		
	}
	
	public synchronized void remove(Entity e){
		
		int x = (int) e.x;
		int y = (int) e.y;
		int w = e.width;
		int h = e.height;
		
		for(int cx = 0; cx < w; cx++)
			for(int cy = 0; cy < h; cy++){
				world[cx + x][cy + y].remove(e.id);
				if(world[cx + x][cy + y].isEmpty())
					world[cx + x][cy + y] = null;
			}
		
		map.remove(e.id);
		
		if(canMove(walker, x, y)) walkable[x][y] = 1;
		if(canMove(navigator, x, y)) navigable[x][y] = 1;
	}
	
	/**
	 * Get the array of all entities which overlap with the specified world coordinate.
	 */
	public Entity[] at(int x, int y){
		if(world[x][y] == null) return null;
		int[] id = world[x][y].entities();
		Entity[] e = new Entity[id.length];
		for(int i = 0; i < e.length; i++)
			e[i] = map.get(id[i]);
		return e;
	}
	
	public int terrainAt(int x, int y){
		if(x < 0 | x >= size() | y < 0 | y >= size()) return 0;
		return terrain[x][y];
	}
	
	public boolean canMove(Mover m, int x, int y){
		if(m == null) return true;
		if(world[x][y] != null){
			int[] id = world[x][y].entities();
			
			for(int i = 0; i < id.length; i++)
				if(map.get(id[i]).solid) return false;
		}
		
		int terrain = this.terrain[x][y];
		if(terrain == Static.TERRAIN_SHORE){
			if(x-1 >= 0)
				if(this.terrain[x-1][y] == Static.TERRAIN_WATER) return false;
			if(y-1 >= 0)
				if(this.terrain[x][y-1] == Static.TERRAIN_WATER) return false;
			if(x+1 < size)
				if(this.terrain[x+1][y] == Static.TERRAIN_WATER) return false;
			if(y+1 < size)
				if(this.terrain[x][y+1] == Static.TERRAIN_WATER) return false;
		}
		else if(terrain == Static.TERRAIN_WATER){
			if(x-1 >= 0)
				if(this.terrain[x-1][y] == Static.TERRAIN_SHORE) return false;
			if(y-1 >= 0)
				if(this.terrain[x][y-1] == Static.TERRAIN_SHORE) return false;
			if(x+1 < size)
				if(this.terrain[x+1][y] == Static.TERRAIN_SHORE) return false;
			if(y+1 < size)
				if(this.terrain[x][y+1] == Static.TERRAIN_SHORE) return false;
		}
		
		if(this.terrain[x][y] == Static.TERRAIN_WATER & !m.canNavigate())
			return false;
		if(this.terrain[x][y] != Static.TERRAIN_WATER & !m.canWalk())
			return false;
		
		return true;
		
	}
	
	public boolean canBuild(Buildable b, int x, int y, int w, int h){
		return true;
	}
	
	public synchronized Iterator<Entity> entities(){
		return map.values().iterator();
	}
	
	public synchronized void updateEntities(){
		try{
			Iterator<Entity> i = map.values().iterator();
			while(i.hasNext())
				i.next().update();
		}
		catch(NullPointerException e){
			
		}
	}
	
	public int size(){
		return size;
	}
	
	private int newEntityId(){
		boolean found = false;
		int id = 0;
		while(!found){
			id = (int) (Math.random() * 99999999);
			found = !map.containsKey(id);
		}
		return id;
	}

	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
		if(terrain[tx][ty] == Static.TERRAIN_SHORE) return 3;
		if(terrain[tx][ty] == Static.TERRAIN_WATER) return 2;
		if(terrain[tx][ty] == Static.TERRAIN_EARTH) return 2;
		return 2;
	}
	
	private void createNavigationMaps(){
		walker = new Mover(){
			@Override
			public boolean canWalk() {return true;}
			@Override
			public boolean canNavigate() {return false;}
		};
		navigator = new Mover(){
			@Override
			public boolean canWalk() {return false;}
			@Override
			public boolean canNavigate() {return true;}
		};
		for(int x = 0; x < size; x++)
			for(int y = 0; y < size; y++){
				if(canMove(walker, x, y))
					walkable[x][y] = 1;
				else
					walkable[x][y] = 0;
				if(canMove(navigator, x, y))
					navigable[x][y] = 1;
				else
					navigable[x][y] = 0;
			}
	}
	
}

package riseOfDots;

import java.util.Vector;

public class GridLocation {
	
	Vector<Integer> entities = new Vector<Integer>();
	
	public GridLocation(){
		
	}
	
	public synchronized void add(int e){
		entities.add(e);
	}
	
	public synchronized void remove(int e){
		entities.removeElement(e);
	}
	
	public synchronized int[] entities(){
		int[] e = new int[entities.size()];
		for(int i = 0; i < e.length; i++)
			e[i] = entities.get(i);
		return e;
	}
	
	public boolean isEmpty(){
		return entities.size() == 0;
	}
	
}

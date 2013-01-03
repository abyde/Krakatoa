package riseOfDots.pathFinding;

import java.util.ArrayList;
import java.util.Collections;

public class BinaryHeap <X extends Comparable<X>>{
	
	private ArrayList<X> list = new ArrayList<X>();
	
	public BinaryHeap(){
		
	}
	
	public void add(X x){
		list.add(x);
		int pos = list.size();
		while(true){
			int parentPos = (pos / 2) - 1;
			if(parentPos < 0) return;
			X parent = list.get(parentPos);
			if(x.compareTo(parent) < 0){
				
				Collections.swap(list, parentPos, pos - 1);
				pos = parentPos + 1;
			}
			else
				break;
		}
	}
	
	private void remove(){
		Collections.swap(list, 0, list.size() - 1);
		list.remove(list.size() - 1);
		int pos = 1;
		while(true){
			if(list.size() < pos * 2 + 1) break;
			X p = list.get(pos - 1);
			X c1 = list.get(pos * 2 - 1);
			X c2 = list.get(pos * 2);
			int lowest = c1.compareTo(c2) < 0 ? pos * 2 - 1 : pos * 2;
			if(p.compareTo(list.get(lowest)) > 0){
				Collections.swap(list, pos - 1, lowest);
				pos = lowest + 1;
			}
			else
				break;
		}
	}
	
	public X peekMin() {
		return list.get(0);
	}
	
	public X getAndRemove(){
		X x = peekMin();
		remove();
		return x;
	}
	
	public boolean contains(X x){
		return list.contains(x);
	}
	
}

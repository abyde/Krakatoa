package riseOfDots.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Entity {
	
	public int id;
	public double x, y;
	public int width, height;
	public boolean solid = false;
	public int drawPriority;
	public boolean selectable = false;
	protected boolean isSelected = false;
	
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
		width = 1;
		height = 1;
	}
	
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public abstract void update();
	public abstract void draw(Graphics2D g);
	public void rightMouseClick(int gx, int gy){}
	
	public boolean visible(Rectangle e){
		Point corner1 = new Point((int) x - 1, (int) y - 1);
		Point corner2 = new Point((int) x - 1, (int) y + 1 + height);
		Point corner3 = new Point((int) x + 1 + width, (int) y - 1);
		Point corner4 = new Point((int) x + 1 + width, (int) y + 1 + height);
		if(e.contains(corner1)) return true;
		if(e.contains(corner2)) return true;
		if(e.contains(corner3)) return true;
		if(e.contains(corner4)) return true;
		return false;
	}
	
}

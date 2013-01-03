package riseOfDots.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import riseOfDots.Static;
import riseOfDots.gfx.Textures;

public class Tree extends Entity {
	
	public Tree(int x, int y){
		super(x, y);
		solid = true;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
//		Rectangle viewport = new Rectangle(Static.viewX, Static.viewY, 30, 30);
//		if(!viewport.contains((int) x, (int) y)) return;
//		if(!viewport.contains((int) x + width, (int) y)) return;
//		if(!viewport.contains((int) x, (int) y + height)) return;
//		if(!viewport.contains((int) x + width, (int) y + height)) return;
//		g.fillRect((int) ((x - Static.viewX) * size), (int) ((y - Static.viewY)
//						* size), size, size);
		Point v = Static.viewport.getViewLocation();
		int vWidth = Static.viewport.getViewWidth();
		if(!visible(new Rectangle(v.x, v.y, vWidth, vWidth))) return;
		int size = (int) (600 / vWidth);
		Point p = Static.viewport.getDrawPoint(x, y);
		g.drawImage(Textures.textures[8][0], p.x, p.y, size, size, null);
	}
	
	
	
}

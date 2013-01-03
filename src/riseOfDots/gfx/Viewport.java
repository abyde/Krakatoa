package riseOfDots.gfx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import riseOfDots.Static;
import riseOfDots.World;
import riseOfDots.entity.Entity;
import riseOfDots.event.MouseHandler;

public class Viewport {
	
	private BufferedImage[][] texture;

	private int viewX = 0;
	private int viewY = 0;
	private int tilesPerScreen = 20;
	private int[] zoom = {10, 12, 15, 20, 24, 30, 40, 50};
	private int cZoom = 3;
	
	public Viewport(int x, int y){
		viewX = x;
		viewY = y;
		updateView();
	}
	
	public void updateView(){
		World w = Static.world;
		if(w == null) return;
		texture = new BufferedImage[tilesPerScreen][tilesPerScreen];
		for(int cx = 0; cx < tilesPerScreen; cx++)
			for(int cy = 0; cy < tilesPerScreen; cy++){
				texture[cx][cy] = Textures.selectTextures(w, cx + viewX, cy + viewY);
			}
	}
	
	public void setViewWidth(int w){
		tilesPerScreen = w;
		updateView();
	}
	
	public int getViewWidth(){
		return tilesPerScreen;
	}
	
	public void setViewLocation(int x, int y){
		viewX = x;
		viewY = y;
		updateView();
	}
	
	public Point getViewLocation(){
		return new Point(viewX, viewY);
	}
	
	public void drawView(Graphics2D g){
        int tileSize = 600 / tilesPerScreen;
        
        if(texture == null) updateView();
        if(texture == null) return;
        
        for(int x = 0; x < texture.length; x++)
        	for(int y = 0; y < texture.length; y++){
        		if(x >= texture.length | y >= texture.length) continue;
        		g.drawImage(texture[x][y], x * tileSize, y * tileSize,
        				tileSize, tileSize, null);
        	}
        
        int lastFilled = (texture.length - 1) * tileSize + tileSize;
        if(lastFilled < 600){
        	g.setColor(Color.BLACK);
        	g.fillRect(lastFilled, 0, 600 - lastFilled, 600);
        }
        MouseHandler m = Static.mouse;
        if(m.pressed){
        	int fx = (m.sgx < m.fgx) ? m.sgx : m.fgx;
    		int fy = (m.sgy < m.fgy) ? m.sgy : m.fgy;
    		int w = Math.abs(m.sgx - m.fgx);
    		int h = Math.abs(m.sgy - m.fgy);
        	g.setColor(Color.WHITE);
        	g.drawRect(fx, fy, w, h);
        }
	}
	
	public void zoomIn(){
		if(cZoom - 1 >= 0){
			int oldZoom = tilesPerScreen;
			tilesPerScreen = zoom[--cZoom];
			int newZoom = tilesPerScreen;
			
			int x = viewX + (oldZoom - newZoom)/2;
			int y = viewY + (oldZoom - newZoom)/2;
			setViewLocation(x, y);
		}
	}
	
	public void zoomOut(){
		if(cZoom + 1 < zoom.length){
			int oldZoom = tilesPerScreen;
			tilesPerScreen = zoom[++cZoom];
			int newZoom = tilesPerScreen;
			
			int x = viewX + (oldZoom - newZoom)/2;
			int y = viewY + (oldZoom - newZoom)/2;
			setViewLocation(x, y);
		}
	}
	
	public Point getDrawPoint(double x, double y){
		int size = (int) (600 / tilesPerScreen);
		int px = (int) ((x - viewX) * size);
		int py = (int) ((y - viewY) * size);
		return new Point(px, py);
	}
	
	public void selectUnits(Rectangle r){
		try{
			Iterator<Entity> i = Static.world.entities();
			while(i.hasNext()){
				Entity e = i.next();
				if(e.selectable & r.contains(e.x, e.y))
					Static.world.select(e);
				else
					Static.world.deSelect(e);
			}
		}
		catch(NullPointerException e){
			
		}
	}
	
}

package riseOfDots.gfx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import riseOfDots.Static;

public class UserInterface {
	
	BufferedImage map = null;
	
	public UserInterface(){
		
	}
	
	public void setMap(BufferedImage img){
		map = img;
	}
	
	public void drawUI(Graphics2D g){
    	g.setColor(Color.GRAY);
        g.fill3DRect(600, 0, 200, 600, true);
        g.setColor(Color.BLACK);
        g.drawLine(600, 0, 600, 600);
        g.drawRect(609, 9, 181, 181);
        if(map != null)
        	g.drawImage(map, 610, 10, 180, 180, null);
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(608, 191, 790, 191);
        g.drawLine(608, 10, 608, 190);
        g.setColor(Color.DARK_GRAY);
        g.drawLine(608, 8, 790, 8);
        g.drawLine(791, 8, 791, 190);
        g.drawLine(601, 0, 601, 600);
        g.setColor(Color.WHITE);
        
        Point v = Static.viewport.getViewLocation();
        
        double ration = Static.world.size() / 180;
        
        int w = (int) (Static.viewport.getViewWidth() / ration);
        int x = (int) (v.x / ration);
        int y = (int) (v.y / ration);
        
        if(x > 179 - w) x = 179 - w;
        if(y > 179 - w) y = 179 - w;
        
        g.drawRect(x + 610, y + 10, w, w);
    }
    
	
}

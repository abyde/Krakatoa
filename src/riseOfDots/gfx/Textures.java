package riseOfDots.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import riseOfDots.Static;
import riseOfDots.World;

public class Textures {
	
	public static BufferedImage[][] textures;
	public static BufferedImage[][] waterCorner;
	public static BufferedImage[][] shoreCorner;
	
	public static void loadTextures() throws IOException {
		BufferedImage texts = ImageIO.read(new File("textures/texture.png"));
		int w = texts.getWidth() / 16;
		int h = texts.getHeight() / 16;
		textures = new BufferedImage[w][h];
		for(int x = 0; x < w; x++)
			for(int y = 0; y < h; y++)
				textures[x][y] = texts.getSubimage(x*16, y*16, 16, 16);
		
		waterCorner = new BufferedImage[2][2];
		waterCorner[0][0] = textures[2][0].getSubimage(0, 0, 8, 8);
		waterCorner[1][0] = textures[2][0].getSubimage(8, 0, 8, 8);
		waterCorner[0][1] = textures[2][0].getSubimage(0, 8, 8, 8);
		waterCorner[1][1] = textures[2][0].getSubimage(8, 8, 8, 8);
		
		shoreCorner = new BufferedImage[2][2];
		shoreCorner[0][0] = textures[1][0].getSubimage(0, 0, 8, 8);
		shoreCorner[1][0] = textures[1][0].getSubimage(8, 0, 8, 8);
		shoreCorner[0][1] = textures[1][0].getSubimage(0, 8, 8, 8);
		shoreCorner[1][1] = textures[1][0].getSubimage(8, 8, 8, 8);
	}
	
	public static BufferedImage selectTextures(World w, int x, int y){
		int ter = w.terrainAt(x, y);
		int up = w.terrainAt(x, y - 1);
		int down = w.terrainAt(x, y + 1);
		int left = w.terrainAt(x - 1, y);
		int right = w.terrainAt(x + 1, y);
		
		int water = Static.TERRAIN_WATER;
		int shore = Static.TERRAIN_SHORE;
		int earth = Static.TERRAIN_EARTH;
		
		
		if(ter == earth){
			if(up == ter & down == ter | left == ter & right == ter)
				return textures[0][0];
			
			if(up == shore & left == shore & right == earth & down == earth)
				return textures[2][1];
			if(up == shore & left == earth & right == shore & down == earth)
				return textures[3][1];
			if(up == earth & left == shore & right == earth & down == shore)
				return textures[2][2];
			if(up == earth & left == earth & right == shore & down == shore)
				return textures[3][2];
			
			if(up == earth & left == shore & right == shore & down == shore)
				return textures[6][3];
			if(up == shore & left == earth & right == shore & down == shore)
				return textures[6][1];
			if(up == shore & left == shore & right == earth & down == shore)
				return textures[6][2];
			if(up == shore & left == shore & right == shore & down == earth)
				return textures[6][4];
			
			if(up == shore & left == up & down == up & right == up)
				return textures[8][2];
		}
		else if(ter == shore){
			if(up == ter & down == ter | left == ter & right == ter)
				return textures[1][0];
			
			if(up == shore & left == shore & right == water & down == water)
				return textures[1][2];
			if(up == shore & left == water & right == shore & down == water)
				return textures[0][2];
			if(up == water & left == shore & right == water & down == shore)
				return textures[1][1];
			if(up == water & left == water & right == shore & down == shore)
				return textures[0][1];
			
			if(up == shore & left == shore & right == earth & down == earth)
				return textures[3][4];
			if(up == shore & left == earth & right == shore & down == earth)
				return textures[2][4];
			if(up == earth & left == shore & right == earth & down == shore)
				return textures[3][3];
			if(up == earth & left == earth & right == shore & down == shore)
				return textures[2][3];
			
			if(up == shore & left == earth & right == earth & down == earth)
				return textures[5][3];
			if(up == earth & left == shore & right == earth & down == earth)
				return textures[5][1];
			if(up == earth & left == earth & right == shore & down == earth)
				return textures[5][2];
			if(up == earth & left == earth & right == earth & down == shore)
				return textures[5][4];
			
			if(up == shore & left == water & right == water & down == water)
				return textures[7][3];
			if(up == water & left == shore & right == water & down == water)
				return textures[7][1];
			if(up == water & left == water & right == shore & down == water)
				return textures[7][2];
			if(up == water & left == water & right == water & down == shore)
				return textures[7][4];
			
			if(up == earth & left == up & down == up & right == up)
				return textures[8][1];
			if(up == water & left == up & down == up & right == up)
				return textures[8][4];
		}
		else if(ter == water){
			if(up == ter & down == ter | left == ter & right == ter)
				return textures[2][0];
			
			if(up == shore & left == shore & right == water & down == water)
				return checkCorners(textures[0][3], w, x, y);
			if(up == shore & left == water & right == shore & down == water)
				return checkCorners(textures[1][3], w, x, y);
			if(up == water & left == shore & right == water & down == shore)
				return checkCorners(textures[0][4], w, x, y);
			if(up == water & left == water & right == shore & down == shore)
				return checkCorners(textures[1][4], w, x, y);
			
			if(up == water & left == shore & right == shore & down == shore)
				return checkCorners(textures[4][3], w, x, y);
			if(up == shore & left == water & right == shore & down == shore)
				return checkCorners(textures[4][1], w, x, y);
			if(up == shore & left == shore & right == water & down == shore)
				return checkCorners(textures[4][2], w, x, y);
			if(up == shore & left == shore & right == shore & down == water)
				return checkCorners(textures[4][4], w, x, y);
			
			if(up == shore & left == up & down == up & right == up)
				return checkCorners(textures[8][3], w, x, y);
		}
		if(ter == earth) return textures[0][0];
		else if(ter == shore) return textures[1][0];
		else return textures[2][0];
	}
	
	private static BufferedImage checkCorners(BufferedImage c, World w, int x, int y){
		if(true) return c;
		@SuppressWarnings("unused")
		int ter = w.terrainAt(x, y);
		int upleft = w.terrainAt(x - 1, y - 1);
		int upright = w.terrainAt(x + 1, y - 1);
		int downleft = w.terrainAt(x - 1, y + 1);
		int downright = w.terrainAt(x + 1, y + 1);
		
		int water = Static.TERRAIN_WATER;
		int shore = Static.TERRAIN_SHORE;

		Graphics2D g = c.createGraphics();
		
		if(ter == water){
			if(upleft == water)
				g.drawImage(waterCorner[0][0], 0, 0, null);
			if(upright == water)
				g.drawImage(waterCorner[1][0], 8, 0, null);
			if(downleft == water)
				g.drawImage(waterCorner[0][1], 0, 8, null);
			if(downright == water)
				g.drawImage(waterCorner[1][1], 8, 8, null);
			return c;
		}
		
		else if(ter == shore){
			if(upleft == shore)
				g.drawImage(shoreCorner[0][0], 0, 0, null);
			if(upright == shore)
				g.drawImage(shoreCorner[1][0], 8, 0, null);
			if(downleft == shore)
				g.drawImage(shoreCorner[0][1], 0, 8, null);
			if(downright == shore)
				g.drawImage(shoreCorner[1][1], 8, 8, null);
			return c;
		}
		
		else
			return c;
	}
	
}

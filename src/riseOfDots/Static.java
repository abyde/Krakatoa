package riseOfDots;

import riseOfDots.event.KeyHandler;
import riseOfDots.event.MouseHandler;
import riseOfDots.gfx.UserInterface;
import riseOfDots.gfx.Viewport;

public class Static {
	
	public static final int TERRAIN_WATER = 1;
	public static final int TERRAIN_SHORE = 2;
	public static final int TERRAIN_EARTH = 3;
	
	public static final double UNIT_BASESPEED = 0.03;
	
	public static RiseOfDots game = null;
	public static World world = null;
	public static KeyHandler keyboard = null;
	public static MouseHandler mouse = null;
	public static Viewport viewport = null;
	public static UserInterface ui = null;
	public static long mapSeed = 0L;
	
	public static boolean drawStats = true;
	
	
}

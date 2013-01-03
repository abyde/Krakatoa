package riseOfDots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import riseOfDots.entity.Entity;
import riseOfDots.entity.Tree;
import riseOfDots.entity.Unit;
import riseOfDots.event.KeyHandler;
import riseOfDots.event.MouseHandler;
import riseOfDots.gfx.Textures;
import riseOfDots.gfx.UserInterface;
import riseOfDots.gfx.Viewport;
import riseOfDots.mapGenerator.HeightMapGenerator;

/**
 *
 * @author Lucas Kuhlemann Duleba da Luz
 */
public class RiseOfDots extends JFrame implements Runnable {
	
	public static final long serialVersionUID = 0L;
    
    public static final int NUM_BUFFERS = 2;
    
    private static final int UPS = 100;
    
    private static final int NO_DELAYS_PER_YIELD = 16;
    
    private static final int MAX_FRAME_SKIPS = 5;
    
    @SuppressWarnings("unused")
	private int pWidth;
    @SuppressWarnings("unused")
	private int pHeight;
    
    private int updates = 0;
    private int frames = 0;
    private long start = 0L;
    private int averageFPS = 0;
    private int averageUPS = 0;
    
    private Thread animator;
    
    private volatile boolean isPaused = false;
    private volatile boolean running = false;
    private volatile boolean gameOver = false;
    private volatile boolean loading = true;
    
    private GraphicsDevice gd;
    private Graphics gSrc;
    private BufferStrategy bufferStrategy;
    
    BufferedImage map = null;
    
    public static void main(String[] args){
    	
        new RiseOfDots(new Dimension(800, 600));
        
    }
    
    public RiseOfDots(Dimension dim) {
        
        initFullScreen();
        
        setFocusable(true);
        requestFocus();
        
        if(dim != null)
            setDisplayMode(dim.width, dim.height, 32);
        
        pWidth = dim.width;
        pHeight = dim.height;
        
        KeyHandler k = new KeyHandler();
        addKeyListener(k);
        Static.keyboard = k;
        
        MouseHandler m = new MouseHandler();
        addMouseListener(m);
        addMouseMotionListener(m);
        Static.mouse = m;
        
        Static.viewport = new Viewport(100, 100);
        
        Static.ui = new UserInterface();
        
        startGame();
    }
    
    @Override
    public void run(){
        long period = (1000 / UPS) * 1000000L;
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        long excess = 0L;
        int noDelays = 0;
        
        beforeTime = System.nanoTime();
        start = System.nanoTime();
        
        running = true;
        while(running){
            updateGame();
            screenUpdate();
            
            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = period - timeDiff - overSleepTime;
            
            if(sleepTime > 0){
                try{
                    Thread.sleep(sleepTime / 1000000L);
                }
                catch(Exception e){}
                overSleepTime = System.nanoTime() - afterTime - sleepTime;
            }
            else{
                excess -= sleepTime;
                overSleepTime = 0L;
                if(++noDelays >= NO_DELAYS_PER_YIELD){
                    Thread.yield();
                    noDelays = 0;
                }
            }
            beforeTime = System.nanoTime();
            
            int skips = 0;
            while((excess > period) && (skips < MAX_FRAME_SKIPS)){
                excess -= period;
                updateGame();
                skips++;
            }
            
            if(System.nanoTime() - start >= 1000000000){
                averageFPS = frames;
                averageUPS = updates;
                frames = 0;
                updates = 0;
                start = System.nanoTime();
            }
        }
        finishOff();
    }
    
    private void screenUpdate(){
        frames++;
        try{
            gSrc = bufferStrategy.getDrawGraphics();
            gameRender(gSrc);
            gSrc.dispose();
            if(!bufferStrategy.contentsLost())
                bufferStrategy.show();
            else
                System.err.println("Graphic contents lost.");
            Toolkit.getDefaultToolkit().sync();
        }
        catch(Exception e){
            e.printStackTrace();
            running = false;
        }
    }
    
    @SuppressWarnings("unused")
	private int[][] loadTerrain(){
    	int[][] terrain = null;
    	try{
    		map = ImageIO.read(new File("map.png"));
    		terrain = new int[map.getWidth()][map.getHeight()];
    		for(int x = 0; x < map.getWidth(); x++)
    			for(int y = 0; y < map.getHeight(); y++){
    				int rgb = map.getRGB(x, y);
    				//if(rgb == -16776961)
    				if(rgb == -128)
    					terrain[x][y] = Static.TERRAIN_SHORE;
    				else if(rgb == -16744448)
    					terrain[x][y] = Static.TERRAIN_EARTH;
    				else
    					terrain[x][y] = Static.TERRAIN_WATER;
    			}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
		return terrain;
    }

    public void startGame(){
        
    	new Thread(new Runnable(){
    		public void run(){
    			loadGame();
    		}
    	}).start();
    	
    	
        if(animator == null || !running){
            animator = new Thread(this);
            animator.start();
        }
        
    }
    
    private void loadGame(){
    	
    	loading = true;
    	
        Static.game = this;
        
        //int[][] terrain = loadTerrain();
        double v = 1;
        double sea = v/2;
        double shore = sea + 0.01;
        double forest = shore + 0.2;
        int size = 1000;
        //long seed = new Random().nextLong();
        long seed = 271861763434L;
//        long seed = 567L;
        Static.mapSeed = seed;
        Static.world = new World(size);
        int[][] terrain = generateTerrain(size, sea, shore, forest, v, seed);
        Static.world.setTerrain(terrain);
        //Static.world = new World(map.getWidth(), terrain);
		Static.world.add(new Unit(105, 105, null));
		Static.world.add(new Unit(105, 106, null));
		Static.world.add(new Unit(106, 105, null));
		Static.world.add(new Unit(106, 106, null));
        //ResourceGenerator.genResources(Static.world, seed);
        
		try{
			Textures.loadTextures();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		Static.viewport.updateView();
        
        loading = false;
    }
    
    private int[][] generateTerrain(int s, double sea, double shore, double forest, double v, long sd){
    	int[][] terrain = new int[s][s];
    	HeightMapGenerator g = new HeightMapGenerator();
    	g.setSize(s, s);
    	g.setVariance(v);
    	g.setSeed(sd);
    	Random r = new Random();
    	r.setSeed(sd);
    	double[][] heightMap = g.generate();
    	map = new BufferedImage(s, s, BufferedImage.TYPE_INT_ARGB);
    	for(int x = 0; x < s; x++)
    		for(int y = 0; y < s; y++){
    			if(heightMap[x][y] <= sea){
    				terrain[x][y] = Static.TERRAIN_WATER;
    				map.setRGB(x, y, -16776961);
    			}
    			else if(heightMap[x][y] <= shore){
    				terrain[x][y] = Static.TERRAIN_SHORE;
    				map.setRGB(x, y, -128);
    			}
    			else if(heightMap[x][y] > shore){
    				terrain[x][y] = Static.TERRAIN_EARTH;
    				map.setRGB(x, y, -16744448);
    			}
    			if(heightMap[x][y] > forest){
    				if(r.nextDouble() > 0.95) Static.world.add(new Tree(x, y));
    			}
    		}
    	Static.ui.setMap(map);
    	return terrain;
    }
    
    private void updateGame(){
        if(!isPaused && !gameOver){
            updates++;
            
            if(Static.world == null) return;
            Static.world.updateEntities();
        }
    }
    
    private void gameRender(Graphics g3d){

        Graphics2D g = (Graphics2D) g3d;
        
        if(loading){
        	g.setColor(Color.BLACK);
        	g.fillRect(0, 0, 800, 600);
        	g.setColor(Color.WHITE);
        	g.setFont(new Font("Comic Sans MS", Font.BOLD, 82));
        	g.drawString("LOADING", 200, 310);
        }
        else
        	drawGame(g);
    	
    }
    
    private void drawGame(Graphics2D g){

        if(Static.world == null) return;
        
        updateViewPos();
        
        Static.viewport.drawView(g);
        drawEntities(g);
        Static.ui.drawUI(g);
        if(Static.drawStats)
        	drawStats(g);
        
    }
    
    private void drawEntities(Graphics2D g){
    	try{
    		Iterator<Entity> i = Static.world.entities();
    		while(i.hasNext())
    			i.next().draw(g);
    	}
    	catch(NullPointerException e){
    		e.printStackTrace();
    	}
    }
    
    private void drawStats(Graphics2D g){
    	g.setColor(new Color(0, 0, 0));
        g.drawString("FPS: "+averageFPS+" UPS: "+averageUPS, 10, 15);
        g.drawString("Requested FPS/UPS: "+UPS, 10, 30);
        int fpsAcc = (averageFPS / (UPS / 100));
        fpsAcc = 100 - fpsAcc;
        int upsAcc = (averageUPS / (UPS / 100));
        upsAcc = 100 - upsAcc;
        g.drawString("FPS error: "+fpsAcc+"%", 10, 45);
        g.drawString("UPS error: "+upsAcc+"%", 10, 60);
        
        
        String at = "";
        int x = Static.mouse.gx;
    	int y = Static.mouse.gy;
    	if(Static.mouse.x < 600){
        	int terrain = Static.world.terrainAt(x, y);
        	if(terrain == Static.TERRAIN_WATER)
        		at = "Terrain: Water";
        	else if(terrain == Static.TERRAIN_SHORE)
        		at = "Terrain: Shore";
        	else if(terrain == Static.TERRAIN_EARTH)
        		at = "Terrain: Earth";
    	}
    	else
    		at = "User interface";
        g.drawString(at, 10, 75);
        Point v = Static.viewport.getViewLocation();
        g.drawString("View: X = "+v.x+" Y = "+v.y, 10, 90);
        g.drawString("Mouse: X = "+Static.mouse.gx+" Y = "+Static.mouse.gy, 10, 105);
        g.drawString("Seed: "+Static.mapSeed, 10, 120);
        g.drawString("Zoom: "+Static.viewport.getViewWidth(), 10, 135);
    }
    

    private void updateViewPos(){
    	int maxCoord = Static.world.size() - Static.viewport.getViewWidth() - 2;
        
        Point view = Static.viewport.getViewLocation();
        if(Static.keyboard.upPressed & view.y > 0)
        	Static.viewport.setViewLocation(view.x, view.y - 1);
        if(Static.keyboard.rightPressed & view.x < maxCoord)
        	Static.viewport.setViewLocation(view.x + 1, view.y);
        if(Static.keyboard.downPressed & view.y < maxCoord)
        	Static.viewport.setViewLocation(view.x, view.y + 1);
        if(Static.keyboard.leftPressed & view.x > 0)
        	Static.viewport.setViewLocation(view.x - 1, view.y);
        Static.mouse.updateGridCoords();
    }
    
    
    public void pauseGame(){
        isPaused = true;
    }
    
    public void resumeGame(){
        isPaused = false;
    }
    
    public void endGame(){
    	running = false;
    }
    
    private void initFullScreen(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        setUndecorated(true);
        setIgnoreRepaint(true);
        setResizable(false);
        if(!gd.isFullScreenSupported()){
            System.err.println("Fullscreen mode not supported.");
            System.exit(0);
        }
        gd.setFullScreenWindow(this);
        
        pWidth = getBounds().width;
        pHeight = getBounds().height;
        
        setBufferStrategy();
    }
    
    private void setBufferStrategy(){
        try{
            EventQueue.invokeAndWait(new Runnable(){
                @Override
                public void run(){
                    createBufferStrategy(NUM_BUFFERS);
                }
            });
        }
        catch(Exception e){
            System.err.println("Error while creating buffer strategy.");
            System.exit(0);
        }
        
        try{
            Thread.sleep(500);
        }
        catch(Exception e){}
        
        bufferStrategy = getBufferStrategy();
    }
    
    private void finishOff(){
        restoreScreen();
        System.exit(0);
    }
    
    private void restoreScreen(){
        Window w = gd.getFullScreenWindow();
        if(w != null)
            w.dispose();
        gd.setFullScreenWindow(null);
    }
    
    private void setDisplayMode(int width, int height, int bitDepth){
        if(!gd.isDisplayChangeSupported()){
            System.err.println("Display mode changing not supported");
            return;
        }
        
        if(!isDisplayModeAvailable(width, height, bitDepth)){
            System.out.println("Display mode (" + width + "," +
                        height + "," + bitDepth + ") not available");
            return;
        }
        
        DisplayMode diplayMode = new DisplayMode(width, height, bitDepth, DisplayMode.REFRESH_RATE_UNKNOWN);
        try{
            gd.setDisplayMode(diplayMode);
            pWidth = width;
            pHeight = height;
            Thread.sleep(1000);
        }
        catch(Exception e){
            System.err.println("Couldn't set display mode.");
        }
    }
    
    private boolean isDisplayModeAvailable(int width, int height, int bitDepth){
        DisplayMode[] modes = gd.getDisplayModes();
        for(int i = 0; i < modes.length; i++) {
            if ( width == modes[i].getWidth( ) &&
                 height == modes[i].getHeight( ) &&
                 bitDepth == modes[i].getBitDepth( ) )
                 return true;
            }
        return false;
    }
    
}

package missile;
/*
 * Bouncer.java
 *
 * Created on November 9, 2012, 2:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author acer
 */
public class Bouncer implements KeyListener, MouseListener, MouseMotionListener {
    
    public static final int wScreen = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int hScreen = Toolkit.getDefaultToolkit().getScreenSize().height;
    
    Color ballColour = new Color(r(), r(), r());
    
    static double diversion = 2;
    
    static int bombParticles = 1000;
    static double explosionDiversion = 10;
    static double rocketDiversion = 0.5;
    static double bombSpeed = 5;
    static double gravity = 0;
    
    static int ballRadius = 42;
    static int flameRadius = 2;
    static int bombRadius = 5;
    
    double x = 400, y = 300;
    double xspeed = 0, yspeed = 0;
    double xacc = 0, yacc = 0;
    double elasticity = 0.9;
    double speedDivider = 0.5;
    double friction = 0.99;
    double acc = 0.01;
    double particleSpeed = 10;
    
    static Vector<Flame> flames = new Vector<Flame>();
    static Vector<Bomb> bombs = new Vector<Bomb>();
    
    public static void main(String[] args){
        new Bouncer();
    }
    
    /** Creates a new instance of Bouncer */
    public Bouncer() {
        JFrame frame = new JFrame("Bouncer");
        JPanel panel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                updatePos();
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, 2000, 2000);
                
                for(int i = 0; i < flames.size(); i++){
                    Flame c = flames.get(i);
                    c.update();
                    g.setColor(c.color);
                    g.fillArc((int) c.x - flameRadius, (int) c.y - flameRadius, flameRadius*2, flameRadius*2, 0, 360);
                }
                
                g.setColor(Color.GRAY);
                int rectsize = 10;
                g.fillRect((int) x + ballRadius - rectsize/2, (int) y - rectsize/2, rectsize, rectsize);
                g.fillRect((int) x + ballRadius - rectsize/2, (int) y - rectsize/2 + ballRadius*2, rectsize, rectsize);
                g.fillRect((int) x - rectsize/2, (int) y - rectsize/2 + ballRadius, rectsize, rectsize);
                g.fillRect((int) x - rectsize/2 + 2*ballRadius, (int) y - rectsize/2 + ballRadius, rectsize, rectsize);
                
                if(ballColour != null) g.setColor(ballColour);
                else g.setColor(Color.RED);
                g.fillArc((int) x, (int) y, ballRadius * 2, ballRadius * 2, 0, 360);
                
                g.setColor(Color.GRAY);
                for(int i = 0; i < bombs.size(); i++){
                    Bomb bomb = bombs.get(i);
                    if(!bomb.visible)
                        continue;
                    bomb.updateLocation();
                    g.fillArc((int) bomb.x - bombRadius, (int) bomb.y - bombRadius, bombRadius * 2, bombRadius * 2, 0, 360);
                }
            }
        };
        frame.setLayout(new GridLayout(1, 1, 0, 0));
        frame.add(panel);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setUndecorated(true);
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        frame.setVisible(true);
        
        while(true){
            try{
                Thread.sleep(20);
                panel.repaint();
                frame.repaint();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void updatePos(){
        xspeed += xacc / speedDivider;
        yspeed += (yacc + gravity) / speedDivider;
        xspeed *= friction;
        yspeed *= friction;
        x += xspeed;
        y += yspeed;
        if(x < 0){
            x = 0;
            xspeed = -xspeed * elasticity;
            ballColour = new Color(r(), r(), r());
        }
        else if(x + ballRadius * 2 > wScreen){
            x = wScreen - ballRadius * 2;
            xspeed = -xspeed * elasticity;
            ballColour = new Color(r(), r(), r());
        }
        if(y < 0){
            y = 0;
            yspeed = -yspeed * elasticity;
            ballColour = new Color(r(), r(), r());
        }
        else if(y + ballRadius * 2 > hScreen){
            y = hScreen - ballRadius * 2;
            yspeed = -yspeed * elasticity;
            ballColour = new Color(r(), r(), r());
        }
        double s = particleSpeed;
        for(int i = 0; i < 5; i++){
            if(yacc == -acc)
                flames.add(new Flame((int) (x + ballRadius), (int) (y + 2*ballRadius), 0, s, gravity));
            if(yacc == acc)
                flames.add(new Flame((int) (x + ballRadius), (int) y, 0, -s, gravity));
            if(xacc == acc)
                flames.add(new Flame((int) x, (int) (y + ballRadius), -s, 0, gravity));
            if(xacc == -acc)
                flames.add(new Flame((int) (x + 2 * ballRadius), (int) (y + ballRadius), s, 0, gravity));
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
        else if(e.getKeyCode() == KeyEvent.VK_UP){
            yacc = -acc;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            yacc = acc;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            xacc = acc;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            xacc = -acc;
        }
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP){
            yacc = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            yacc = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            xacc = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            xacc = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            /*double bxMomentum = xspeed * ballRadius;
            double fMomentum = particleSpeed * flameRadius;
            int xflames = (int) (bxMomentum / fMomentum) * 10;
            if(xspeed > 0){
                for(int i = 0; i < xflames; i++)
                    flames.add(new Flame((int) x, (int) (y + ballRadius), particleSpeed, 0, gravity));
            }
            else{
                for(int i = 0; i < xflames; i++)
                    flames.add(new Flame((int) x + 2*ballRadius, (int) (y + ballRadius), -particleSpeed, 0, gravity));
            }
            
            double byMomentum = yspeed * ballRadius;
            int yflames = (int) (byMomentum / fMomentum) * 10;
            if(yspeed > 0){
                for(int i = 0; i < yflames; i++)
                    flames.add(new Flame((int) (x + ballRadius), (int) y, 0, particleSpeed, gravity));
            }
            else{
                for(int i = 0; i < yflames; i++)
                    flames.add(new Flame((int) (x + ballRadius), (int) (y + 2*ballRadius), 0, -particleSpeed, gravity));
            }*/
            
            xspeed = 0;
            yspeed = 0;
        }
    }
    
    public int r(){
        //return (int) (Math.random() * 255);
        return 255;
    }

    public void mouseClicked(MouseEvent e) {
        
    }

    public void mousePressed(MouseEvent e) {
        Bomb bomb = new Bomb((int) x + ballRadius, (int) y + ballRadius, e.getX(), e.getY());
        bombs.add(bomb);
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		for(Bomb bomb : bombs) {
			bomb.setTarget(arg0.getX(), arg0.getY());
		}
	}
    
}

class Flame{
    
    double x, y;
    Color color;
    int red, green, blue;
    double transparency;
    double xspeed, yspeed, friction = 0.99;
    double gravity;
    double diversionLimit = Bouncer.diversion;
    
    public Flame(int x, int y, double xspeed, double yspeed, double gravity){
        this.x = x;
        this.y = y;
        int rand = (int) (Math.random() * 2);
        switch(rand){
            case -1:
                red = 153;
                green = 217;
                blue = 234;
                break;
            case 0:
                //red = 225;
                //green = 225;
                //blue = 0;
                red = 255;
                green = 100;
                blue = 100;
                break;
            case 1:
                red = 225;
                green = 0;
                blue = 0;
                break;
        }
        color = new Color(red, green, blue);
        this.xspeed = xspeed + (Math.random() * diversionLimit) - (diversionLimit / 2);
        this.yspeed = yspeed + (Math.random() * diversionLimit) - (diversionLimit / 2);
        this.gravity = gravity;
        transparency = 100;
    }
    
    public void update(){
        if(transparency > 100){
            transparency = 100;
            color = new Color(0, 0, 0);
            return;
        }
        if(transparency == 0){
            Bouncer.flames.remove(this);
            return;
        }
        xspeed *= friction;
        yspeed -= gravity/10;
        yspeed *= friction;
        x += xspeed;
        y += yspeed;
        transparency -= 2;
        
        int newRed = (int) (red * (transparency / 100));
        int newGreen = (int) (green * (transparency / 100));
        int newBlue = (int) (blue * (transparency / 100));
        color = new Color(newRed, newGreen, newBlue);
    }
    
    public void setDiversion(double diversion){
        diversionLimit = diversion;
        
        double rand = Math.random() * diversion;// - diversion/2;
        double hip = diversion - rand;
        double angle = Math.random() * 360;
        double xs = Math.sin(angle)*hip;
        double ys = Math.cos(angle)*hip;
        
        this.xspeed = xs;
        this.yspeed = ys;
        
        //this.xspeed = xspeed + (Math.random() * diversionLimit) - (diversionLimit / 2);
        //this.yspeed = yspeed + (Math.random() * diversionLimit) - (diversionLimit / 2);
    }
    
}

class Bomb{
    
	double dt = 0.5;
	double acceleration = 1;
    double x, y;
    double targetX, targetY;
    double xspeed, yspeed;
    boolean visible = true;
    
    public Bomb(int x, int y, int igx, int igy){
        this.x = x;
        this.y = y;
        this.targetX = igx;
        this.targetY = igy;
    }

    public void setTarget(double x, double y) {
    	targetX = x;
    	targetY = y;
    }
    
    public void updateLocation(){

    	/*
        if(x - targetX < 0) xspeed = Bouncer.bombSpeed;
        else if(x - targetX > 0) xspeed = -Bouncer.bombSpeed;
        else xspeed = 0;
        
        if(y - targetY < 0) yspeed = Bouncer.bombSpeed;
        else if(y - targetY > 0) yspeed = -Bouncer.bombSpeed;
        else yspeed = 0;
*/
    	double ax, ay;
    	ax = (targetX - x);
    	ay = (targetY - y);
    	double l = Math.sqrt(ax*ax + ay*ay);
    	if (l == 0.0)
    		return;
    	ax /= l;
    	ay /= l;
    	xspeed += ax * dt * acceleration;
    	yspeed += ay * dt * acceleration;
    	
        x += xspeed * dt;
        y += yspeed * dt;
        
        Flame particle = new Flame((int) x + Bouncer.bombRadius - Bouncer.flameRadius,
                (int) y + Bouncer.bombRadius - Bouncer.flameRadius * 2,
                -xspeed, - yspeed, Bouncer.gravity);
        particle.setDiversion(Bouncer.rocketDiversion);
        Bouncer.flames.add(particle);
        
        //explode when close enough
        int xdiff = (int) Math.abs(targetX - x);
        int ydiff = (int) Math.abs(targetY - y);
        if(xdiff < 10 & ydiff < 10){
            visible = false;
            for(int i = 0; i < Bouncer.bombParticles; i++){
                Flame flame = new Flame((int) x, (int) y, 0, 0, 0);
                flame.setDiversion(Bouncer.explosionDiversion);
                Bouncer.flames.add(flame);
            }
        }
    }
    
}
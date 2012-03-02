package util;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

/**
 * Class to do double buffering.  I thought it was supposed to be standard in
 * Java 2, but it isn't, so here's a guaranteed double-buffer canvas.
 *
 * @author Andrew Byde
 * @version 1.0
 */
public class BufferCanvas extends Canvas {

	private static final long serialVersionUID = 1L;

	/** The image to which stuff is drawn before it's shown on-screen. */
    private Image	offImage;
    
    /** The graphics context of the off-screen image. */
    private Graphics	offGraphics;
    
    /** Is the class working properly? */
    private boolean	double_buffer = true;
    
    /** The size of the off-screen image. */
    private Dimension   mySize = new Dimension(0,0);

    /**
     * Over-ride the method that is called to update the canvas.  Usually this
     * method just blanks the canvas then calls paint.  Here we do the same for
     * a separate canvas, then draw the separate image onto the current one.
     */
    public void update(Graphics g) {
		try_buffer(getSize());
		if (double_buffer) {
		    blank(offGraphics);
		    paint(offGraphics);
		    g.drawImage(offImage, 0, 0, this);
		} else {
		    blank(g);
		    paint(g);
		}
    }

	/**
	 * Clear the screen.
	 * 
	 * @param g
	 */
    protected void blank(Graphics g) {
        Color c = g.getColor();
		g.setColor( getBackground() );
		g.fillRect(0,0,getSize().width, getSize().height);
        g.setColor(c);
    }

	/**
	 * See if the size of the existing off-screen buffer is the same
	 * as my actual size.  If not, build a new one.  If we can't build
	 * a new one, let the update method know.
	 * 
	 * @param newSize
	 */
    protected void try_buffer( Dimension newSize ) {
		if ( (newSize.width != mySize.width)
			|| (newSize.height != mySize.height) ) {
	
			// We need a new off screen image.
	
		    mySize = newSize;
		    try {
				offImage = createImage(mySize.width, mySize.height);
				offGraphics = offImage.getGraphics();
				double_buffer = true;
		    } catch (Exception e) {
				System.out.println("Can't find the graphics context:");
				System.out.println("Single buffering");
				double_buffer = false;
		    }
        }
    }

}

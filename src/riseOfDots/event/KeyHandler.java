package riseOfDots.event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;

import riseOfDots.Static;

public class KeyHandler implements KeyListener {

	public boolean upPressed = false;
	public boolean rightPressed = false;
	public boolean downPressed = false;
	public boolean leftPressed = false;
	
	@Override
	public void keyPressed(KeyEvent evt) {
		if(evt.getKeyCode() == KeyEvent.VK_ESCAPE)
			Static.game.endGame();
		else if(evt.getKeyCode() == KeyEvent.VK_UP)
			upPressed = true;
		else if(evt.getKeyCode() == KeyEvent.VK_RIGHT)
			rightPressed = true;
		else if(evt.getKeyCode() == KeyEvent.VK_DOWN)
			downPressed = true;
		else if(evt.getKeyCode() == KeyEvent.VK_LEFT)
			leftPressed = true;
		else if(evt.getKeyCode() == KeyEvent.VK_F12){
			try{
				File f = new File("E:/Users/Lucas/Desktop/seeds.txt");
				if(!f.exists()) f.createNewFile();
				BufferedReader br = new BufferedReader(new InputStreamReader
						(new FileInputStream(f)));
				Vector<String> v = new Vector<String>();
				String line = "";
				while((line = br.readLine()) != null)
					v.add(line);
				br.close();
				PrintWriter pw = new PrintWriter(f);
				for(String s : v)
					pw.println(s);
				pw.println(Static.mapSeed);
				pw.flush();
				pw.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		else if(evt.getKeyCode() == KeyEvent.VK_F11){
			Static.game.startGame();
		}
		else if(evt.getKeyCode() == KeyEvent.VK_F10){
			Static.drawStats = !Static.drawStats;
		}
		else if(evt.getKeyCode() == KeyEvent.VK_MINUS){
			Static.viewport.zoomOut();
		}
		else if(evt.getKeyCode() == KeyEvent.VK_EQUALS){
			Static.viewport.zoomIn();
		}
		Static.game.requestFocus();
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		if(evt.getKeyCode() == KeyEvent.VK_UP)
			upPressed = false;
		else if(evt.getKeyCode() == KeyEvent.VK_RIGHT)
			rightPressed = false;
		else if(evt.getKeyCode() == KeyEvent.VK_DOWN)
			downPressed = false;
		else if(evt.getKeyCode() == KeyEvent.VK_LEFT)
			leftPressed = false;
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		
	}

}

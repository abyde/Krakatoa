package game.platform;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class WorldCanvas extends Canvas {
	private static final char CHAR_LEFT = 'q';
	private static final char CHAR_RIGHT = 'w';
	private static final char CHAR_JUMP = ' ';

	public static void main(String[] args) {
		World world = new World();
		world.addPlatform(new Platform(-1, -1, 100));
		world.addPlatform(new Platform(5, 5, 10));
		world.addPlatform(new Platform(2, 2, 4));
		world.addPlatform(new Platform(12, 2, 4));

		Player player = new Player(new DLocation(0, 7));
		world.addPlayer(player);
		player.fall();
		WorldCanvas wc = new WorldCanvas(world);

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container p = f.getContentPane();
		p.setLayout(new BorderLayout());
		p.add(wc, BorderLayout.CENTER);
		wc.setPreferredSize(new Dimension(600, 400));
		f.pack();
		f.setVisible(true);
	}

	World world;
	int squareSize = 25;
	int height;
	Thread moveThread;
	WorldThread move;

	public WorldCanvas(World world) {
		this.world = world;
		move = new WorldThread(this, world);
		moveThread = new Thread(move);
		moveThread.start();

		final Player player = world.player;
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent ke) {
				char c = ke.getKeyChar();
				if (c == CHAR_LEFT) {
					player.run(-1);
				} else if (c == CHAR_RIGHT) {
					player.run(1);
				} else if (c == CHAR_JUMP) {
					player.jump();
				}
			}

			@Override
			public void keyReleased(KeyEvent ke) {
				player.run(0);
			}

			@Override
			public void keyTyped(KeyEvent ke) {
				char c = ke.getKeyChar();
				if (c == CHAR_JUMP) {
					player.jump();
				}
			}
		});
	}

	public void paint(Graphics g) {
		height = getHeight();
		for (Platform p : world.platforms) {
			drawPlatform(g, p);
		}
		drawPlayer(g, world.player);
	}

	private void drawPlatform(Graphics g, Platform p) {
		g.setColor(Color.black);
		int sx = screenX(p.x);
		int sx2 = screenX(p.x + p.length);
		int sy = screenY(p.y);
		int sy2 = screenY(p.y + 1);
		g.fillRect(sx, sy2, (sx2 - sx), (sy - sy2));
	}

	private void drawPlayer(Graphics g, Player p) {
		g.setColor(Color.red);
		int sx = screenX(p.loc.x);
		int sy = screenY(p.loc.y);
		int sx2 = screenX(p.loc.x + 1);
		int sy2 = screenY(p.loc.y + 1);
		g.fillArc(sx, sy2, (sx2 - sx), (sy - sy2), 0, 360);
	}

	private int screenX(double lx) {
		return (int) (lx * squareSize);
	}

	private int screenY(double ly) {
		return height - (int) (ly * squareSize);
	}
}

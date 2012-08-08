package game.platform;

public class WorldThread implements Runnable {

	World world;
	WorldCanvas c;

	public WorldThread(WorldCanvas c, World world) {
		this.c = c;
		this.world = world;
	}

	public void run() {
		try {
			while (true) {
				Thread.sleep(20);
				world.move(0.07);
				c.repaint();
			}
		} catch (InterruptedException e) {
		}
	}

}

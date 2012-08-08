package game.platform;

import java.util.ArrayList;
import java.util.Collection;

public class World {
	
	World() {}
	Collection<Platform> platforms = new ArrayList<Platform>();
	Player player;
	
	public void addPlatform(Platform platform) {
		platforms.add(platform);
	}
	
	public void addPlayer(Player player) {
		this.player = player;
	}
	
	public void move(double dt) {
		player.move(dt);

		switch (player.motion) {
		case FALLING:
			if (player.v.y >= 0)
				return;
			
			for(Platform platform : platforms) {
				if (!platform.xIntersec(player.loc.x)) {
	//				System.out.println("player at " + player.loc + " does not X intersect with " + platform);
					continue;
				}
				
				if (!platform.yIntersec(player.loc.y)) {
//					System.out.println("player at " + player.loc + " does not Y intersect with " + platform);
					continue;
				}

				// land!
				player.land(platform);
			}
			break;
		case WALKING:
			// check if there's air underneath!
			if (!player.support.xIntersec(player.loc.x)) {
				player.fall();
			}
		}
	}

}

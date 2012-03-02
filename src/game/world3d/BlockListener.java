package game.world3d;

public interface BlockListener {

	void blockAdded(Block b);
	
	void blockMoved(Block b);
	
	void blockRemoved(Block b);
}

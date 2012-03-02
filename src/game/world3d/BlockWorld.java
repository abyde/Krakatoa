package game.world3d;

import java.util.Iterator;

public interface BlockWorld {

	/**
	 * Describe what is at a particular location.
	 * @return null if there is no block there.
	 */
	Block at(int x, int y, int z);
	
	/** Add a call-back that allows notification of changes to the world. */
	void addBlockListener(BlockListener blockListener);
	
	/** Iterate over all blocks.  The ordering should be lexicographic, for each z, over x then y. */
	Iterator<Block> allBlocks();
	
	/** Called by 'block' itself, to establish a connection between the world and the block. */
	void add(Block b);
}

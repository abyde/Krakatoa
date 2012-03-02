package game.world3d;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * An implementation of BlockWorld which indexes blocks by VIEW_ORDER.
 */
public class BlockWorldImpl implements BlockWorld {

	/** Order blocks by z then x then y. */
	private TreeSet<Block> blocks = new TreeSet<Block>(Block.VIEW_ORDER);
	private Set<BlockListener> listeners = new HashSet<BlockListener>();

	@Override
	public Block at(int x, int y, int z) {
		return null;
	}

	@Override
	public void addBlockListener(BlockListener blockListener) {
		if (blockListener == null)
			return;
		listeners.add(blockListener);
	}

	@Override
	public Iterator<Block> allBlocks() {
		return blocks.iterator();
	}

	@Override
	public void add(Block b) {
		if (b == null)
			return;
		blocks.add(b);
	}

	/**
	 * Move to another location without checking if it is occupied.
	 */
	public void moveTo(Block b, int x, int y, int z) {
		// check if the destination is occupied
		Block bOld = at(x, y, z);
		if (bOld != null)
			throw new IllegalArgumentException("Cannot move block: location "
					+ toString(x, y, z) + " is occupied by " + bOld);
		
		blocks.add(b);
		for(BlockListener bl : listeners) {
			bl.blockMoved(b);
		}
	}

	public static String toString(int x, int y, int z) {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}

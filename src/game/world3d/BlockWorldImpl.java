package game.world3d;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * An implementation of BlockWorld which indexes blocks by VIEW_ORDER.
 */
public class BlockWorldImpl implements IterableBlockWorld {

	/** Order blocks by z then x then y. */
	private TreeSet<Block> xyBlocks = new TreeSet<Block>(Block.VIEW_ORDER(1, 1));
	private TreeSet<Block> negxyBlocks = new TreeSet<Block>(Block.VIEW_ORDER(-1, 1));
	private TreeSet<Block> xnegyBlocks = new TreeSet<Block>(Block.VIEW_ORDER(1, -1));
	private TreeSet<Block> negxnegyBlocks = new TreeSet<Block>(Block.VIEW_ORDER(-1, -1));
	
	private Set<BlockListener> listeners = new HashSet<BlockListener>();

	/**
	 * TODO -- not implemented yet!
	 */
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
	public Iterator<Block> allBlocksXY() {
		return xyBlocks.iterator();
	}

	@Override
	public Iterator<Block> allBlocksRevXY() {
		return negxyBlocks.iterator();
	}

	@Override
	public Iterator<Block> allBlocksRevXRevY() {
		return negxnegyBlocks.iterator();
	}

	@Override
	public Iterator<Block> allBlocksXRevY() {
		return xnegyBlocks.iterator();
	}

	@Override
	public void add(Block b) {
		if (b == null)
			return;
		xyBlocks.add(b);
		negxyBlocks.add(b);
		negxnegyBlocks.add(b);
		xnegyBlocks.add(b);
	}

	public void remove(Block b) {
		if (b == null)
			return;
		xyBlocks.remove(b);
		negxyBlocks.remove(b);
		xnegyBlocks.remove(b);
		negxnegyBlocks.remove(b);
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

		remove(b);
		b.moveTo(x, y, z);
		add(b);
		
		for(BlockListener bl : listeners) {
			bl.blockMoved(b);
		}
	}

	public static String toString(int x, int y, int z) {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}

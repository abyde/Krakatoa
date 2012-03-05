package game.world3d;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * An implementation of BlockWorld which indexes blocks by VIEW_ORDER.
 */
public class BlockWorldImpl implements IterableBlockWorld {
	private static Logger log = Logger.getLogger(BlockWorldImpl.class);
	private boolean isTrace = log.isTraceEnabled();

	/** Order blocks by z then x then y. */
	private TreeSet<Block> xyBlocks = new TreeSet<Block>(Block.VIEW_ORDER(1, 1));
	private TreeSet<Block> negxyBlocks = new TreeSet<Block>(Block.VIEW_ORDER(
			-1, 1));
	private TreeSet<Block> xnegyBlocks = new TreeSet<Block>(Block.VIEW_ORDER(1,
			-1));
	private TreeSet<Block> negxnegyBlocks = new TreeSet<Block>(
			Block.VIEW_ORDER(-1, -1));

	private TreeMap<String, Block> lookup = new TreeMap<String, Block>();

	private Set<BlockListener> listeners = new HashSet<BlockListener>();

	@Override
	public Block at(int x, int y, int z) {
		String key = keyOfLocation(x, y, z);
		if (isTrace)
			log.trace(toString(x, y, z) + " -> " + key);
		if (lookup.containsKey(key))
			return lookup.get(key);
		return null;
	}

	private String keyOfBlock(Block b) {
		return keyOfLocation(b.x(), b.y(), b.z());
	}
	
	/**
	 * Convert a tuple of integers into a string. For this to work, we can't use
	 * negative coordinates.
	 */
	private String keyOfLocation(int x, int y, int z) {
		if ((x < 0) || (y < 0) || (z < 0))
			throw new IllegalArgumentException(
					"Negative coordinates not allowed!");
		byte[] b = new byte[6];
		b[0] = (byte) ((x >> 8) & 0xff);
		b[1] = (byte) (x & 0xff);
		b[2] = (byte) ((y >> 8) & 0xff);
		b[3] = (byte) (y & 0xff);
		b[4] = (byte) ((z >> 8) & 0xff);
		b[5] = (byte) (z & 0xff);
		return new String(b);
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
		
		// TODO what to do if the space is already occupied?
		String key = keyOfBlock(b);
		lookup.put(key, b);
		
		for (BlockListener bl : listeners) {
			bl.blockAdded(b);
		}
	}

	@Override
	public void remove(Block b) {
		if (b == null)
			return;
		xyBlocks.remove(b);
		negxyBlocks.remove(b);
		xnegyBlocks.remove(b);
		negxnegyBlocks.remove(b);
		
		// TODO what to do if the space is not already occupied?
		String key = keyOfBlock(b);
		lookup.remove(key);
		
		for (BlockListener bl : listeners) {
			bl.blockRemoved(b);
		}
	}

	public static String toString(int x, int y, int z) {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}

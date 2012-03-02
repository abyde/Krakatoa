package game.world3d;

import java.util.Comparator;

/**
 * The basics of what it means to be a block.
 * 
 * @author abyde
 */
public class Block {

	public static Comparator<Block> VIEW_ORDER = new Comparator<Block>() {
		public int compare(Block b1, Block b2) {
			assert (b1 != null);
			assert (b2 != null);
			int c = b1.z - b2.z;
			if (c != 0)
				return c;
			c = b1.x - b2.x;
			if (c != 0)
				return c;
			return b1.y - b2.y;
		}
	};

	private static int id_counter = 0;
	private int x, y, z;
	public final int id = (id_counter++);
	private int type;

	public Block(int type, int x, int y, int z) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	void moveTo(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int z() {
		return z;
	}

	public int type() {
		return type;
	}
}
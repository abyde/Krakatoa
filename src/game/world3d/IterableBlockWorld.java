package game.world3d;

import java.util.Iterator;

/**
 * A block world which offers the ability to iterate over the blocks. We iterate
 * from the ground up, and then offer 4 possibilities in terms of the
 * lexicographic ordering of x and y, depending on whether we go forward or
 * backward in each dimension.
 * 
 * @author abyde
 */
public interface IterableBlockWorld extends BlockWorld {

	/** Forward in both x and y. */
	Iterator<Block> allBlocksRevXY();

	/** Forward in x, reverse in y. */
	Iterator<Block> allBlocksXRevY();

	/** Reverse in x, forward in y. */
	Iterator<Block> allBlocksXY();

	/** Reverse in both x and y. */
	Iterator<Block> allBlocksRevXRevY();
}

package game.world3d;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * Check the correctness of add.
 * 
 * @author abyde
 */
public class BlockWorldImplTest {
	private static Logger log = Logger.getLogger(BlockWorldImplTest.class);
	
	public static void main(String[] args) {
		int numBlocks = 100;
		int[] x = new int[numBlocks];
		int[] y = new int[numBlocks];
		int[] z = new int[numBlocks];

		Set<String> s = new TreeSet<String>();

		BlockWorld world = new BlockWorldImpl();

		Random r = new Random(1);
		log.info("Adding blocks");
		for (int i = 0; i < numBlocks; i++) {
			x[i] = r.nextInt(100);
			y[i] = r.nextInt(100);
			z[i] = r.nextInt(100);
			String id = BlockWorldImpl.toString(x[i], y[i], z[i]);
			s.add(id);

			try {
				world.add(new Block(0, x[i], y[i], z[i]));
			} catch (IllegalArgumentException e) {
				continue;
			}
		}

		log.info("Checking all valid blocks");
		for (int i = 0; i < numBlocks; i++) {
			// try the block itself.
			Block b = world.at(x[i], y[i], z[i]);
			if (b == null)
				throw new RuntimeException("Was expecting to find a block at "
						+ BlockWorldImpl.toString(x[i], y[i], z[i]));
		}
		
		log.info("Checking a few INvalid blocks");
		for(int i = 0; i < 20; ){
			x[i] = r.nextInt(100);
			y[i] = r.nextInt(100);
			z[i] = r.nextInt(100);

			String id = BlockWorldImpl.toString(x[i], y[i], z[i]);
			
			if (s.contains(id))
				continue;
			
			Block b = world.at(x[i], y[i], z[i]);
			if (b != null) {
				throw new RuntimeException("Expected to NOT find block at " + id + " but I did!");
			}
			i++;
		}
		log.info("Success");
	}
}

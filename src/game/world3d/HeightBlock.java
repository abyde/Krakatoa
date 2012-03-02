package game.world3d;

/**
 * An extension of block defined by a height for each of the 4 x-y corners.
 * 
 * @author abyde
 */
public class HeightBlock extends Block {

	public double[][] height = new double[][] { { 1, 1 }, { 1, 1 } };

	public HeightBlock(int x, int y, int z) {
		super(0, x, y, z);
	}

	public HeightBlock(double[][] height, int x, int y, int z) {
		super(0, x, y, z);
		this.height = height;
	}

}

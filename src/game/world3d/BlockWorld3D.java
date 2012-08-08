package game.world3d;

public class BlockWorld3D {
	
	HeightBlock3D[][][] world;
	
	public BlockWorld3D(int xSize, int ySize, int zSize){
		world = new HeightBlock3D[xSize][ySize][zSize];
	}
	
	public HeightBlock3D at(int x, int y, int z) {
		return world[x + world.length/2][y + world[x].length/2][z + world[x][y].length/2];
	}

	public void addBlockListener(BlockListener blockListener) {
		// TODO Auto-generated method stub
		
	}

//	public void add(HeightBlock3D b) {
//		// TODO Auto-generated method stub
//		world[b.x + world.length/2][b.y + world[b.x].length/2][b.z + world[b.x][b.y].
//		                                                       length/2] = b;
//	}
//
//	public void remove(HeightBlock3D b) {
//		// TODO Auto-generated method stub
//		world[b.x + world.length/2][b.y + world[b.x].length/2][b.z + world[b.x][b.y].
//		                                                       length/2] = null;
//	}

}

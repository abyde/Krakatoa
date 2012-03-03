package game.world3d;

public interface BlockFactory<B extends Block> {
	B make(int type, int x, int y, int z);
}

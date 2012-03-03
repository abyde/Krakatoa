package game.world3d;

import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;
import javax.vecmath.*;

public class Block3D extends Block {
	
	TransformGroup t = new TransformGroup();
	
	public Block3D(int type, int x, int y, int z){
		super(type, x, y, z);
		//it's supposed to be 0.5, but I've put 0.46 so we
		//can see all the blocks. We'll be able
		//to see them normally after placing textures
		t.addChild(new ColorCube(0.46));
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(x, y, z));
		//cross it
		t.setTransform(t3d);
	}
}

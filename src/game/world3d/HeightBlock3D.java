package game.world3d;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;

public class HeightBlock3D {
	
	TransformGroup t;
	
	static float size = 0.49f;
	
	Point3f leftUpFront;
	Point3f leftUpBack;
	Point3f leftDownFront;
	Point3f leftDownBack;
	Point3f rightUpFront;
	Point3f rightUpBack;
	Point3f rightDownFront;
	Point3f rightDownBack;
	
	public HeightBlock3D(float[] height, int x, int y, int z) {

		leftUpFront = new Point3f(-size, size-(size-height[0]), size);
		leftUpBack = new Point3f(-size, size-(size-height[1]), -size);
		leftDownFront = new Point3f(-size, -size, size);
		leftDownBack = new Point3f(-size, -size, -size);
		rightUpFront = new Point3f(size, size-(size-height[2]), size);
		rightUpBack = new Point3f(size, size-(size-height[3]), -size);
		rightDownFront = new Point3f(size, -size, size);
		rightDownBack = new Point3f(size, -size, -size);
		
		t = new TransformGroup();
		t.addChild(left());
		t.addChild(right());
		t.addChild(up());
		t.addChild(down());
		t.addChild(back());
		t.addChild(front());
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(x, y, z));
		t.setTransform(t3d);
	}
	
	private Shape3D left(){
		QuadArray q = new QuadArray(4, QuadArray.COORDINATES |
				GeometryArray.TEXTURE_COORDINATE_2);
		
		q.setCoordinate(0, leftUpFront);
		q.setTextureCoordinate(0, new Point2f(1, 1));
		q.setCoordinate(1, leftUpBack);
		q.setTextureCoordinate(1, new Point2f(0, 1));
		q.setCoordinate(2, leftDownBack);
		q.setTextureCoordinate(2, new Point2f(0, 0));
		q.setCoordinate(3, leftDownFront);
		q.setTextureCoordinate(3, new Point2f(1, 0));
		Appearance app = app();
		Texture texImage = new TextureLoader("left.png", null).getTexture();
	   	app.setTexture (texImage);
		
		return new Shape3D(q, app);
	}
	
	private Shape3D right(){
		QuadArray q = new QuadArray(4, QuadArray.COORDINATES |
				GeometryArray.TEXTURE_COORDINATE_2);
		
		q.setCoordinate(0, rightDownFront);
		q.setTextureCoordinate(0, new Point2f(0, 0));
		q.setCoordinate(1, rightDownBack);
		q.setTextureCoordinate(1, new Point2f(1, 0));
		q.setCoordinate(2, rightUpBack);
		q.setTextureCoordinate(2, new Point2f(1, 1));
		q.setCoordinate(3, rightUpFront);
		q.setTextureCoordinate(3, new Point2f(0, 1));
		Appearance app = app();
		Texture texImage = new TextureLoader("right.png", null).getTexture();
	   	app.setTexture (texImage);
		
		return new Shape3D(q, app);
	}

	private Shape3D up(){
		QuadArray q = new QuadArray(4, QuadArray.COORDINATES |
				GeometryArray.TEXTURE_COORDINATE_2);
		
		q.setCoordinate(0, leftUpBack);
		q.setTextureCoordinate(0, new Point2f(0, 1));
		q.setCoordinate(1, leftUpFront);
		q.setTextureCoordinate(1, new Point2f(0, 0));
		q.setCoordinate(2, rightUpFront);
		q.setTextureCoordinate(2, new Point2f(1, 0));
		q.setCoordinate(3, rightUpBack);
		q.setTextureCoordinate(3, new Point2f(1, 1));
		Appearance app = app();
		Texture texImage = new TextureLoader("up.png", null).getTexture();
	   	app.setTexture (texImage);
		
		return new Shape3D(q, app);
	}

	private Shape3D down(){
		QuadArray q = new QuadArray(4, QuadArray.COORDINATES |
				GeometryArray.TEXTURE_COORDINATE_2);
		
		q.setCoordinate(0, rightDownBack);
		q.setTextureCoordinate(0, new Point2f(1, 0));
		q.setCoordinate(1, rightDownFront);
		q.setTextureCoordinate(1, new Point2f(1, 1));
		q.setCoordinate(2, leftDownFront);
		q.setTextureCoordinate(2, new Point2f(0, 1));
		q.setCoordinate(3, leftDownBack);
		q.setTextureCoordinate(3, new Point2f(0, 0));
		Appearance app = app();
		Texture texImage = new TextureLoader("down.png", null).getTexture();
	   	app.setTexture (texImage);
		
		return new Shape3D(q, app);
	}

	private Shape3D back(){
		QuadArray q = new QuadArray(4, QuadArray.COORDINATES |
				GeometryArray.TEXTURE_COORDINATE_2);
		
		q.setCoordinate(0, rightUpBack);
		q.setTextureCoordinate(0, new Point2f(0, 1));
		q.setCoordinate(1, rightDownBack);
		q.setTextureCoordinate(1, new Point2f(0, 0));
		q.setCoordinate(2, leftDownBack);
		q.setTextureCoordinate(2, new Point2f(1, 0));
		q.setCoordinate(3, leftUpBack);
		q.setTextureCoordinate(3, new Point2f(1, 1));
		Appearance app = app();
		Texture texImage = new TextureLoader("back.png", null).getTexture();
	   	app.setTexture (texImage);
		
		return new Shape3D(q, app);
	}

	private Shape3D front(){
		QuadArray q = new QuadArray(4, QuadArray.COORDINATES |
				GeometryArray.TEXTURE_COORDINATE_2);
		
		q.setCoordinate(0, rightDownFront);
		q.setTextureCoordinate(0, new Point2f(1, 0));
		q.setCoordinate(1, rightUpFront);
		q.setTextureCoordinate(1, new Point2f(1, 1));
		q.setCoordinate(2, leftUpFront);
		q.setTextureCoordinate(2, new Point2f(0, 1));
		q.setCoordinate(3, leftDownFront);
		q.setTextureCoordinate(3, new Point2f(0, 0));
		Appearance app = app();
		Texture texImage = new TextureLoader("front.png", null).getTexture();
	   	app.setTexture (texImage);
		
		return new Shape3D(q, app);
	}
	private Appearance app(){
		Appearance app = new Appearance();
		Material mat = new Material();
		mat.setAmbientColor(new Color3f(0.0f,0.0f,1.0f));
		mat.setDiffuseColor(new Color3f(0.7f,0.0f,0.7f));
		mat.setSpecularColor(new Color3f(0.7f,0.7f,0.7f));
		mat.setShininess(90f);
		app.setMaterial(mat);
		return app;
	}
}

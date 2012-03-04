package game.world3d;

import java.awt.*;
import java.util.Random;

import javax.swing.*;
import javax.vecmath.*;
import javax.media.j3d.*;

import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.vp.*;

public class Main extends JFrame{
	
	private static final long serialVersionUID = 1L;

	private static final int BOUNDSIZE = 100;
	
	SimpleUniverse universe = null;
	Canvas3D canvas = null;
	BranchGroup scene = null;
	BoundingSphere bounds = null;
	
	public static void main(String[] args){
		new Main();
	}
	public Main(){
		setupComponents();
		initWorld();
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void setupComponents(){
		setLayout(new BorderLayout());
		
		//initialize and configure the canvas
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(gc);
		add(canvas, BorderLayout.CENTER);
		canvas.setFocusable(true);
		canvas.requestFocus();
		
		//initialize the other stuff
		universe = new SimpleUniverse(canvas);
		scene = new BranchGroup();
		bounds = new BoundingSphere(new Point3d(0, 0, 0), BOUNDSIZE);		
	}
	
	/**
	 * Adds all necessary nodes to the scene graph
	 * */
	private void initWorld(){
		//slightly blue light, so we have a nice moonlight that is invisible during daytime
		//because the following directional light will be visible, the sunlight
		AmbientLight ambientLight = new AmbientLight(new Color3f(0.31f, 0.31f, 0.68f));
		ambientLight.setInfluencingBounds(bounds);
		scene.addChild(ambientLight);
		
		//slightly yellow light, directly from above
		DirectionalLight light = new DirectionalLight(new Color3f(1.0f, 1.0f, 0.56f),
				new Vector3f(0.0f, 1.0f, 0.0f));
		light.setInfluencingBounds(bounds);
		scene.addChild(light);
		
		//add a background with a plain sky color, for now
		Background back = new Background();
		back.setApplicationBounds(bounds);
		back.setColor(0.17f, 0.65f, 0.92f);
		scene.addChild(back);
		
		addWorld();
		
		//OrbitBehavior for now, just so we can see the scene
		OrbitBehavior orbit = new OrbitBehavior(canvas);
		orbit.setSchedulingBounds(bounds);
		universe.getViewingPlatform().setViewPlatformBehavior(orbit);
		
		//initialize user position
		TransformGroup steerTG = universe.getViewingPlatform().getViewPlatformTransform();
		Transform3D t3d = new Transform3D();
		steerTG.getTransform(t3d);
		t3d.lookAt(new Point3d(-10, 10, 20), new Point3d(0, 0, 0), new Vector3d(0,1,0));
		//arguments are: viewer position, where looking and up direction
		t3d.invert();
		steerTG.setTransform(t3d);
		
		//compile the scene for optimizations
		scene.compile();
		universe.addBranchGraph(scene);
	}
	private void addWorld(){
		//make the floor go 0.5 down
		TransformGroup tg = new TransformGroup();
		BranchGroup bg = new CheckerFloor().getBG();
		tg.addChild(bg);
		Transform3D move = new Transform3D();
		move.setTranslation(new Vector3d(0, -0.5, 0));
		tg.setTransform(move);
		scene.addChild(tg);
		
		float[] def = {HeightBlock3D.size, HeightBlock3D.size, HeightBlock3D.size,
				HeightBlock3D.size};
		
		int w = 3;
		int n = 4;
		for (int i = -w; i <= w; i++) {
			scene.addChild(new HeightBlock3D(def, i, 0, -w - 1).t);
			scene.addChild(new HeightBlock3D(def, i, 0, w + 1).t);
			scene.addChild(new HeightBlock3D(def, -w - 1, 0, i).t);
			scene.addChild(new HeightBlock3D(def, w + 1, 0, i).t);

			if (Math.abs(i) <= 1) {
				scene.addChild(rand(i, 0, -w -2).t);
				scene.addChild(rand(i, 0, w + 2).t);
				scene.addChild(rand(-w - 2, 0, i).t);
				scene.addChild(rand(w + 2, 0, i).t);
			}
		}

		for (int i = 0; i < n; i++) {
			scene.addChild(new HeightBlock3D(def, -w - 1, i, -w -1).t);
			scene.addChild(new HeightBlock3D(def, -w - 1, i, w + 1).t);
			scene.addChild(new HeightBlock3D(def, w + 1, i, -w -1).t);
			scene.addChild(new HeightBlock3D(def, w + 1, i, w + 1).t);
		}
		
	}
	private static HeightBlock3D rand(int x, int y, int z) {
		float[] height = {
				(float) Math.random() - HeightBlock3D.size,
				(float) Math.random() - HeightBlock3D.size,
				(float) Math.random() - HeightBlock3D.size,
				(float) Math.random() - HeightBlock3D.size};
		return new HeightBlock3D(height, x, y, z);
	}
}

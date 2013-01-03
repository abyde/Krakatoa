package riseOfDots.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import riseOfDots.Nation;
import riseOfDots.Static;


public class Unit extends Entity implements Mover {

	public Nation nation = null;

	public final double speedModifier = 1;

	protected int toX;
	protected int toY;
	protected int nextX;
	protected int nextY;

	protected double xspeed = 0;
	protected double yspeed = 0;

	boolean newPath;
	int step = 0;


	public Unit(int x, int y, Nation n) {
		super(x, y);
		toX = x;
		toY = y;
		nextX = x;
		nextY = y;
		nation = n;
		selectable = true;
	}

	@Override
	public boolean canWalk() {
		return true;
	}

	@Override
	public boolean canNavigate() {
		return false;
	}

	@Override
	public void update() {
		updatePos();
	}

	@Override
	public void draw(Graphics2D g) {
		Point p = Static.viewport.getDrawPoint(x, y);
		if (nation != null)
			g.setColor(nation.color);
		else
			g.setColor(Color.RED);
		if (isSelected)
			g.setColor(Color.WHITE);
		int size = (int) (600 / Static.viewport.getViewWidth());
		g.fillArc(p.x, p.y, size / 2, size / 2, 0, 360);
	}

	private void updatePos() {
		//do speed setting here, and update path if needed
		Static.world.move(this, (int) x, (int) y);
	}

	@Override
	public void rightMouseClick(int gx, int gy) {
		toX = gx;
		toY = gy;
		new Thread(new Runnable() {
			@Override
			public void run() {
				updatePath();
			}
		}).start();
	}

	private void updatePath() {
		
	}

}

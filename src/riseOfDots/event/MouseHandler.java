package riseOfDots.event;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import riseOfDots.Static;
import riseOfDots.entity.Entity;
import riseOfDots.gfx.Viewport;

public class MouseHandler implements MouseListener, MouseMotionListener {

	public int x = 0;
	public int y = 0;
	public int gx = 0;
	public int gy = 0;

	public int sgx = 0;
	public int sgy = 0;
	public int fgx = 0;
	public int fgy = 0;

	public boolean pressed = false;

	@Override
	public void mouseClicked(MouseEvent evt) {
		updateGridCoords();
	}

	@Override
	public void mouseEntered(MouseEvent evt) {
		updateGridCoords();
	}

	@Override
	public void mouseExited(MouseEvent evt) {
		updateGridCoords();
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		updateGridCoords();
		if (evt.getButton() == MouseEvent.BUTTON1) {
			pressed = true;
			sgx = evt.getX();
			sgy = evt.getY();
			fgx = sgx;
			fgy = sgy;
			moveView(sgx, sgy);
		} else if (evt.getButton() == MouseEvent.BUTTON3) {
			// Send the right mouse click event to all selected entities.
			for (Entity entity : Static.world.selected) {
				entity.rightMouseClick(gx, gy);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		updateGridCoords();
		pressed = false;
		fgx = evt.getX();
		fgy = evt.getY();

	}

	
	/**
	 * If dragging with the left button, select all units in the given area.
	 */
	@Override
	public void mouseDragged(MouseEvent evt) {
		// TODO Superhack!  Should be some BUTTON_MASK type thing here instead...
		if ((evt.getModifiers() & 16) == 0) {
			return;
		}
		updateGridCoords();
		fgx = evt.getX();
		fgy = evt.getY();
		moveView(fgx, fgy);

		int fx = (sgx < fgx) ? sgx : fgx;
		int fy = (sgy < fgy) ? sgy : fgy;
		int w = Math.abs(sgx - fgx);
		int h = Math.abs(sgy - fgy);

		Static.viewport.selectUnits(getWorldArea(fx, fy, w, h));
	}

	@Override
	public void mouseMoved(MouseEvent evt) {
		x = evt.getX();
		y = evt.getY();
		updateGridCoords();
	}

	public void updateGridCoords() {
		if (Static.world == null)
			return;
		Viewport v = Static.viewport;
		Point l = v.getViewLocation();
		gx = (int) (x / (600 / v.getViewWidth())) + l.x;
		gy = (int) (y / (600 / v.getViewWidth())) + l.y;
		if (gx >= Static.world.size())
			gx = Static.world.size() - 1;
	}

	private void moveView(int x, int y) {

		Rectangle minimap = new Rectangle(610, 10, 180, 180);
		int size = Static.world.size();
		double ration = (size / 180);
		int tiles = Static.viewport.getViewWidth();

		if (minimap.contains(x, y)) {
			int r = (int) ((Static.viewport.getViewWidth() / ration) / 2);
			int mx = x - minimap.x;
			int my = y - minimap.y;
			int desiredX = (int) ((mx - r) * ration);
			int desiredY = (int) ((my - r) * ration);
			if (desiredX < 0)
				desiredX = 0;
			if (desiredY < 0)
				desiredY = 0;
			if (desiredX > size - tiles)
				desiredX = size - tiles;
			if (desiredY > size - tiles)
				desiredY = size - tiles;
			Static.viewport.setViewLocation(desiredX, desiredY);
		}
	}

	public Rectangle getWorldArea(int x, int y, int w, int h) {
		if (Static.world == null)
			return null;
		Viewport v = Static.viewport;
		Point l = v.getViewLocation();
		int rx = (int) (x / (600 / v.getViewWidth())) + l.x;
		int ry = (int) (y / (600 / v.getViewWidth())) + l.y;
		int rx2 = (int) ((x + w) / (600 / v.getViewWidth())) + l.x;
		int ry2 = (int) ((y + h) / (600 / v.getViewWidth())) + l.y;
		if (rx >= Static.world.size())
			rx = Static.world.size() - 1;
		return new Rectangle(rx, ry, rx2 - rx, ry2 - ry);
	}

}

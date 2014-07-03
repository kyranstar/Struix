package ui.windows.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import ui.windows.WindowComponent;

public class MapComponent extends WindowComponent {

	private static final long serialVersionUID = 1L;

	public static final int GRID_SIZE = 60;
	private static final int SPACING_SIZE = 10;

	private Optional<Point> pressedPoint = Optional.empty();
	private int currentX = 0;
	private int currentY = 0;

	private List<MapRoom> mapRooms = new LinkedList<MapRoom>();

	private MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				for (MapRoom room : mapRooms) {
					if (room.getBounds().contains(e.getPoint())) {
						// OPEN UP ROOM DIALOGUE
						return;
					}
				}
			}
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			for (MapRoom c : mapRooms) {
				Point mouse = e.getPoint();
				mouse.translate(currentX, currentY);
				
				if (c.getBounds().contains(mouse)) {
					c.mousePressed(e);
					return;
				}
			}

			pressedPoint = Optional.of(e.getPoint());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			pressedPoint = Optional.empty();
			for (MapRoom c : mapRooms) {
				c.mouseReleased(e);
			}
			repaint();
		}

	};

	private MouseMotionListener mousemMotionListener = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent e) {
			for (MapRoom c : mapRooms) {
				c.mouseDragged(e);
				repaint();
			}
			if (pressedPoint.isPresent()) {
				currentX = currentX + pressedPoint.get().x - e.getPoint().x;
				currentY = currentY + pressedPoint.get().y - e.getPoint().y;

				pressedPoint = Optional.of(e.getPoint());
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}
	};

	public MapComponent() {
		setPreferredSize(new Dimension(200, 200));
		setSize(getPreferredSize());
		this.setBackground(new Color(255, 200, 255, 100));
		this.addMouseMotionListener(mousemMotionListener);
		this.addMouseListener(mouseListener);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);		
		g.setColor(new Color(50,50,50,150));
		for (int x = -(currentX % GRID_SIZE ) - GRID_SIZE; x < getWidth(); x += GRID_SIZE) {
			for (int y = -(currentY % GRID_SIZE) - GRID_SIZE; y < getHeight(); y += GRID_SIZE) {
				g.drawRoundRect(x, y, GRID_SIZE - SPACING_SIZE, GRID_SIZE - SPACING_SIZE, MapRoom.CORNER_ROUNDNESS, MapRoom.CORNER_ROUNDNESS);
			}
		}
		g.translate(-currentX, -currentY);
		for (MapRoom c : mapRooms) {
			c.draw(g);
		}
		g.translate(currentX, currentY);
		g.setColor(Color.GREEN);
		g.drawString("X: " + currentX + " Y: " + currentY, 10, 20);
		
	}

	public void addRoom(MapRoom c) {
		this.mapRooms.add(c);
		repaint();
	}
	public void addAndCenterRoom(MapRoom c){
		int x = currentX + getWidth()/2 - c.getBounds().width/2;
		int y = currentY + getHeight()/2 - c.getBounds().height/2;
		
		c.setLocation(x - (x % GRID_SIZE), y - (y % GRID_SIZE));
		this.mapRooms.add(c);
		repaint();
	}
}

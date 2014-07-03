package ui.windows.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import ui.windows.WindowComponent;

public class MapComponent extends WindowComponent {

	private static final long serialVersionUID = 1L;

	public static final int GRID_SIZE = 60;
	private static final int SPACING_SIZE = 10;
	
	private static final double MAX_SCALE = 3.0;
	private static final double MIN_SCALE = 0.25;
	
	private static final Color BACKGROUND_COLOR =new Color(133, 167, 190);
	private static final Color HOLDER_COLOR = new Color(0,0,0,150);
	
	private double scale = 1.0;

	private Optional<Point> pressedPoint = Optional.empty();
	private int currentX = 0;
	private int currentY = 0;

	private List<MapRoom> mapRooms = new LinkedList<MapRoom>();

	private MouseWheelListener mouseWheelListener = new MouseWheelListener(){
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			//going inward
			if(e.getWheelRotation() < 0){
				if(scale < MAX_SCALE){
				
					if(scale <= 1)
						scale *= 1.5;
					else
						scale += 0.25;
				
					currentX += getWidth()/scale/4;
					currentY += getHeight()/scale/4;
				}
			}else{
				if(scale > MIN_SCALE){
					if(scale <= 1)
						scale /= 1.5;
					else
						scale -= 0.25;
				
					currentX -= getWidth()/scale/6;
					currentY -= getHeight()/scale/6;
				}
			}
			repaint();
		}
	};
	
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
				mouse.translate((int)(currentX * scale ), (int) (currentY * scale));
				
				Rectangle bounds = (Rectangle) c.getBounds().clone();
				bounds.x *= scale;
				bounds.y *= scale;
				bounds.width *= scale;
				bounds.height *= scale;
				
				if (bounds.contains(mouse)) {
					c.mousePressed(e);
					System.out.println("pressed");
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
		this.setBackground(BACKGROUND_COLOR);
		this.addMouseMotionListener(mousemMotionListener);
		this.addMouseListener(mouseListener);
		this.addMouseWheelListener(mouseWheelListener);
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		super.paint(g);		
		g.scale(scale, scale);
		g.setColor(HOLDER_COLOR);
		g.setStroke(new BasicStroke(2));
		for (int x = -(currentX % GRID_SIZE ) - GRID_SIZE; x < getWidth() / scale; x += GRID_SIZE) {
			for (int y = -(currentY % GRID_SIZE) - GRID_SIZE; y < getHeight() / scale; y += GRID_SIZE) {
				g.drawRoundRect(x, y, GRID_SIZE - SPACING_SIZE, GRID_SIZE - SPACING_SIZE, MapRoom.CORNER_ROUNDNESS, MapRoom.CORNER_ROUNDNESS);
			}
		}
		g.translate(-currentX, -currentY);
		for (MapRoom c : mapRooms) {
			c.draw(g);
		}
		g.translate(currentX, currentY);
		g.scale(1/scale, 1/scale);
		
		g.setColor(Color.GREEN);
		g.drawString("X: " + currentX + " Y: " + currentY, 10, 20);
	}

	public void addRoom(MapRoom c) {
		this.mapRooms.add(c);
		repaint();
	}
	public void addAndCenterRoom(MapRoom c){
		int x = (int) (currentX + (getWidth()/scale)/2 - c.getBounds().width/2);
		int y = (int) (currentY + (getHeight()/scale)/2 - c.getBounds().height/2);
		
		c.setLocation(x - (x % GRID_SIZE), y - (y % GRID_SIZE));
		this.mapRooms.add(c);
		repaint();
	}
}

package ui.windows.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

import ui.ColorPaletteConstants;
import ui.windows.WindowComponent;

import com.google.common.base.Optional;

public class MapComponent extends WindowComponent {

	private static final long serialVersionUID = 1L;

	public static final int GRID_SIZE = 60;
	private static final int SPACING_SIZE = 10;

	private static final double MAX_SCALE = 3.0;
	private static final double MIN_SCALE = 0.25;

	private double scale = 1.0;

	private Optional<Point> pressedPoint = Optional.absent();
	private int currentX = 0;
	private int currentY = 0;

	private List<MapRoom> mapRooms = new LinkedList<MapRoom>();
	
	private Optional<MapRoom> stuckToMouse = Optional.absent();

	private Tool currentTool = Tool.DRAG_ROOM;

	public enum Tool {
		DRAG_ROOM, CREATE_ROOM, CREATE_HALLWAY;
	}

	public MapComponent() {
		setPreferredSize(new Dimension(200, 200));
		setSize(getPreferredSize());
		this.setBackground(ColorPaletteConstants.MAP_BACKGROUND);
		this.addMouseMotionListener(mousemMotionListener);
		this.addMouseListener(mouseListener);
		this.addMouseWheelListener(mouseWheelListener);
	}


	private MouseWheelListener mouseWheelListener = new MouseWheelListener() {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// going inward
			if (e.getWheelRotation() < 0) {
				if (scale < MAX_SCALE) {

					if (scale <= 1)
						scale *= 1.5;
					else
						scale += 0.25;

					// hack that seems to make it zoom to center
					currentX += getWidth() / scale / 4;
					currentY += getHeight() / scale / 4;
				}
			} else {
				if (scale > MIN_SCALE) {
					if (scale <= 1)
						scale /= 1.5;
					else
						scale -= 0.25;

					// hack that seems to make it zoom to center
					currentX -= getWidth() / scale / 6;
					currentY -= getHeight() / scale / 6;
				}
			}
			repaint();
		}
	};

	private MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (currentTool == Tool.DRAG_ROOM) {
				if (e.getClickCount() >= 2) {
					for (MapRoom room : mapRooms) {
						if (room.getBounds().contains(e.getPoint())) {
							// OPEN UP ROOM DIALOGUE
							return;
						}
					}
				} else if (e.getClickCount() == 1) {
					setStuckToMouse(Optional.absent());
				}
			} 
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (currentTool == Tool.DRAG_ROOM) {
				for (MapRoom c : mapRooms) {
					Point mouse = e.getPoint();
					mouse.translate((int) (currentX * scale),
							(int) (currentY * scale));

					Rectangle bounds = (Rectangle) c.getBounds().clone();
					bounds.x *= scale;
					bounds.y *= scale;
					bounds.width *= scale;
					bounds.height *= scale;

					if (bounds.contains(mouse)) {
						c.mousePressed(e);
						return;
					}
				}

				pressedPoint = Optional.of(e.getPoint());
			}else if (currentTool == Tool.CREATE_ROOM) {
				if(getStuckToMouse().isPresent()){
					getStuckToMouse().get().snapToGrid();
				}
				
				setStuckToMouse(Optional.absent());

				Point mouse = e.getPoint();
				mouse.translate(
						(int) (currentX / scale - MapRoom.DEFAULT_WIDTH / 2),
						(int) (currentY / scale - MapRoom.DEFAULT_HEIGHT / 2));
				createRoomAtPoint(mouse.x, mouse.y).stickToMouse();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			pressedPoint = Optional.absent();
			for (MapRoom c : mapRooms) {
				c.mouseReleased(e);
			}
			repaint();
		}

	};

	private MouseMotionListener mousemMotionListener = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent e) {
			if (currentTool == Tool.DRAG_ROOM) {
				for (MapRoom c : mapRooms) {
					c.mouseDragged(e);
					repaint();
				}
				if (pressedPoint.isPresent()) {
					currentX = (int) (currentX + (pressedPoint.get().x - e
							.getPoint().x) / scale);
					currentY = (int) (currentY + (pressedPoint.get().y - e
							.getPoint().y) / scale);

					pressedPoint = Optional.of(e.getPoint());
					repaint();
				}
			}else if (currentTool == Tool.CREATE_ROOM){
				this.mouseMoved(e);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if(getStuckToMouse().isPresent()){
				MapRoom room = getStuckToMouse().get();
				
				room.setX((int) (getCurrentX() + e.getPoint().x/getScale() - room.getBounds().width/2));
				room.setY((int) (getCurrentY() + e.getPoint().y/getScale() - room.getBounds().height/2));
				repaint();
			}
		}
	};
	
	public void setCurrentTool(Tool currentTool) {
		this.currentTool = currentTool;
	}

	public Tool getCurrentTool() {
		return currentTool;
	}
	public MapRoom createRoomAtPoint(int x, int y) {
		MapRoom mapRoom = new MapRoom(this, new Point(x, y));
		addRoom(mapRoom);
		mapRoom.snapToGrid();
		return mapRoom;
	}

	public double getScale() {
		return scale;
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		super.paint(g);
		
		g.addRenderingHints( new RenderingHints( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON ));
		g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		g.setRenderingHint( RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY );
		
		
		g.scale(scale, scale);
		g.setColor(ColorPaletteConstants.MAP_ROOM_HOLDER);
		g.setStroke(new BasicStroke(2));
		for (int x = -(currentX % GRID_SIZE) - GRID_SIZE; x < getWidth()
				/ scale; x += GRID_SIZE) {
			for (int y = -(currentY % GRID_SIZE) - GRID_SIZE; y < getHeight()
					/ scale; y += GRID_SIZE) {
				g.drawRoundRect(x, y, GRID_SIZE - SPACING_SIZE, GRID_SIZE
						- SPACING_SIZE, MapRoom.CORNER_ROUNDNESS,
						MapRoom.CORNER_ROUNDNESS);
			}
		}
		g.translate(-currentX, -currentY);
		for (MapRoom c : mapRooms) {
			c.draw(g);
			if(currentTool == Tool.CREATE_HALLWAY){
				c.drawEmptyHallways(g);
			}
		}
		g.translate(currentX, currentY);
		g.scale(1 / scale, 1 / scale);

		g.setColor(Color.GREEN);
		g.drawString("X: " + currentX + " Y: " + currentY, 10, 20);
	}

	public int getCurrentX() {
		return currentX;
	}

	public int getCurrentY() {
		return currentY;
	}

	public Optional<MapRoom> getStuckToMouse() {
		return stuckToMouse;
	}
	public void addHallway(
			MapRoom first, MapRoom.HallwaySet.Direction firstDirection, 
			MapRoom second,	MapRoom.HallwaySet.Direction secondDirection) {
		
		MapHallway hallway = new MapHallway();
		first.getHallways().setHallway(firstDirection, Optional.of(hallway));
		second.getHallways().setHallway(secondDirection, Optional.of(hallway));
	}

	public void addRoom(MapRoom c) {
		this.mapRooms.add(c);
		repaint();
	}

	public void addAndCenterRoom(MapRoom c) {
		int x = (int) (currentX + (getWidth() / scale) / 2 - c.getBounds().width / 2);
		int y = (int) (currentY + (getHeight() / scale) / 2 - c.getBounds().height / 2);

		c.setLocation(x - x % GRID_SIZE, y - y % GRID_SIZE);
		this.mapRooms.add(c);
		repaint();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setStuckToMouse(Optional optional) {
		this.stuckToMouse = optional;
	}

	public void deleteStuckToMouse() {
		if(stuckToMouse.isPresent()){
			mapRooms.remove(stuckToMouse.get());		
			stuckToMouse = Optional.absent();
			repaint();
		}
	}


}

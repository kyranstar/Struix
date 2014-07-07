package ui.windows.map.main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

import ui.ColorPaletteConstants;
import ui.windows.UIButton;
import ui.windows.WindowComponent;
import ui.windows.map.MapHallway;
import ui.windows.map.MapRoom;

import com.google.common.base.Optional;

public class MapComponent extends WindowComponent {

	private static final long serialVersionUID = 1L;

	public static final int GRID_SIZE = 60;
	private static final int SPACING_SIZE = 10;

	private double scale = 1.0;

	private int currentX;
	private int currentY;

	private List<MapRoom> mapRooms = new LinkedList<MapRoom>();

	private Optional<MapRoom> stuckToMouse = Optional.absent();

	private Tool currentTool = Tool.DRAG_ROOM;

	public enum Tool {
		DRAG_ROOM, CREATE_ROOM, CREATE_HALLWAY;
	}

	public MapComponent() {
		super();
		setPreferredSize(new Dimension(200, 200));
		setSize(getPreferredSize());
		this.setBackground(ColorPaletteConstants.MAP_BACKGROUND);
		this.addMouseMotionListener(mouseListener);
		this.addMouseListener(mouseListener);
		this.addMouseWheelListener(mouseWheelListener);
		this.setOpaque(false);

		setLayout(new BorderLayout());

		UIButton button = new UIButton("Reset View", true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentX = currentY = 0;
				repaint();
			}
		});
		this.add(button, BorderLayout.EAST);
	}

	private MouseWheelListener mouseWheelListener = new MapMouseWheelListener(this);
	private MapMouseListener mouseListener = new MapMouseListener(this);

	public void setCurrentTool(Tool currentTool) {
		this.currentTool = currentTool;
	}

	public Tool getCurrentTool() {
		return currentTool;
	}

	public List<MapRoom> getMapRooms() {
		return mapRooms;
	}

	public void setMapRooms(List<MapRoom> mapRooms) {
		this.mapRooms = mapRooms;
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
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;

		g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

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
		for (MapRoom c : getMapRooms()) {
			c.draw(g);
			if (currentTool == Tool.CREATE_HALLWAY) {
				c.drawEmptyHallways(g);
			}
		}
		g.translate(currentX, currentY);
		g.scale(1 / scale, 1 / scale);

		g.setColor(Color.GREEN);
		g.drawString("X: " + currentX + " Y: " + currentY, 10, 20);

		super.paintComponent(g);
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public int getCurrentX() {
		return currentX;
	}

	public void setCurrentX(int d) {
		this.currentX = d;
	}

	public int getCurrentY() {
		return currentY;
	}

	public void setCurrentY(int d) {
		this.currentY = d;
	}

	public Optional<MapRoom> getStuckToMouse() {
		return stuckToMouse;
	}

	public void addHallway(MapRoom first,
			MapRoom.HallwaySet.Direction firstDirection, MapRoom second,
			MapRoom.HallwaySet.Direction secondDirection) {

		MapHallway hallway = new MapHallway();
		first.getHallways().setHallway(firstDirection, Optional.of(hallway));
		second.getHallways().setHallway(secondDirection, Optional.of(hallway));
	}

	public void addRoom(MapRoom room) {
		this.getMapRooms().add(room);
		repaint();
	}

	public void addAndCenterRoom(MapRoom c) {
		int x = (int) (currentX + (getWidth() / scale) / 2 - c.getBounds().width / 2);
		int y = (int) (currentY + (getHeight() / scale) / 2 - c.getBounds().height / 2);

		c.setLocation(x - x % GRID_SIZE, y - y % GRID_SIZE);
		this.getMapRooms().add(c);
		repaint();
	}

	public boolean twoRoomsInPosition(Point point) {
		boolean foundOne = false;
		for (MapRoom room : getMapRooms()) {
			if (room.getLocation().equals(point)) {
				if (!foundOne) {
					foundOne = true;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setStuckToMouse(Optional optional) {
		this.stuckToMouse = optional;
	}

	public void deleteStuckToMouse() {
		if (stuckToMouse.isPresent()) {
			getMapRooms().remove(stuckToMouse.get());
			stuckToMouse = Optional.absent();
			repaint();
		}
	}
}
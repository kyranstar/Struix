package ui.windows.map.main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import logic.creator.GameRoom;
import logic.creator.GameRoom.RoomHandler;
import logic.words.WordSet;
import ui.ColorPaletteConstants;
import ui.CreatorUI;
import ui.windows.UIButton;
import ui.windows.map.MapHallway;
import ui.windows.map.MapRoom;

import com.google.common.base.Optional;

public class MapComponent extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final int GRID_SIZE = 60;
	private static final int SPACING_SIZE = 10;

	private int currentX;
	private int currentY;

	private final List<MapRoom> mapRooms = new LinkedList<MapRoom>();

	private Optional<MapRoom> stuckToMouse = Optional.absent();

	private Tool currentTool = Tool.DRAG_ROOM;
	private final MapMouseListener mouseListener = new MapMouseListener(this);

	private final WordSet wordSet = new WordSet();
	private final RoomHandler roomBuilder = new RoomHandler(wordSet);

	private CreatorUI creatorUI;

	public enum Tool {
		DRAG_ROOM, CREATE_ROOM, CREATE_HALLWAY;
	}

	private String worldName;

	public MapComponent(String name, CreatorUI creatorUI) {
		super();
		this.setWorldName(name);
		this.setCreatorUI(creatorUI);
		setPreferredSize(new Dimension(200, 200));
		setSize(getPreferredSize());
		this.setBackground(ColorPaletteConstants.MAP_BACKGROUND);
		this.addMouseMotionListener(mouseListener);
		this.addMouseListener(mouseListener);
		this.setOpaque(false);

		setLayout(new BorderLayout());

		UIButton button = new UIButton("Reset View", true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentX = currentY = 0;
				repaint();
			}
		});
		JPanel buttonHolder = new JPanel();
		buttonHolder.add(button);
		buttonHolder.setOpaque(false);

		this.add(buttonHolder, BorderLayout.EAST);
	}

	public void setCurrentTool(Tool currentTool) {
		this.currentTool = currentTool;
		if(currentTool != Tool.CREATE_HALLWAY){
			mouseListener.hallwayToMouse = Optional.absent();
			mouseListener.pressedRoom = Optional.absent();
			mouseListener.pressedHallwayIndex = Optional.absent();
		}
	}

	public Tool getCurrentTool() {
		return currentTool;
	}

	public List<MapRoom> getMapRooms() {
		return mapRooms;
	}

	public MapRoom createRoomAtPoint(int xPos, int yPos) {
		MapRoom mapRoom = new MapRoom(this, new Point(xPos, yPos));
		addRoom(mapRoom);
		mapRoom.snapToGrid();
		return mapRoom;
	}

	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;

		g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(ColorPaletteConstants.MAP_ROOM_HOLDER);
		g.setStroke(new BasicStroke(2));
		for (int x = -(currentX % GRID_SIZE) - GRID_SIZE; x < getWidth(); x += GRID_SIZE) {
			for (int y = -(currentY % GRID_SIZE) - GRID_SIZE; y < getHeight(); y += GRID_SIZE) {
				g.drawRoundRect(x, y, GRID_SIZE - SPACING_SIZE, GRID_SIZE - SPACING_SIZE, MapRoom.CORNER_ROUNDNESS,
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
		if (this.mouseListener.hallwayToMouse.isPresent()) {
			g.setColor(Color.BLACK);
			g.draw(mouseListener.hallwayToMouse.get());
		}
		g.translate(currentX, currentY);

		g.setColor(Color.GREEN);
		g.drawString("X: " + currentX + " Y: " + currentY, 10, 20);

		super.paintComponent(g);
	}

	public int getCurrentX() {
		return currentX;
	}

	public void setCurrentX(int curentX) {
		this.currentX = curentX;
	}

	public int getCurrentY() {
		return currentY;
	}

	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}

	public Optional<MapRoom> getStuckToMouse() {
		return stuckToMouse;
	}

	public void addHallway(MapRoom first, GameRoom.HallwaySet.Direction firstDirection, MapRoom second,
			GameRoom.HallwaySet.Direction secondDirection) {

		MapHallway hallway = new MapHallway(first, firstDirection, second, secondDirection);
		first.getRoom().getHallways().setHallway(firstDirection, Optional.of(hallway));
		second.getRoom().getHallways().setHallway(secondDirection, Optional.of(hallway));
	}

	private void addRoom(MapRoom room) {
		this.getMapRooms().add(room);
		repaint();
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

	public RoomHandler getRoomBuilder() {
		return roomBuilder;
	}

	public CreatorUI getCreatorUI() {
		return creatorUI;
	}

	public void setCreatorUI(CreatorUI creatorUI) {
		this.creatorUI = creatorUI;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}
}

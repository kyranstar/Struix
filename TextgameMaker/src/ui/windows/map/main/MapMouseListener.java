package ui.windows.map.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.List;

import logic.creator.GameRoom.HallwaySet;
import logic.creator.GameRoom.HallwaySet.Direction;
import ui.windows.map.MapHallway;
import ui.windows.map.MapRoom;
import ui.windows.map.main.MapComponent.Tool;
import ui.windows.room.RoomDialogue;

import com.google.common.base.Optional;

class MapMouseListener implements MouseListener, MouseMotionListener {

	private final MapComponent parent;
	private Optional<Point> pressedPoint = Optional.absent();

	Optional<MapRoom> pressedRoom = Optional.absent();
	Optional<Integer> pressedHallwayIndex = Optional.absent();
	Optional<Line2D.Double> hallwayToMouse = Optional.absent();

	private Optional<MapRoom> selected = Optional.absent();

	MapMouseListener(MapComponent parent) {
		this.parent = parent;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		final Point mouse = e.getPoint();
		mouse.translate(getCurrentX(), getCurrentY());

		if (getCurrentTool() == Tool.DRAG_ROOM) {
			if (e.getClickCount() >= 2) {
				handleDoubleClickDragRoom(e, mouse);
			} else if (e.getClickCount() == 1) {
				handleSingleClickDragRoom(mouse);
			}
		}
		parent.repaint();
	}

	private void handleSingleClickDragRoom(Point mouse) {
		parent.setStuckToMouse(Optional.absent());
	}

	private void handleDoubleClickDragRoom(MouseEvent e, Point mouse) {
		for (final MapRoom room : getMapRooms()) {
			if (room.getBounds().contains(mouse)) {
				if (!room.hasRoomDialogueOpen()) {
					room.setHasRoomDialogueOpen(true);
					final RoomDialogue roomWindow = new RoomDialogue(room);
					parent.getCreatorUI().addWindow(roomWindow, room, roomWindow.getPreferredSize(),
							e.getLocationOnScreen(), room.getRoom().getName());
					deselectRoom();
				}
				return;
			}
		}
	}

	private void selectRoom(MapRoom room) {
		selected = Optional.of(room);
		room.setSelected(true);
	}

	private void deselectRoom() {
		if (selected.isPresent()) {
			selected.get().setSelected(false);
			selected = Optional.absent();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (getCurrentTool()) {
		case DRAG_ROOM:
			handleMousePressDragRoom(e);
			break;
		case CREATE_ROOM:
			handleMousePressCreateRoom(e);
			break;
		case CREATE_HALLWAY:
			handleMousePressCreateHallway(e);
			break;
		default:
			throw new UnhandledToolException(getCurrentTool() + " unhandled for mouse press");
		}
		parent.repaint();
	}

	private void handleMousePressDragRoom(MouseEvent e) {
		deselectRoom();
		for (final MapRoom c : getMapRooms()) {
			final Point mouse = e.getPoint();
			mouse.translate(getCurrentX(), getCurrentY());

			final Rectangle bounds = (Rectangle) c.getBounds().clone();

			if (bounds.contains(mouse)) {
				selectRoom(c);
				c.mousePressed(e);
				return;
			}
		}
		pressedPoint = Optional.of(e.getPoint());
	}

	private void handleMousePressCreateRoom(MouseEvent e) {
		if (parent.getStuckToMouse().isPresent()) {
			final MapRoom room = parent.getStuckToMouse().get();
			room.snapToGrid();
			if (twoRoomsInPosition(room.getLocation())) { return; }
		}
		// unstick to mouse
		parent.setStuckToMouse(Optional.absent());

		final Point mouse = e.getPoint();
		mouse.translate(getCurrentX() - MapRoom.DEFAULT_WIDTH / 2, getCurrentY() - MapRoom.DEFAULT_HEIGHT / 2);
		parent.createRoomAtPoint(mouse.x, mouse.y).stickToMouse();
	}

	private void handleMousePressCreateHallway(MouseEvent e) {
		for (final MapRoom c : getMapRooms()) {
			final Point mouse = e.getPoint();
			mouse.translate(getCurrentX(), getCurrentY());

			final Rectangle roomBounds = (Rectangle) c.getBounds().clone();
			if (roomBounds.contains(mouse)) {

				int index = 0;
				for (final Optional<MapHallway> hallway : c.getRoom().getHallways()) {
					if (!hallway.isPresent()) {
						final Direction dir = HallwaySet.Direction.valueOf(index);
						final Rectangle circleBounds = new Rectangle(dir.x, dir.y, MapRoom.EMPTY_HALLWAY_SIZE,
								MapRoom.EMPTY_HALLWAY_SIZE);
						circleBounds.x += roomBounds.x;
						circleBounds.y += roomBounds.y;
						if (circleBounds.contains(mouse)) {
							if (pressedRoom.isPresent()) {
								final Direction oneDir = Direction.valueOf(index);
								final Direction twoDir = Direction.valueOf(pressedHallwayIndex.get());

								parent.addHallway(c, oneDir, pressedRoom.get(), twoDir);

								pressedRoom = Optional.absent();
								pressedHallwayIndex = Optional.absent();
								hallwayToMouse = Optional.absent();
							} else {
								pressedHallwayIndex = Optional.of(index);
								pressedRoom = Optional.of(c);

								final int x = pressedRoom.get().getBounds().x
										+ Direction.valueOf(pressedHallwayIndex.get()).x + MapHallway.EMPTY_SIZE / 2;
								final int y = pressedRoom.get().getBounds().y
										+ Direction.valueOf(pressedHallwayIndex.get()).y + MapHallway.EMPTY_SIZE / 2;

								hallwayToMouse = Optional.of(new Line2D.Double(x, y, mouse.x, mouse.y));

							}
						}

					}
					index++;
				}
			}
		}
	}

	private boolean twoRoomsInPosition(Point position) {
		boolean foundOne = false;
		for (final MapRoom room : getMapRooms()) {
			if (room.getLocation().equals(position)) {
				if (!foundOne) {
					foundOne = true;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (getCurrentTool()) {
		case DRAG_ROOM:
			pressedPoint = Optional.absent();
			for (final MapRoom c : getMapRooms()) {
				if (c.getPressedPoint().isPresent()) {
					c.pressedPoint = Optional.absent();
					c.snapToGrid();
					if (twoRoomsInPosition(c.getLocation())) {
						c.stickToMouse();
					}
					return;
				}
			}
			break;
		case CREATE_ROOM:
			break;
		case CREATE_HALLWAY:
			break;
		default:
			throw new UnhandledToolException(getCurrentTool() + " unhandled for mouse release");
		}

		parent.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (getCurrentTool() == Tool.DRAG_ROOM) {
			for (final MapRoom c : getMapRooms()) {
				c.mouseDragged(e);
			}
			if (pressedPoint.isPresent()) {
				parent.setCurrentX(getCurrentX() + pressedPoint.get().x - e.getPoint().x);
				parent.setCurrentY(getCurrentY() + pressedPoint.get().y - e.getPoint().y);

				pressedPoint = Optional.of(e.getPoint());
			}
		} else if (getCurrentTool() == Tool.CREATE_ROOM) {
			mouseMoved(e);
		} else if (getCurrentTool() == Tool.CREATE_HALLWAY) {
			mouseMoved(e);
		}
		parent.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (parent.getStuckToMouse().isPresent()) {
			final MapRoom room = parent.getStuckToMouse().get();

			room.setX((int) (getCurrentX() + (double) e.getPoint().x - room.getBounds().width / 2));
			room.setY((int) (getCurrentY() + (double) e.getPoint().y - room.getBounds().height / 2));
		} else if (pressedHallwayIndex.isPresent()) {
			final Point mouse = e.getPoint();
			mouse.translate(getCurrentX(), getCurrentY());

			final int x = pressedRoom.get().getBounds().x + Direction.valueOf(pressedHallwayIndex.get()).x
					+ MapHallway.EMPTY_SIZE / 2;
			final int y = pressedRoom.get().getBounds().y + Direction.valueOf(pressedHallwayIndex.get()).y
					+ MapHallway.EMPTY_SIZE / 2;

			hallwayToMouse = Optional.of(new Line2D.Double(x, y, mouse.x, mouse.y));
		}
		parent.repaint();
	}

	private int getCurrentX() {
		return parent.getCurrentX();
	}

	private int getCurrentY() {
		return parent.getCurrentY();
	}

	private List<MapRoom> getMapRooms() {
		return parent.getMapRooms();
	}

	private Tool getCurrentTool() {
		return parent.getCurrentTool();
	}
}

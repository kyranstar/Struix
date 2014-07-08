package ui.windows.map.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;

import ui.windows.map.MapHallway;
import ui.windows.map.MapRoom;
import ui.windows.map.MapRoom.HallwaySet;
import ui.windows.map.MapRoom.HallwaySet.Direction;
import ui.windows.map.main.MapComponent.Tool;

import com.google.common.base.Optional;

class MapMouseListener implements MouseListener, MouseMotionListener {

	private MapComponent parent;
	private Optional<Point> pressedPoint = Optional.absent();

	private Optional<MapRoom> pressedRoom = Optional.absent();
	private Optional<Integer> pressedHallwayIndex = Optional.absent();
	Optional<Line2D.Double> hallwayToMouse = Optional.absent();

	MapMouseListener(MapComponent parent) {
		this.parent = parent;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (parent.getCurrentTool() == Tool.DRAG_ROOM) {
			if (e.getClickCount() >= 2) {
				for (MapRoom room : parent.getMapRooms()) {
					if (room.getBounds().contains(e.getPoint())) {
						// OPEN UP ROOM DIALOGUE
						return;
					}
				}
			} else if (e.getClickCount() == 1) {
				parent.setStuckToMouse(Optional.absent());
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (parent.getCurrentTool()) {
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
			throw new UnhandledToolException(parent.getCurrentTool()
					+ " unhandled for mouse press");
		}
	}

	private void handleMousePressDragRoom(MouseEvent e) {
		for (MapRoom c : parent.getMapRooms()) {
			Point mouse = e.getPoint();
			mouse.translate((int) (parent.getCurrentX() * parent.getScale()),
					(int) (parent.getCurrentY() * parent.getScale()));

			Rectangle bounds = (Rectangle) c.getBounds().clone();
			bounds.x *= parent.getScale();
			bounds.y *= parent.getScale();
			bounds.width *= parent.getScale();
			bounds.height *= parent.getScale();

			if (bounds.contains(mouse)) {
				c.mousePressed(e);
				return;
			}
		}

		pressedPoint = Optional.of(e.getPoint());
	}

	private void handleMousePressCreateRoom(MouseEvent e) {
		if (parent.getStuckToMouse().isPresent()) {
			MapRoom room = parent.getStuckToMouse().get();
			room.snapToGrid();
			if (twoRoomsInPosition(room.getLocation())) {
				return;
			}
		}
		// unstick to mouse
		parent.setStuckToMouse(Optional.absent());

		Point mouse = e.getPoint();
		mouse.translate(
				(int) (parent.getCurrentX() / parent.getScale() - MapRoom.DEFAULT_WIDTH / 2),
				(int) (parent.getCurrentY() / parent.getScale() - MapRoom.DEFAULT_HEIGHT / 2));
		parent.createRoomAtPoint(mouse.x, mouse.y).stickToMouse();
	}

	private void handleMousePressCreateHallway(MouseEvent e) {
		for (MapRoom c : parent.getMapRooms()) {
			Point mouse = e.getPoint();
			mouse.translate((int) (parent.getCurrentX() * parent.getScale()),
					(int) (parent.getCurrentY() * parent.getScale()));

			Rectangle roomBounds = (Rectangle) c.getBounds().clone();
			roomBounds.x *= parent.getScale();
			roomBounds.y *= parent.getScale();
			roomBounds.width *= parent.getScale();
			roomBounds.height *= parent.getScale();
			if (roomBounds.contains(mouse)) {

				int index = 0;
				for (Optional<MapHallway> hallway : c.getHallways()) {
					if (!hallway.isPresent()) {
						Direction dir = HallwaySet.Direction.valueOf(index);
						Rectangle circleBounds = new Rectangle(dir.x, dir.y,
								MapRoom.EMPTY_HALLWAY_SIZE,
								MapRoom.EMPTY_HALLWAY_SIZE);
						circleBounds.x += roomBounds.x;
						circleBounds.y += roomBounds.y;
						circleBounds.x *= parent.getScale();
						circleBounds.y *= parent.getScale();
						circleBounds.width *= parent.getScale();
						circleBounds.height *= parent.getScale();
						if (circleBounds.contains(mouse)) {
							if (this.pressedRoom.isPresent()) {
								Direction oneDir = Direction.valueOf(index);
								Direction twoDir = Direction
										.valueOf(pressedHallwayIndex.get());

								parent.addHallway(c, oneDir, pressedRoom.get(),
										twoDir);

								this.pressedRoom = Optional.absent();
								pressedHallwayIndex = Optional.absent();
								this.hallwayToMouse = Optional.absent();
							} else {
								pressedHallwayIndex = Optional.of(index);
								this.pressedRoom = Optional.of(c);
							}
						}

					}
					index++;
				}
			}
		}
	}

	private boolean twoRoomsInPosition(Point point) {
		boolean foundOne = false;
		for (MapRoom room : parent.getMapRooms()) {
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

	@Override
	public void mouseReleased(MouseEvent e) {
		pressedPoint = Optional.absent();
		for (MapRoom c : parent.getMapRooms()) {
			c.mouseReleased(e);
		}
		parent.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (parent.getCurrentTool() == Tool.DRAG_ROOM) {
			for (MapRoom c : parent.getMapRooms()) {
				c.mouseDragged(e);
				parent.repaint();
			}
			if (pressedPoint.isPresent()) {
				parent.setCurrentX((int) (parent.getCurrentX() + (pressedPoint
						.get().x - e.getPoint().x) / parent.getScale()));
				parent.setCurrentY((int) (parent.getCurrentY() + (pressedPoint
						.get().y - e.getPoint().y) / parent.getScale()));

				pressedPoint = Optional.of(e.getPoint());
				parent.repaint();
			}
		} else if (parent.getCurrentTool() == Tool.CREATE_ROOM) {
			this.mouseMoved(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (parent.getStuckToMouse().isPresent()) {
			MapRoom room = parent.getStuckToMouse().get();

			room.setX((int) (parent.getCurrentX() + e.getPoint().x
					/ parent.getScale() - room.getBounds().width / 2));
			room.setY((int) (parent.getCurrentY() + e.getPoint().y
					/ parent.getScale() - room.getBounds().height / 2));
			parent.repaint();
		} else if (this.pressedHallwayIndex.isPresent()) {
			Point mouse = e.getPoint();
			mouse.translate((int) (parent.getCurrentX() * parent.getScale()),
					(int) (parent.getCurrentY() * parent.getScale()));

			int x = pressedRoom.get().getBounds().x
					+ Direction.valueOf(pressedHallwayIndex.get()).x
					+ MapHallway.EMPTY_SIZE / 2;
			int y = pressedRoom.get().getBounds().y
					+ Direction.valueOf(pressedHallwayIndex.get()).y
					+ MapHallway.EMPTY_SIZE / 2;

			this.hallwayToMouse = Optional.of(new Line2D.Double(x, y, mouse.x,
					mouse.y));
			parent.repaint();
		}
	}

}

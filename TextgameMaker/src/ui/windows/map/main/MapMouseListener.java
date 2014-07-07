package ui.windows.map.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import ui.windows.map.MapRoom;
import ui.windows.map.main.MapComponent.Tool;

import com.google.common.base.Optional;

public class MapMouseListener implements MouseListener, MouseMotionListener{
	
	private MapComponent parent;
	private Optional<Point> pressedPoint = Optional.absent();

	public MapMouseListener(MapComponent parent) {
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
		if (parent.getCurrentTool() == Tool.DRAG_ROOM) {
			for (MapRoom c : parent.getMapRooms()) {
				Point mouse = e.getPoint();
				mouse.translate(
						(int) (parent.getCurrentX() * parent.getScale()),
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
		} else if (parent.getCurrentTool() == Tool.CREATE_ROOM) {
			if (parent.getStuckToMouse().isPresent()) {
				MapRoom room = parent.getStuckToMouse().get();
				room.snapToGrid();
				if (parent.twoRoomsInPosition(room.getLocation())) {
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
				parent.setCurrentX((int) (parent.getCurrentX() + (pressedPoint.get().x - e
						.getPoint().x) / parent.getScale()));
				parent.setCurrentY((int) (parent.getCurrentY() + (pressedPoint.get().y - e
						.getPoint().y) / parent.getScale()));

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

			room.setX((int) (parent.getCurrentX() + e.getPoint().x / parent.getScale() - room
					.getBounds().width / 2));
			room.setY((int) (parent.getCurrentY() + e.getPoint().y / parent.getScale() - room
					.getBounds().height / 2));
			parent.repaint();
		}
	}

}

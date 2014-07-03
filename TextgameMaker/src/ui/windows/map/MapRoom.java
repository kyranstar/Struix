package ui.windows.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Optional;

public class MapRoom {

	private static final int DEFAULT_WIDTH = 50;
	private static final int DEFAULT_HEIGHT = 50;

	public static final int CORNER_ROUNDNESS = 10;

	private Rectangle bounds;
	private Color backgroundColor;

	private HallwaySet hallways = new HallwaySet();

	public MapRoom(Color background, Point position) {
		bounds = new Rectangle(position.x, position.y, DEFAULT_WIDTH,
				DEFAULT_HEIGHT);
		this.backgroundColor = background;
	}

	public HallwaySet getHallways() {
		return hallways;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public Point getLocation() {
		return new Point(bounds.x, bounds.y);
	}

	public void setLocation(int x, int y) {
		this.bounds.x = x;
		this.bounds.y = y;
	}

	public void draw(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		for (Optional<MapHallway> hallway : hallways) {
			hallway.ifPresent(h -> h.draw(g));
		}
	}

	private Optional<Point> pressedPoint = Optional.empty();

	public void mousePressed(MouseEvent e) {
		pressedPoint = Optional.of(e.getPoint());
	}

	public void mouseReleased(MouseEvent e) {
		if (pressedPoint.isPresent()) {
			// Round to nearest grid box
			if (bounds.x % MapComponent.GRID_SIZE < MapComponent.GRID_SIZE / 2) {
				this.bounds.x = bounds.x - (bounds.x % MapComponent.GRID_SIZE);
			} else {
				this.bounds.x = bounds.x - (bounds.x % MapComponent.GRID_SIZE)
						+ MapComponent.GRID_SIZE;
			}
			if (bounds.y % MapComponent.GRID_SIZE < MapComponent.GRID_SIZE / 2) {
				this.bounds.y = bounds.y - (bounds.y % MapComponent.GRID_SIZE);
			} else {
				this.bounds.y = bounds.y - (bounds.y % MapComponent.GRID_SIZE)
						+ MapComponent.GRID_SIZE;
			}
		}

		pressedPoint = Optional.empty();
	}

	// REPAINT after calling this method
	public void mouseDragged(MouseEvent e) {
		if (!pressedPoint.isPresent()) {
			return;
		}

		this.bounds.x = bounds.x - pressedPoint.get().x + e.getPoint().x;
		this.bounds.y = bounds.y - pressedPoint.get().y + e.getPoint().y;
		pressedPoint = Optional.of(e.getPoint());
	}

	static class HallwaySet implements Iterable<Optional<MapHallway>> {
		private static final int DIRECTIONS = 8;

		public final static int NORTH = 0;
		public final static int NORTH_EAST = 1;
		public final static int EAST = 2;
		public final static int SOUTH_EAST = 3;
		public final static int SOUTH = 4;
		public final static int SOUTH_WEST = 5;
		public final static int WEST = 6;
		public final static int NORTH_WEST = 7;

		@SuppressWarnings("unchecked")
		private Optional<MapHallway>[] hallways = (Optional<MapHallway>[]) new Optional[DIRECTIONS];

		public HallwaySet() {
			for (int i = 0; i < hallways.length; i++) {
				hallways[i] = Optional.empty();
			}
		}

		public void setHallway(int direction, Optional<MapHallway> hallway) {
			this.hallways[direction] = hallway;
		}

		public Optional<MapHallway> getHallway(int direction) {
			return this.hallways[direction];
		}

		@Override
		public Iterator<Optional<MapHallway>> iterator() {
			Iterator<Optional<MapHallway>> it = new Iterator<Optional<MapHallway>>() {

				private int currentIndex = 0;

				@Override
				public boolean hasNext() {
					return currentIndex < DIRECTIONS;
				}

				@Override
				public Optional<MapHallway> next() {
					return hallways[currentIndex++];
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
			return it;
		}
	}

}

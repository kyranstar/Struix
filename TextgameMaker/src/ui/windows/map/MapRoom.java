package ui.windows.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import ui.ColorUtils;

import com.google.common.base.Optional;

public class MapRoom {

	public static final int DEFAULT_WIDTH = 50;
	public static final int DEFAULT_HEIGHT = 50;

	public static final int CORNER_ROUNDNESS = 10;

	private Rectangle bounds;
	private Color backgroundColor;

	private HallwaySet hallways = new HallwaySet();

	private boolean stuckToMouse = false;
	
	private MapComponent parent;
	
	public MapRoom(MapComponent parent, Color background, Point position) {
		this.parent = parent;
		bounds = new Rectangle(position.x, position.y, DEFAULT_WIDTH,
				DEFAULT_HEIGHT);
		this.backgroundColor = background;
	}
	public MapRoom(MapComponent parent, Point position) {
		this.parent = parent;
		bounds = new Rectangle(position.x, position.y, DEFAULT_WIDTH,
				DEFAULT_HEIGHT);
		this.backgroundColor = ColorUtils.mapOpacity(ColorUtils.mapBrightness(ColorUtils.mapSaturation(ColorUtils.generateRandomColor(new Color(255,50,50)), 0.7f), 0.9f),0.7f);
	}

	public void setX(int x){
		this.bounds.x = x;
	}
	public void setY(int y){
		this.bounds.y = y;
	}

	public void setLocation(int x, int y) {
		this.bounds.x = x;
		this.bounds.y = y;
	}

	public void setHeight(int height){
		this.bounds.height = height;
	}
	public void setWidth(int width){
		this.bounds.width = width;
	}
	
	public void stickToMouse(){
		stuckToMouse = true;
	}
	public void unstickToMouse(){
		stuckToMouse = false;
		snapToGrid();
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

	public void draw(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, CORNER_ROUNDNESS, CORNER_ROUNDNESS);

		for (Optional<MapHallway> hallway : hallways) {
			if(hallway.isPresent()){
				hallway.get().draw(g);
			}
		}
	}

	private Optional<Point> pressedPoint = Optional.absent();

	public void mousePressed(MouseEvent e) {
		pressedPoint = Optional.of(e.getPoint());
	}

	public void mouseReleased(MouseEvent e) {
		if (pressedPoint.isPresent()) {
			snapToGrid();
		}

		pressedPoint = Optional.absent();
	}

	// REPAINT after calling this method
	public void mouseDragged(MouseEvent e) {
		if (!pressedPoint.isPresent()) {
			return;
		}
		System.out.println(pressedPoint.get().x);
		setX( (int) Math.round(bounds.x - (pressedPoint.get().x - e.getPoint().x)/parent.getScale()));
		setY( (int) Math.round(bounds.y - (pressedPoint.get().y - e.getPoint().y)/parent.getScale()));
		pressedPoint = Optional.of(e.getPoint());
	}

	public void snapToGrid(){
		if (bounds.x % MapComponent.GRID_SIZE < MapComponent.GRID_SIZE / 2) {
			setX(bounds.x - (bounds.x % MapComponent.GRID_SIZE));
		} else {
			setX(bounds.x - (bounds.x % MapComponent.GRID_SIZE)
					+ MapComponent.GRID_SIZE);
		}
		if (bounds.y % MapComponent.GRID_SIZE < MapComponent.GRID_SIZE / 2) {
			setY(bounds.y - (bounds.y % MapComponent.GRID_SIZE));
		} else {
			setY(bounds.y - (bounds.y % MapComponent.GRID_SIZE)
					+ MapComponent.GRID_SIZE);
		}
	}

	public void mouseMoved(MouseEvent e, int currentX, int currentY, double scale) {
		if(stuckToMouse){
			setX((int) (currentX + e.getPoint().x/scale - this.bounds.width/2));
			setY((int) (currentY + e.getPoint().y/scale - this.bounds.height/2));
		}
	}
	
	static class HallwaySet implements Iterable<Optional<MapHallway>> {

		public enum Direction{
			NORTH(0),
			NORTH_EAST(1),
			EAST(2),
			SOUTH_EAST(3),
			SOUTH(4),
			SOUTH_WEST(5),
			WEST(6),
			NORTH_WEST(7);
			
			
			int index;
			private Direction(int num){
				this.index = num;
			}
		}

		@SuppressWarnings("unchecked")
		private Optional<MapHallway>[] hallways = (Optional<MapHallway>[]) new Optional[Direction.values().length];

		public HallwaySet() {
			for (int i = 0; i < hallways.length; i++) {
				hallways[i] = Optional.absent();
			}
		}

		public void setHallway(Direction direction, Optional<MapHallway> hallway) {
			this.hallways[direction.index] = hallway;
		}

		public Optional<MapHallway> getHallway(Direction direction) {
			return this.hallways[direction.index];
		}

		@Override
		public Iterator<Optional<MapHallway>> iterator() {
			Iterator<Optional<MapHallway>> it = new Iterator<Optional<MapHallway>>() {

				private int currentIndex = 0;

				@Override
				public boolean hasNext() {
					return currentIndex < Direction.values().length;
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

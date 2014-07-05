package ui.windows.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import ui.ColorUtils;
import ui.windows.map.MapRoom.HallwaySet.Direction;

import com.google.common.base.Optional;

public class MapRoom {

	public static final int DEFAULT_WIDTH = 50;
	public static final int DEFAULT_HEIGHT = 50;

	public static final int CORNER_ROUNDNESS = 10;
	
	private Rectangle bounds;
	private Color backgroundColor;

	private HallwaySet hallways = new HallwaySet();
	
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
		if(parent.getStuckToMouse().isPresent())
			throw new AlreadyExistsException("Mouse already has a room stuck to it!");

		parent.setStuckToMouse(Optional.of(this));
	}
	public void unstickToMouse(){
		if(!parent.getStuckToMouse().isPresent())
			throw new AlreadyExistsException("Mouse doesnt have a room stuck to it!");
		if(parent.getStuckToMouse().get() != this){
			throw new AlreadyExistsException("Room stuck to mouse is not this");
		}

		parent.setStuckToMouse(Optional.absent());
		snapToGrid();
	}
	public void deleteStuckPiece(){
		
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
	public void drawEmptyHallways(Graphics2D g) {
		g.translate(this.getBounds().x, this.getBounds().y);
		g.setColor(MapHallway.EMPTY_CIRCLE_COLOR);
		int index = 0;
		for (Optional<MapHallway> hallway : hallways) {
			if(!hallway.isPresent()){
				Direction dir = HallwaySet.Direction.valueOf(index);
				g.drawOval(dir.x, dir.y, 10, 10);
			}
			index++;
		}
		g.translate(-this.getBounds().x, -this.getBounds().y);
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
	static class HallwaySet implements Iterable<Optional<MapHallway>> {

		private static final int X_LEFT = 0;
		private static final int X_CENTER = MapRoom.DEFAULT_WIDTH/2-MapHallway.EMPTY_SIZE/2;
		private static final int X_RIGHT = MapRoom.DEFAULT_WIDTH-MapHallway.EMPTY_SIZE;
		
		private static final int Y_TOP = 0;
		private static final int Y_CENTER = MapRoom.DEFAULT_HEIGHT/2 - MapHallway.EMPTY_SIZE/2;
		private static final int Y_BOTTOM = MapRoom.DEFAULT_HEIGHT - MapHallway.EMPTY_SIZE;
		
		public enum Direction{
			
			NORTH(0, X_CENTER, Y_TOP),
			NORTH_EAST(1, X_RIGHT, Y_TOP),
			EAST(2, X_RIGHT, Y_CENTER),
			SOUTH_EAST(3, X_RIGHT, Y_BOTTOM),
			SOUTH(4, X_CENTER, Y_BOTTOM),
			SOUTH_WEST(5, X_LEFT, Y_BOTTOM),
			WEST(6, X_LEFT, Y_CENTER),
			NORTH_WEST(7, X_LEFT, Y_TOP);
			
			final int index;
			final int x, y;
			
			public static Direction valueOf(int index){
				for(Direction dir : values()){
					if(dir.index == index){
						return dir;
					}
				}
				throw new IndexOutOfBoundsException("Index out of range 0-7, was " + index);
			}
			
			private Direction(int num, int x, int y){
				this.index = num;
				this.x = x;
				this.y = y;
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

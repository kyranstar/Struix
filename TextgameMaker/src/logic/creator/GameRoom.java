package logic.creator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import logic.object.GameObject;
import logic.words.Word;
import logic.words.WordSet;
import ui.windows.map.MapHallway;
import ui.windows.map.MapRoom;

import com.google.common.base.Optional;

public class GameRoom {
	//GUI
	private Color backgroundColor;

	//Game
	private Word name;
	private String nameGiven;
	private List<GameObject> objects;
	
	private HallwaySet hallways = new HallwaySet();
	private RoomHandler parent;

	private GameRoom(RoomHandler parent, String nameGiven, Word word, Color backgroundColor){
		this.parent = parent;
		this.setName(nameGiven);
		this.name = word;
		this.backgroundColor = backgroundColor;
		this.objects = new ArrayList<GameObject>();
	}
	
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public HallwaySet getHallways() {
		return hallways;
	}

	public void setHallways(HallwaySet hallways) {
		this.hallways = hallways;
	}
	
	public String getName() {
		return nameGiven;
	}

	public void setName(String nameGiven) {
		this.nameGiven = nameGiven;
		this.name = parent.wordSet.getMeaning(nameGiven);
	}

	public static class RoomHandler{
		private WordSet wordSet;
		public RoomHandler(WordSet words){
			this.wordSet = words;
		}
		
		public GameRoom createRoom(String name, Color colorInMap){
			return new GameRoom(this, name, wordSet.getMeaning(name), colorInMap);
		}
	}
	public static class HallwaySet implements Iterable<Optional<MapHallway>> {

		private static final int X_LEFT = 0;
		private static final int X_CENTER = MapRoom.DEFAULT_WIDTH / 2
				- MapHallway.EMPTY_SIZE / 2;
		private static final int X_RIGHT = MapRoom.DEFAULT_WIDTH
				- MapHallway.EMPTY_SIZE;

		private static final int Y_TOP = 0;
		private static final int Y_CENTER = MapRoom.DEFAULT_HEIGHT / 2
				- MapHallway.EMPTY_SIZE / 2;
		private static final int Y_BOTTOM = MapRoom.DEFAULT_HEIGHT
				- MapHallway.EMPTY_SIZE;

		public enum Direction {

			NORTH(0, X_CENTER, Y_TOP), NORTH_EAST(1, X_RIGHT, Y_TOP), EAST(2,
					X_RIGHT, Y_CENTER), SOUTH_EAST(3, X_RIGHT, Y_BOTTOM), SOUTH(
					4, X_CENTER, Y_BOTTOM), SOUTH_WEST(5, X_LEFT, Y_BOTTOM), WEST(
					6, X_LEFT, Y_CENTER), NORTH_WEST(7, X_LEFT, Y_TOP);

			public final int index;
			public final int x;
			public final int y;

			public static Direction valueOf(int index) {
				for (Direction dir : values()) {
					if (dir.index == index) {
						return dir;
					}
				}
				throw new IndexOutOfBoundsException(
						"Index out of range 0-7, was " + index);
			}

			private Direction(int num, int x, int y) {
				this.index = num;
				this.x = x;
				this.y = y;
			}
		}

		@SuppressWarnings("unchecked")
		private Optional<MapHallway>[] hallways = (Optional<MapHallway>[]) new Optional[Direction
				.values().length];

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
					if(currentIndex >= hallways.length){
						throw new NoSuchElementException();
					}
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

package ui.windows.map;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import logic.creator.GameRoom;
import logic.creator.GameRoom.HallwaySet;
import logic.creator.GameRoom.HallwaySet.Direction;
import ui.ColorPaletteConstants;
import ui.ColorUtils;
import ui.windows.map.main.MapComponent;

import com.google.common.base.Optional;

public class MapRoom {

	public static final int DEFAULT_WIDTH = 50;
	public static final int DEFAULT_HEIGHT = 50;

	public static final int CORNER_ROUNDNESS = 10;

	public static final int EMPTY_HALLWAY_SIZE = 10;

	private final Rectangle bounds;

	private final MapComponent parent;

	private final GameRoom room;

	private boolean hasRoomDialogueOpen;
	private boolean selected = false;

	public MapRoom(MapComponent parent, Color background, Point position) {
		room = parent.getRoomBuilder().createRoom("", background);

		this.parent = parent;
		bounds = new Rectangle(position.x, position.y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public MapRoom(MapComponent parent, Point position) {
		this.parent = parent;
		bounds = new Rectangle(position.x, position.y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Color color;

		if (ColorPaletteConstants.ROOM_RANDOM_COLOR) {
			color = ColorUtils.mapBrightness(ColorUtils.mapSaturation(
					ColorUtils.generateRandomColor(ColorPaletteConstants.ROOM_DEFAULT_COLOR),
					ColorPaletteConstants.ROOM_DEFAULT_COLOR_SATURATION),
					ColorPaletteConstants.ROOM_DEFAULT_COLOR_BRIGHTNESS);
		} else {
			color = ColorPaletteConstants.ROOM_DEFAULT_COLOR;
		}
		room = parent.getRoomBuilder().createRoom("Room Name", color);
	}

	public void setX(int x) {
		bounds.x = x;
	}

	public void setY(int y) {
		bounds.y = y;
	}

	public void setLocation(int x, int y) {
		bounds.x = x;
		bounds.y = y;
	}

	public GameRoom getRoom() {
		return room;
	}

	public void setHeight(int height) {
		bounds.height = height;
	}

	public void setWidth(int width) {
		bounds.width = width;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void stickToMouse() {
		if (!parent.getStuckToMouse().isPresent()) {
			parent.setStuckToMouse(Optional.of(this));
		}
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public Point getLocation() {
		return new Point(bounds.x, bounds.y);
	}

	public void draw(Graphics g) {
		if (isSelected()) {
			// this is to avoid little corner blanks
			final double amountInward = (bounds.getWidth() + bounds.getHeight()) / 10;

			final double x = bounds.getX() + amountInward;
			final double y = bounds.getY() + amountInward;
			drawSelection((Graphics2D) g,
					new Rectangle2D.Double(x, y, bounds.getWidth() - amountInward * 2, bounds.getHeight()
							- amountInward * 2), 30);
		}
		g.setColor(room.getBackgroundColor());
		g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, CORNER_ROUNDNESS, CORNER_ROUNDNESS);

		for (final Optional<MapHallway> hallway : room.getHallways()) {
			if (hallway.isPresent()) {
				hallway.get().draw(g);
			}
		}
	}

	private void drawSelection(Graphics2D g, Rectangle2D r, double s) {
		final Color c0 = ColorUtils.mapBrightness(getRoom().getBackgroundColor(), 0.9f);
		final Color c1 = new Color(0, 0, 0, 0);

		final double x0 = r.getMinX();
		final double y0 = r.getMinY();
		final double x1 = r.getMaxX();
		final double y1 = r.getMaxY();
		final double w = r.getWidth();
		final double h = r.getHeight();

		// Left
		g.setPaint(new GradientPaint(new Point2D.Double(x0, y0), c0, new Point2D.Double(x0 - s, y0), c1));
		g.fill(new Rectangle2D.Double(x0 - s, y0, s, h));

		// Right
		g.setPaint(new GradientPaint(new Point2D.Double(x1, y0), c0, new Point2D.Double(x1 + s, y0), c1));
		g.fill(new Rectangle2D.Double(x1, y0, s, h));

		// Top
		g.setPaint(new GradientPaint(new Point2D.Double(x0, y0), c0, new Point2D.Double(x0, y0 - s), c1));
		g.fill(new Rectangle2D.Double(x0, y0 - s, w, s));

		// Bottom
		g.setPaint(new GradientPaint(new Point2D.Double(x0, y1), c0, new Point2D.Double(x0, y1 + s), c1));
		g.fill(new Rectangle2D.Double(x0, y1, w, s));

		final float fractions[] = new float[] { 0.0f, 1.0f };
		final Color colors[] = new Color[] { c0, c1 };

		// Top Left
		g.setPaint(new RadialGradientPaint(new Rectangle2D.Double(x0 - s, y0 - s, s + s, s + s), fractions, colors,
				CycleMethod.NO_CYCLE));
		g.fill(new Rectangle2D.Double(x0 - s, y0 - s, s, s));

		// Top Right
		g.setPaint(new RadialGradientPaint(new Rectangle2D.Double(x1 - s, y0 - s, s + s, s + s), fractions, colors,
				CycleMethod.NO_CYCLE));
		g.fill(new Rectangle2D.Double(x1, y0 - s, s, s));

		// Bottom Left
		g.setPaint(new RadialGradientPaint(new Rectangle2D.Double(x0 - s, y1 - s, s + s, s + s), fractions, colors,
				CycleMethod.NO_CYCLE));
		g.fill(new Rectangle2D.Double(x0 - s, y1, s, s));

		// Bottom Right
		g.setPaint(new RadialGradientPaint(new Rectangle2D.Double(x1 - s, y1 - s, s + s, s + s), fractions, colors,
				CycleMethod.NO_CYCLE));
		g.fill(new Rectangle2D.Double(x1, y1, s, s));
	}

	public void drawEmptyHallways(Graphics2D g) {
		g.translate(getBounds().x, getBounds().y);
		g.setColor(MapHallway.EMPTY_CIRCLE_COLOR);
		for (int index = 0; index < Direction.values().length; index++) {
			final Direction dir = HallwaySet.Direction.valueOf(index);
			g.drawOval(dir.x, dir.y, EMPTY_HALLWAY_SIZE, EMPTY_HALLWAY_SIZE);
		}
		g.translate(-getBounds().x, -getBounds().y);
	}

	public Optional<Point> pressedPoint = Optional.absent();

	public void mousePressed(MouseEvent e) {
		setPressedPoint(Optional.of(e.getPoint()));
	}

	public void mouseDragged(MouseEvent e) {
		if (!getPressedPoint().isPresent()) { return; }

		setX((int) Math.round(bounds.getX() - getPressedPoint().get().getX() + e.getPoint().getX()));
		setY((int) Math.round(bounds.getY() - getPressedPoint().get().getY() + e.getPoint().getY()));
		setPressedPoint(Optional.of(e.getPoint()));
		parent.repaint();
	}

	public void snapToGrid() {
		if (bounds.x % MapComponent.GRID_SIZE < MapComponent.GRID_SIZE / 2) {
			setX(bounds.x - bounds.x % MapComponent.GRID_SIZE);
		} else {
			setX(bounds.x - bounds.x % MapComponent.GRID_SIZE + MapComponent.GRID_SIZE);
		}
		if (bounds.y % MapComponent.GRID_SIZE < MapComponent.GRID_SIZE / 2) {
			setY(bounds.y - bounds.y % MapComponent.GRID_SIZE);
		} else {
			setY(bounds.y - bounds.y % MapComponent.GRID_SIZE + MapComponent.GRID_SIZE);
		}
	}

	public Optional<Point> getPressedPoint() {
		return pressedPoint;
	}

	private void setPressedPoint(Optional<Point> optional) {
		pressedPoint = optional;
	}

	public boolean hasRoomDialogueOpen() {
		return hasRoomDialogueOpen;
	}

	public void setHasRoomDialogueOpen(boolean hasWindowOpen) {
		hasRoomDialogueOpen = hasWindowOpen;
	}

}

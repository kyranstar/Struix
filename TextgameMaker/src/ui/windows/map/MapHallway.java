package ui.windows.map;

import java.awt.Color;
import java.awt.Graphics;

import ui.windows.map.MapRoom.HallwaySet.Direction;

public class MapHallway {

	public static final int EMPTY_SIZE = 10;
	public static final Color EMPTY_CIRCLE_COLOR = new Color(50,50,50,200);
	
	private MapRoom one, two;
	private Direction oneDir, twoDir;
	
	public MapHallway(MapRoom one, Direction oneDir, MapRoom two, Direction twoDir){
		this.one = one;
		this.two = two;
		this.oneDir = oneDir;
		this.twoDir = twoDir;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(one.getBounds().x + oneDir.x + EMPTY_SIZE/2, 
					one.getBounds().y + oneDir.y+ EMPTY_SIZE/2, 
					two.getBounds().x + twoDir.x+ EMPTY_SIZE/2, 
					two.getBounds().y + twoDir.y+ EMPTY_SIZE/2);
	}

}

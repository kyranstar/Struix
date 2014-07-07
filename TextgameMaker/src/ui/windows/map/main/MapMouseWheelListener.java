package ui.windows.map.main;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MapMouseWheelListener implements MouseWheelListener{
	private MapComponent parent;


	private static final double MAX_SCALE = 3.0;
	private static final double MIN_SCALE = 0.25;

	public MapMouseWheelListener(MapComponent parent){
		this.parent = parent;
	}
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// going inward
		if (e.getWheelRotation() < 0) {
			if (parent.getScale() < MAX_SCALE) {

				if (parent.getScale() <= 1)
					parent.setScale(parent.getScale() * 1.5);
				else
					parent.setScale(parent.getScale() + 0.25);

				// hack that seems to make it zoom to center
				parent.setCurrentX((int) (parent.getCurrentX() + parent.getWidth() / parent.getScale() / 4));
				parent.setCurrentY((int) (parent.getCurrentY() + parent.getHeight() / parent.getScale() / 4));
			}
		} else {
			if (parent.getScale() > MIN_SCALE) {
				if (parent.getScale() <= 1)
					parent.setScale(parent.getScale() / 1.5);
				else
					parent.setScale(parent.getScale() - 0.25);

				// hack that seems to make it zoom to center
				parent.setCurrentX((int) (parent.getCurrentX() - parent.getWidth() / parent.getScale() / 6));
				parent.setCurrentY((int) (parent.getCurrentY() - parent.getHeight() / parent.getScale() / 6));
			}
		}
		parent.repaint();
	}
}

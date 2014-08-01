package ui.windows.map;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ui.ColorPaletteConstants;
import ui.windows.map.main.MapComponent;

import com.google.common.base.Optional;

public class WorldSelectorPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	private static final int CORNER_ROUNDNESS = 10;

	private static final int GAP_SIZE = 10;

	private static final int X_GAP = 10;

	private double renderY;
	private final List<MapComponent> mapPanels = new ArrayList<>();
	private int currentMapPanel;
	private final MapComponentsHolder componentsHolder;

	private int stringHeight;

	public WorldSelectorPanel(MapComponentsHolder componentsHolder) {
		super();
		this.componentsHolder = componentsHolder;
		setBackground(ColorPaletteConstants.WORLD_SELECTOR_BACKGROUND);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void addMapComponent(MapComponent mapPanel) {
		mapPanels.add(mapPanel);
		int index = mapPanels.size() - 1;
		componentsHolder.addPanel(mapPanel, index);
		changeToPanel(index);
		repaint();
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D g = (Graphics2D) graphics;

		g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		stringHeight = g.getFontMetrics().getHeight();

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		for (int i = 0; i < mapPanels.size(); i++) {

			int yPos = i * (stringHeight + GAP_SIZE) + 10;

			String name = mapPanels.get(i).getWorldName();
			if (renderY + yPos > getHeight()) {
				break;
			}

			int rectWidth = getWidth() - 2 * X_GAP;
			int rectHeight = stringHeight;
			int stringY = (int) renderY + (rectHeight + GAP_SIZE) * (i + 1) - rectHeight / 4;
			int stringX = X_GAP + rectWidth / 2 - g.getFontMetrics().stringWidth(name) / 2;

			if (i == currentMapPanel) g.setColor(ColorPaletteConstants.WORLD_SELECTOR_BUTTON_CURRENT);
			else g.setColor(ColorPaletteConstants.WORLD_SELECTOR_BUTTON);
			g.fillRoundRect(X_GAP, (int) (renderY + yPos), rectWidth, rectHeight, CORNER_ROUNDNESS, CORNER_ROUNDNESS);
			g.setColor(ColorPaletteConstants.WORLD_SELECTOR_TEXT);
			g.drawString(name, stringX, stringY);
		}
	}

	public MapComponent getCurrentWorld() {
		return mapPanels.get(currentMapPanel);
	}

	private void changeToPanel(int index) {
		currentMapPanel = index;
		componentsHolder.changePanel(index);
	}

	public void deleteCurrentWorld() {
		if (mapPanels.size() <= 1) return;
		int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the current world?");
		if (result == JOptionPane.OK_OPTION) {

			int currentId = currentMapPanel;
			mapPanels.remove(currentId);
			if (currentId >= mapPanels.size()) {
				changeToPanel(currentId - 1);
			} else {
				changeToPanel(currentId);
			}
			componentsHolder.removeComponent(currentId);

			repaint();
		}
	}

	private Optional<Double> pressedY = Optional.absent();

	@Override
	public void mouseClicked(MouseEvent e) {
		for (int i = 0; i < mapPanels.size(); i++) {
			int rectWidth = getWidth() - 2 * X_GAP;
			RoundRectangle2D rect = new RoundRectangle2D.Double(X_GAP, (int) (renderY + 10 + (stringHeight + GAP_SIZE)
					* i), rectWidth, stringHeight, CORNER_ROUNDNESS, CORNER_ROUNDNESS);
			if (rect.contains(e.getPoint())) {
				changeToPanel(i);
				repaint();
				return;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressedY = Optional.of(e.getPoint().getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pressedY = Optional.absent();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		double currentY = e.getPoint().getY();
		if (pressedY.isPresent()) {
			renderY = renderY + currentY - pressedY.get();
		}
		pressedY = Optional.of(currentY);
		enforceBounds();
		repaint();
	}

	private void enforceBounds() {
		final int MIN_BOUND = -Math.abs(mapPanels.size() * (stringHeight + GAP_SIZE) - 10);
		final int MAX_BOUND = getHeight() - 20;
		final int RETURN_SLOWNESS = 3; // should be less than 5ish

		if (renderY < MIN_BOUND) {
			renderY += Math.abs(renderY - MIN_BOUND) / RETURN_SLOWNESS;
		} else if (renderY > MAX_BOUND) {
			renderY -= Math.abs(renderY - MAX_BOUND) / RETURN_SLOWNESS;
		}
	}
}

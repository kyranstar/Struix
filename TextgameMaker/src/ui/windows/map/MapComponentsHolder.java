package ui.windows.map;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JPanel;

import ui.windows.map.main.MapComponent;

public class MapComponentsHolder extends JPanel {

	private static final long serialVersionUID = 1L;

	public MapComponentsHolder() {
		super(new CardLayout());
	}

	public void addPanel(MapComponent component, int id) {
		add(component, String.valueOf(id));
	}

	public void changePanel(int id) {
		CardLayout cl = (CardLayout) this.getLayout();
		cl.show(this, String.valueOf(id));
	}

	public void removeComponent(int currentId) {
		Component[] components = getComponents();
		CardLayout cardLayout = (CardLayout) this.getLayout();

		for (int i = 0; i < components.length; i++) {
			if (components[i].getName() == null) continue;
			if (components[i].getName().equals(String.valueOf(currentId))) {
				cardLayout.removeLayoutComponent(components[i]);
			}
		}
		repaint();
	}

}

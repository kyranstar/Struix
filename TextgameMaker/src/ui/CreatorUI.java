package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ui.windows.BackgroundUI;
import ui.windows.map.MapRoom;
import ui.windows.map.main.MapComponent;
import ui.windows.map.main.MapComponent.Tool;

public class CreatorUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private Container backgroundPane;
	private JDesktopPane desktop = new JDesktopPane();

	private MapComponent mapPanel;

	public CreatorUI() {
		super("Struix");

		ActionListener dragToolListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mapPanel.getCurrentTool() != Tool.DRAG_ROOM) {
					mapPanel.setCurrentTool(Tool.DRAG_ROOM);
					mapPanel.deleteStuckToMouse();
				}
			}
		};

		ActionListener createRoomListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mapPanel.getCurrentTool() != Tool.CREATE_ROOM) {
					mapPanel.setCurrentTool(Tool.CREATE_ROOM);
					// 1000 extra just to be safe
					mapPanel.createRoomAtPoint(
							(int) (mapPanel.getCurrentX() * mapPanel.getScale()
									- MapRoom.DEFAULT_WIDTH - 1000),
							(int) (mapPanel.getCurrentY() * mapPanel.getScale()
									- MapRoom.DEFAULT_HEIGHT - 1000))
							.stickToMouse();
				}
			}
		};
		ActionListener createHallwayListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mapPanel.getCurrentTool() != Tool.CREATE_HALLWAY) {
					mapPanel.setCurrentTool(Tool.CREATE_HALLWAY);
					mapPanel.deleteStuckToMouse();
					mapPanel.repaint();
				}
			}
		};
		ActionListener createWorldListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.deleteStuckToMouse();
			}

		};
		ActionListener createPortalListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.deleteStuckToMouse();
			}
		};

		ActionListener deleteWorldListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};
		ActionListener createEntityListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};
		ActionListener deleteEntityListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};
		desktop.addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) {
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentShown(ComponentEvent e) {
			}

			public void componentResized(ComponentEvent e) {
				backgroundPane.setSize(getSize());
			}
		});

		mapPanel = new MapComponent(this);
		JPanel worldSelectorPanel = new JPanel();
		worldSelectorPanel.setBackground(new Color(255, 0, 100, 100));
		JPanel entityListPanel = new JPanel();
		entityListPanel.setBackground(new Color(0, 100, 255, 100));

		backgroundPane = new BackgroundUI().initComponents(dragToolListener,
				createRoomListener, createHallwayListener, createWorldListener,
				createPortalListener, deleteWorldListener,
				createEntityListener, deleteEntityListener, mapPanel,
				worldSelectorPanel, entityListPanel);

		desktop.setLayout(new BorderLayout());
		desktop.setOpaque(false);

		desktop.setLayer(backgroundPane, JLayeredPane.DEFAULT_LAYER);
		desktop.add(backgroundPane, BorderLayout.CENTER);

		add(desktop, BorderLayout.CENTER);
		desktop.setPreferredSize(backgroundPane.getPreferredSize());

		pack();
	}

	private static void setPLAF(String name) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if (name.equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(BackgroundUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(BackgroundUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(BackgroundUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(BackgroundUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) {
		setPLAF("Nimbus");

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				CreatorUI frame = new CreatorUI();
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});

	}

	public void addWindow(JPanel window, Dimension size, Point location, String title) {
		// For some reason, without this for loop, the room opens up in full screen?
		for (int i = 0; i < 2; i++) {
			JInternalFrame internalFrame = new JInternalFrame(title, false,
					true, false, false);
			if (i == 0) {
				internalFrame.getContentPane().add(window);// ????????????????????????
				internalFrame.setSize(size);
				internalFrame.setLocation(location);
			}
			desktop.add(internalFrame);
			desktop.setLayer(internalFrame, JLayeredPane.DRAG_LAYER);
			internalFrame.setVisible(i == 0);
		}
		repaint();
	}

}

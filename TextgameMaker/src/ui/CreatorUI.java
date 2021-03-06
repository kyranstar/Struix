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

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import ui.windows.BackgroundUI;
import ui.windows.map.MapComponentsHolder;
import ui.windows.map.MapRoom;
import ui.windows.map.WorldSelectorPanel;
import ui.windows.map.main.MapComponent;
import ui.windows.map.main.MapComponent.Tool;
import ui.windows.room.RoomDialogue;

public class CreatorUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private Container backgroundPane;
	private final JDesktopPane desktop = new JDesktopPane();

	private WorldSelectorPanel worldSelectorPanel;

	public CreatorUI() {
		super("Struix");

		final ActionListener dragToolListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("SUPEr");
				worldSelectorPanel.getCurrentWorld().setCurrentTool(Tool.DRAG_ROOM);
				worldSelectorPanel.getCurrentWorld().deleteStuckToMouse();
			}
		};

		final ActionListener createRoomListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (worldSelectorPanel.getCurrentWorld().getCurrentTool() != Tool.CREATE_ROOM) {
					worldSelectorPanel.getCurrentWorld().setCurrentTool(Tool.CREATE_ROOM);
					// 1000 extra just to be safe
					worldSelectorPanel
					.getCurrentWorld()
					.createRoomAtPoint(
							worldSelectorPanel.getCurrentWorld().getCurrentX() - MapRoom.DEFAULT_WIDTH - 1000,
							worldSelectorPanel.getCurrentWorld().getCurrentY() - MapRoom.DEFAULT_HEIGHT - 1000)
							.stickToMouse();
				}
			}
		};
		final ActionListener createHallwayListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (worldSelectorPanel.getCurrentWorld().getCurrentTool() != Tool.CREATE_HALLWAY) {
					worldSelectorPanel.getCurrentWorld().setCurrentTool(Tool.CREATE_HALLWAY);
					worldSelectorPanel.getCurrentWorld().deleteStuckToMouse();
					worldSelectorPanel.getCurrentWorld().repaint();
				}
			}
		};
		final ActionListener createWorldListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				worldSelectorPanel.getCurrentWorld().deleteStuckToMouse();
				final String name = JOptionPane.showInputDialog("World Name: ");
				if (name == null) { return; }
				worldSelectorPanel.addMapComponent(new MapComponent(name, CreatorUI.this));
			}

		};
		final ActionListener createPortalListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				worldSelectorPanel.getCurrentWorld().deleteStuckToMouse();
			}
		};

		final ActionListener deleteWorldListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				worldSelectorPanel.deleteCurrentWorld();
			}
		};
		final ActionListener createEntityListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};
		final ActionListener deleteEntityListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};
		desktop.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				backgroundPane.setSize(getSize());
			}
		});

		final MapComponentsHolder mapPanel = new MapComponentsHolder();

		worldSelectorPanel = new WorldSelectorPanel(mapPanel);
		worldSelectorPanel.addMapComponent(new MapComponent("Default World", this));
		final JPanel entityListPanel = new JPanel();
		entityListPanel.setBackground(new Color(0, 100, 255, 100));

		backgroundPane = new BackgroundUI().initComponents(dragToolListener, createRoomListener, createHallwayListener,
				createWorldListener, createPortalListener, deleteWorldListener, createEntityListener,
				deleteEntityListener, mapPanel, worldSelectorPanel, entityListPanel);

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
			for (final javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if (name.equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (final ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(BackgroundUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (final InstantiationException ex) {
			java.util.logging.Logger.getLogger(BackgroundUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (final IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(BackgroundUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (final javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(BackgroundUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
	}

	public static void main(String[] args) {
		setPLAF("Nimbus");

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final CreatorUI frame = new CreatorUI();
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});

	}

	public void addWindow(RoomDialogue window, final MapRoom room, Dimension size, Point location, String title) {
		// For some reason, without this for loop, the room opens up in full
		// screen?
		for (int i = 0; i < 2; i++) {
			final JInternalFrame internalFrame = new JInternalFrame(title, false, true, false, false);
			if (i == 0) {
				internalFrame.getContentPane().add(window);// ????????????????????????
				internalFrame.setSize(size);
				internalFrame.setLocation(location);
				internalFrame.addInternalFrameListener(new InternalFrameListener() {

					@Override
					public void internalFrameActivated(InternalFrameEvent e) {
					}

					@Override
					public void internalFrameClosed(InternalFrameEvent e) {
						room.setHasRoomDialogueOpen(false);
					}

					@Override
					public void internalFrameClosing(InternalFrameEvent e) {
					}

					@Override
					public void internalFrameDeactivated(InternalFrameEvent e) {
					}

					@Override
					public void internalFrameDeiconified(InternalFrameEvent e) {
					}

					@Override
					public void internalFrameIconified(InternalFrameEvent e) {
					}

					@Override
					public void internalFrameOpened(InternalFrameEvent e) {
					}

				});
			}
			desktop.add(internalFrame);
			desktop.setLayer(internalFrame, JLayeredPane.DRAG_LAYER);
			internalFrame.setVisible(i == 0);
		}
		repaint();
	}

}

package ui.windows;

import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import ui.ColorPaletteConstants;

/*IMPORTANT! When replacing this with new code from net beans:
 * make this extend nothing
 * make initComponents() return a Container
 * add parameters for action listeners for buttons
 * replace action listeners for each button with those
 * create a frame inside the method, add everything to that frame
 * return frame.getContentPane() 
 */
public class BackgroundUI {

	public Container initComponents(ActionListener dragToolListener, ActionListener createRoomListener,
			ActionListener createHallwayListener, ActionListener createWorldListener,
			ActionListener createPortalListener, ActionListener deleteWorldListener,
			ActionListener createEntityListener, ActionListener deleteEntityListener, final JPanel mapPanel,
			JPanel worldSelectorPanel, JPanel entityListPanel) {

		JFrame frame = new JFrame();

		mainBackgroundPanel = new JPanel();
		toolbarPanel = new JPanel();
		toolbarPanel.setBackground(ColorPaletteConstants.TOOLBAR_BACKGROUND);
		mapToolbarPanel = new JPanel();
		mapToolbarPanel.setBackground(ColorPaletteConstants.TOOLBAR_BACKGROUND);

		mapToolbarTitle = new UILabel(true);
		dragToolButton = new UIButton("Drag Tool", true);
		createRoomButton = new UIButton("Create Room", true);
		createHallwayButton = new UIButton("Create Hallway", true);
		createWorldButton = new UIButton("Create World", true);
		createPortalButton = new UIButton("Create Portal", true);
		deleteWorldButton = new UIButton("Delete World", false);
		entityListToolbarPanel = new JPanel();
		entityListToolbarPanel.setBackground(ColorPaletteConstants.TOOLBAR_BACKGROUND);

		entityTitle = new UILabel(true);
		createEntityButton = new UIButton("Create Entity", true);
		deleteEntityButton = new UIButton("Delete Entity", false);
		mainPanel = new JPanel();
		toolbarSplitPane = new JSplitPane() {
			private static final long serialVersionUID = 1L;
			private static final int LOCATION = 115;
			{
				setDividerLocation(LOCATION);
			}

			@Override
			public int getDividerLocation() {
				return LOCATION;
			}

			@Override
			public int getLastDividerLocation() {
				return LOCATION;
			}
		};
		toolbarSplitPane.setBackground(ColorPaletteConstants.TOOLBAR_BACKGROUND);

		toolbarSplitPane.setUI(new BasicSplitPaneUI() {
			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {
					private static final long serialVersionUID = 1L;

					public void setBorder(Border b) {
					}
				};
			}
		});

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		mapToolbarTitle.setText("Map");

		createRoomButton.addActionListener(createRoomListener);

		createHallwayButton.addActionListener(createHallwayListener);

		createWorldButton.addActionListener(createWorldListener);

		createPortalButton.addActionListener(createPortalListener);

		deleteWorldButton.addActionListener(deleteWorldListener);

		dragToolButton.addActionListener(dragToolListener);

		GroupLayout worldSelectorPanelLayout = new GroupLayout(worldSelectorPanel);
		worldSelectorPanel.setLayout(worldSelectorPanelLayout);
		worldSelectorPanelLayout.setHorizontalGroup(worldSelectorPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
		worldSelectorPanelLayout.setVerticalGroup(worldSelectorPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 155, Short.MAX_VALUE));

		GroupLayout mapToolbarPanelLayout = new GroupLayout(mapToolbarPanel);
		mapToolbarPanel.setLayout(mapToolbarPanelLayout);
		mapToolbarPanelLayout.setHorizontalGroup(mapToolbarPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						mapToolbarPanelLayout.createSequentialGroup().addGroup(
								mapToolbarPanelLayout
										.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(mapToolbarTitle, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(dragToolButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(createRoomButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(createHallwayButton, GroupLayout.Alignment.TRAILING,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(createWorldButton, GroupLayout.Alignment.TRAILING,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(createPortalButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(deleteWorldButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				.addComponent(worldSelectorPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		mapToolbarPanelLayout.setVerticalGroup(mapToolbarPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						mapToolbarPanelLayout
								.createSequentialGroup()
								.addComponent(mapToolbarTitle)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(dragToolButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(createRoomButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(createHallwayButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(createWorldButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(createPortalButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(deleteWorldButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(worldSelectorPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE).addContainerGap()));

		entityTitle.setText("Entities");

		createEntityButton.addActionListener(createEntityListener);

		deleteEntityButton.addActionListener(deleteEntityListener);

		GroupLayout entityListToolbarPanelLayout = new GroupLayout(entityListToolbarPanel);
		entityListToolbarPanel.setLayout(entityListToolbarPanelLayout);
		entityListToolbarPanelLayout.setHorizontalGroup(entityListToolbarPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				entityListToolbarPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								entityListToolbarPanelLayout
										.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(entityTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(createEntityButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(deleteEntityButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));
		entityListToolbarPanelLayout.setVerticalGroup(entityListToolbarPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				entityListToolbarPanelLayout.createSequentialGroup().addComponent(entityTitle)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(createEntityButton)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(deleteEntityButton)
						.addGap(0, 26, Short.MAX_VALUE)));

		GroupLayout toolbarPanelLayout = new GroupLayout(toolbarPanel);
		toolbarPanel.setLayout(toolbarPanelLayout);
		toolbarPanelLayout.setHorizontalGroup(toolbarPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						toolbarPanelLayout
								.createSequentialGroup()
								.addGroup(
										toolbarPanelLayout
												.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
												.addComponent(entityListToolbarPanel, GroupLayout.Alignment.LEADING,
														GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(mapToolbarPanel, GroupLayout.Alignment.LEADING,
														GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		toolbarPanelLayout.setVerticalGroup(toolbarPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						toolbarPanelLayout
								.createSequentialGroup()
								.addComponent(mapToolbarPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(entityListToolbarPanel, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

		toolbarSplitPane.setLeftComponent(toolbarPanel);

		GroupLayout entityListPanelLayout = new GroupLayout(entityListPanel);
		entityListPanel.setLayout(entityListPanelLayout);
		entityListPanelLayout.setHorizontalGroup(entityListPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 382, Short.MAX_VALUE));
		entityListPanelLayout.setVerticalGroup(entityListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGap(0, 103, Short.MAX_VALUE));

		GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(entityListPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(mapPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				mainPanelLayout
						.createSequentialGroup()
						.addComponent(mapPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(entityListPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)));

		toolbarSplitPane.setRightComponent(mainPanel);

		GroupLayout mainBackgroundPanelLayout = new GroupLayout(mainBackgroundPanel);
		mainBackgroundPanel.setLayout(mainBackgroundPanelLayout);
		mainBackgroundPanelLayout.setHorizontalGroup(mainBackgroundPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(toolbarSplitPane));
		mainBackgroundPanelLayout.setVerticalGroup(mainBackgroundPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(toolbarSplitPane));

		GroupLayout layout = new GroupLayout(frame.getContentPane());
		frame.getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(
				mainBackgroundPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(
				mainBackgroundPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		frame.pack();
		return frame.getContentPane();
	}// </editor-fold>

	// Variables declaration - do not modify

	private UIButton dragToolButton;
	private UIButton createEntityButton;
	private UIButton createHallwayButton;
	private UIButton createPortalButton;
	private UIButton createRoomButton;
	private UIButton createWorldButton;
	private UIButton deleteEntityButton;
	private UIButton deleteWorldButton;
	private JPanel entityListToolbarPanel;
	private UILabel entityTitle;
	private JPanel mainBackgroundPanel;
	private JPanel mainPanel;
	private JPanel mapToolbarPanel;
	private UILabel mapToolbarTitle;
	private JPanel toolbarPanel;
	private JSplitPane toolbarSplitPane;
	// End of variables declaration

}

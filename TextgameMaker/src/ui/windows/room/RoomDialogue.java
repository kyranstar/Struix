package ui.windows.room;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.border.Border;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import ui.ColorUtils;
import ui.windows.UIButton;
import ui.windows.UILabel;
import ui.windows.map.MapRoom;

public class RoomDialogue extends JPanel {
	private static final long serialVersionUID = 1L;

	// Variables declaration - do not modify
	private UIButton alternativeInputsButton;
	private UIButton createObjectButton;
	private JPanel enterFirstScriptPanel;
	private JTextArea enterFirstTextArea;
	private JPanel enterScriptPanel;
	private JTextArea enterTextArea;
	private UIButton entranceTextButton;
	private JPanel everyTurnScriptPanel;
	private JTextArea everyTurnTextArea;
	private JPanel exitFirstScriptPanel;
	private JTextArea exitFirstTextArea;
	private JPanel exitScriptPanel;
	private JTextArea exitTextArea;
	private UIButton importObjectButton;
	private UIButton changeColorButton;
	private UILabel jLabel1;
	private JPanel jPanel1;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private JScrollPane jScrollPane4;
	private JScrollPane jScrollPane5;
	private JSplitPane jSplitPane;
	private JTabbedPane jTabbedPane;
	private JPanel leftPanel;
	private UILabel objectListLabel;
	private JPanel objectListPanel;
	private JPanel rightPanel;
	private JTextField roomNameField;
	private UILabel scriptsLabelEnter;
	private UILabel scriptsLabelEnterFirst;
	private UILabel scriptsLabelEveryTurn;
	private UILabel scriptsLabelExit;
	private UILabel scriptsLabelExitFirst;

	public RoomDialogue(MapRoom room) {
		this.add(buildComponents(room));
	}

	private void setAllComponentsColor(Color color){
		final Color ROOM_COLOR = color;
		final Color TEXT_AREA_COLOR = ColorUtils.mapSaturation(
				ColorUtils.mapBrightness(color, 0.85f), 0.2f);
		

		leftPanel.setBackground(ROOM_COLOR);
		objectListLabel.setBackground(ROOM_COLOR);
		objectListPanel.setBackground(TEXT_AREA_COLOR);
		roomNameField.setBackground(TEXT_AREA_COLOR);
		rightPanel.setBackground(ROOM_COLOR);
		enterScriptPanel.setBackground(ROOM_COLOR);
		enterTextArea.setBackground(TEXT_AREA_COLOR);
		exitScriptPanel.setBackground(ROOM_COLOR);
		exitTextArea.setBackground(TEXT_AREA_COLOR);
		enterFirstScriptPanel.setBackground(ROOM_COLOR);
		enterFirstTextArea.setBackground(TEXT_AREA_COLOR);
		exitFirstScriptPanel.setBackground(ROOM_COLOR);
		exitFirstTextArea.setBackground(TEXT_AREA_COLOR);
		everyTurnScriptPanel.setBackground(ROOM_COLOR);
		everyTurnTextArea.setBackground(TEXT_AREA_COLOR);
		jSplitPane.setBackground(ROOM_COLOR);
	}
	
	private Container buildComponents(final MapRoom room) {

		JPanel main = new JPanel();

		jPanel1 = new JPanel();
		jLabel1 = new UILabel(true);
		jSplitPane = new JSplitPane() {
			private static final long serialVersionUID = 1L;
			private final int location = 115;
			{
				setDividerLocation(location);
			}

			@Override
			public int getDividerLocation() {
				return location;
			}

			@Override
			public int getLastDividerLocation() {
				return location;
			}
		};
		changeColorButton = new UIButton(true);
		changeColorButton.setText("Change Color");
		changeColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color changedTo = getColorDialogue(room.getRoom()
						.getBackgroundColor());
				room.getRoom().setBackgroundColor(changedTo);
				setAllComponentsColor(room.getRoom().getBackgroundColor());
			}

		});

		jSplitPane.setUI(new BasicSplitPaneUI() {
			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {
					private static final long serialVersionUID = 1L;

					public void setBorder(Border b) {
					}

					@Override
					public void paint(Graphics g) {
						g.setColor(room.getRoom().getBackgroundColor());
						g.fillRect(0, 0, getSize().width, getSize().height);
						super.paint(g);
					}
				};
			}
		});

		leftPanel = new JPanel();

		objectListLabel = new UILabel(false);


		entranceTextButton = new UIButton(true);
		alternativeInputsButton = new UIButton(true);
		createObjectButton = new UIButton(true);
		importObjectButton = new UIButton(true);
		objectListPanel = new JPanel();



		roomNameField = new JTextField();


		rightPanel = new JPanel();


		jTabbedPane = new JTabbedPane();
		jTabbedPane.setOpaque(false);

		enterScriptPanel = new JPanel();


		scriptsLabelEnter = new UILabel(false);
		jScrollPane1 = new JScrollPane();
		enterTextArea = new JTextArea();


		exitScriptPanel = new JPanel();


		jScrollPane5 = new JScrollPane();
		exitTextArea = new JTextArea();

		scriptsLabelExit = new UILabel(false);
		enterFirstScriptPanel = new JPanel();



		jScrollPane4 = new JScrollPane();
		enterFirstTextArea = new JTextArea();

		scriptsLabelEnterFirst = new UILabel(false);
		exitFirstScriptPanel = new JPanel();


		jScrollPane3 = new JScrollPane();
		exitFirstTextArea = new JTextArea();

		scriptsLabelExitFirst = new UILabel(false);
		everyTurnScriptPanel = new JPanel();


		jScrollPane2 = new JScrollPane();
		everyTurnTextArea = new JTextArea();

		scriptsLabelEveryTurn = new UILabel(false);

		GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 44, Short.MAX_VALUE));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 410, Short.MAX_VALUE));

		jLabel1.setText("Scripts");

		objectListLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		objectListLabel.setText("Object List");

		entranceTextButton.setText("Entrance Text");
		entranceTextButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						entranceTextButtonActionPerformed(evt);
					}
				});

		alternativeInputsButton.setText("Alternative Inputs");

		createObjectButton.setText("Create Object");

		importObjectButton.setText("Import Object");
		importObjectButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						importObjectButtonActionPerformed(evt);
					}
				});

		GroupLayout objectListPanelLayout = new GroupLayout(objectListPanel);
		objectListPanel.setLayout(objectListPanelLayout);
		objectListPanelLayout.setHorizontalGroup(objectListPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0,
						103, Short.MAX_VALUE));
		objectListPanelLayout.setVerticalGroup(objectListPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0,
						320, Short.MAX_VALUE));

		roomNameField.setText(room.getRoom().getName());

		roomNameField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				changeName();
			}

			public void removeUpdate(DocumentEvent e) {
				changeName();
			}

			public void insertUpdate(DocumentEvent e) {
				changeName();
			}

			public void changeName() {
				room.getRoom().setName(roomNameField.getText());
			}
		});

		GroupLayout leftPanelLayout = new GroupLayout(leftPanel);
		leftPanel.setLayout(leftPanelLayout);
		leftPanelLayout
				.setHorizontalGroup(leftPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								leftPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												leftPanelLayout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING)
														.addComponent(
																objectListLabel,
																GroupLayout.DEFAULT_SIZE,
																161,
																Short.MAX_VALUE)
														.addGroup(
																leftPanelLayout
																		.createSequentialGroup()
																		.addGroup(
																				leftPanelLayout
																						.createParallelGroup(
																								GroupLayout.Alignment.LEADING)
																						.addComponent(
																								importObjectButton)
																						.addGroup(
																								leftPanelLayout
																										.createParallelGroup(
																												GroupLayout.Alignment.TRAILING,
																												false)
																										.addComponent(
																												roomNameField,
																												GroupLayout.Alignment.LEADING)
																										.addComponent(
																												createObjectButton,
																												GroupLayout.Alignment.LEADING,
																												GroupLayout.DEFAULT_SIZE,
																												GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE)
																										.addComponent(
																												alternativeInputsButton,
																												GroupLayout.Alignment.LEADING,
																												GroupLayout.PREFERRED_SIZE,
																												0,
																												Short.MAX_VALUE)
																										.addComponent(
																												entranceTextButton,
																												GroupLayout.Alignment.LEADING,
																												GroupLayout.DEFAULT_SIZE,
																												GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE)
																										.addComponent(
																												changeColorButton,
																												GroupLayout.Alignment.LEADING,
																												GroupLayout.DEFAULT_SIZE,
																												GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE))
																						.addComponent(
																								objectListPanel,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE))
																		.addGap(0,
																				0,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		leftPanelLayout
				.setVerticalGroup(leftPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								leftPanelLayout
										.createSequentialGroup()
										.addGap(6, 6, 6)
										.addComponent(roomNameField,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(changeColorButton,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(entranceTextButton)
										.addGap(4, 4, 4)
										.addComponent(alternativeInputsButton)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(createObjectButton)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(importObjectButton)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(objectListLabel,
												GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(objectListPanel,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		jSplitPane.setLeftComponent(leftPanel);

		jTabbedPane.setTabPlacement(JTabbedPane.RIGHT);

		scriptsLabelEnter.setText("Scripts");

		enterTextArea.setColumns(20);
		enterTextArea.setRows(5);
		jScrollPane1.setViewportView(enterTextArea);

		GroupLayout enterScriptPanelLayout = new GroupLayout(enterScriptPanel);
		enterScriptPanel.setLayout(enterScriptPanelLayout);
		enterScriptPanelLayout.setHorizontalGroup(enterScriptPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						enterScriptPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(scriptsLabelEnter)
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
				.addComponent(jScrollPane1));
		enterScriptPanelLayout.setVerticalGroup(enterScriptPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
						enterScriptPanelLayout
								.createSequentialGroup()
								.addComponent(scriptsLabelEnter)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jScrollPane1)));

		jTabbedPane.addTab("Enter", enterScriptPanel);

		exitTextArea.setColumns(20);
		exitTextArea.setRows(5);
		jScrollPane5.setViewportView(exitTextArea);

		scriptsLabelExit.setText("Scripts");

		GroupLayout exitScriptPanelLayout = new GroupLayout(exitScriptPanel);
		exitScriptPanel.setLayout(exitScriptPanelLayout);
		exitScriptPanelLayout.setHorizontalGroup(exitScriptPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						exitScriptPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(scriptsLabelExit)
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
				.addComponent(jScrollPane5));
		exitScriptPanelLayout.setVerticalGroup(exitScriptPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
						exitScriptPanelLayout
								.createSequentialGroup()
								.addComponent(scriptsLabelExit)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jScrollPane5)));

		jTabbedPane.addTab("Exit", exitScriptPanel);

		enterFirstTextArea.setColumns(20);
		enterFirstTextArea.setRows(5);
		jScrollPane4.setViewportView(enterFirstTextArea);

		scriptsLabelEnterFirst.setText("Scripts");

		GroupLayout enterFirstScriptPanelLayout = new GroupLayout(
				enterFirstScriptPanel);
		enterFirstScriptPanel.setLayout(enterFirstScriptPanelLayout);
		enterFirstScriptPanelLayout
				.setHorizontalGroup(enterFirstScriptPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								enterFirstScriptPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(scriptsLabelEnterFirst)
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
						.addComponent(jScrollPane4));
		enterFirstScriptPanelLayout
				.setVerticalGroup(enterFirstScriptPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								enterFirstScriptPanelLayout
										.createSequentialGroup()
										.addComponent(scriptsLabelEnterFirst)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jScrollPane4)));

		jTabbedPane.addTab("Enter First", enterFirstScriptPanel);

		exitFirstTextArea.setColumns(20);
		exitFirstTextArea.setRows(5);
		jScrollPane3.setViewportView(exitFirstTextArea);

		scriptsLabelExitFirst.setText("Scripts");

		GroupLayout exitFirstScriptPanelLayout = new GroupLayout(
				exitFirstScriptPanel);
		exitFirstScriptPanel.setLayout(exitFirstScriptPanelLayout);
		exitFirstScriptPanelLayout
				.setHorizontalGroup(exitFirstScriptPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								exitFirstScriptPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(scriptsLabelExitFirst)
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
						.addComponent(jScrollPane3));
		exitFirstScriptPanelLayout.setVerticalGroup(exitFirstScriptPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
						exitFirstScriptPanelLayout
								.createSequentialGroup()
								.addComponent(scriptsLabelExitFirst)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jScrollPane3)));

		jTabbedPane.addTab("Exit First", exitFirstScriptPanel);

		everyTurnTextArea.setColumns(20);
		everyTurnTextArea.setRows(5);
		jScrollPane2.setViewportView(everyTurnTextArea);

		scriptsLabelEveryTurn.setText("Scripts");

		GroupLayout everyTurnScriptPanelLayout = new GroupLayout(
				everyTurnScriptPanel);
		everyTurnScriptPanel.setLayout(everyTurnScriptPanelLayout);
		everyTurnScriptPanelLayout
				.setHorizontalGroup(everyTurnScriptPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								everyTurnScriptPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(scriptsLabelEveryTurn)
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
						.addComponent(jScrollPane2));
		everyTurnScriptPanelLayout.setVerticalGroup(everyTurnScriptPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
						everyTurnScriptPanelLayout
								.createSequentialGroup()
								.addComponent(scriptsLabelEveryTurn)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jScrollPane2)));

		jTabbedPane.addTab("Every Turn", everyTurnScriptPanel);

		GroupLayout rightPanelLayout = new GroupLayout(rightPanel);
		rightPanel.setLayout(rightPanelLayout);
		rightPanelLayout.setHorizontalGroup(rightPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
						rightPanelLayout
								.createSequentialGroup()
								.addComponent(jTabbedPane,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, Short.MAX_VALUE)));
		rightPanelLayout.setVerticalGroup(rightPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jTabbedPane,
				GroupLayout.Alignment.TRAILING));
		jSplitPane.setRightComponent(rightPanel);
		jSplitPane.setOpaque(true);
		GroupLayout layout = new GroupLayout(main);
		main.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jSplitPane, GroupLayout.PREFERRED_SIZE,
								558, GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jSplitPane));
		setAllComponentsColor(room.getRoom().getBackgroundColor());
		
		return main;
	}// </editor-fold>

	private Color getColorDialogue(Color defaultColor) {
		// Swatches, HSV, HSL, RGB, CMYK
		final String ALLOWED_PANEL = "HSV";

		JColorChooser chooser = new JColorChooser();
		AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
		for (AbstractColorChooserPanel accp : panels) {
			if (accp.getDisplayName().equals(ALLOWED_PANEL)) {
				JOptionPane.showMessageDialog(null, accp);
			}
		}
		Color colorVal = chooser.getColor();
		if (colorVal == null) {
			return defaultColor;
		}
		return colorVal;
	}

	private void entranceTextButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void importObjectButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

}

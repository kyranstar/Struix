package ui.windows;

import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
/*IMPORTANT! When replacing this with new code from net beans:
 * make this extend nothing
 * make initComponents() return a Container
 * add parameters for action listeners for buttons
 * replace action listeners for each button with those
 * create a frame inside the method, add everything to that frame
 * return frame.getContentPane() 
 */
public class BackgroundUI {


    public Container initComponents(ActionListener createRoomListener, 
    		ActionListener createHallwayListener, 
    		ActionListener createWorldListener, 
    		ActionListener createPortalListener, 
    		ActionListener deleteWorldListener, 
    		ActionListener createEntityListener, 
    		ActionListener deleteEntityListener, 
    		JPanel mapPanel, 
    		JPanel worldSelectorPanel,
    		JPanel entityListPanel) {

    	JFrame frame = new JFrame();
    	
        mainBackgroundPanel = new javax.swing.JPanel();
        toolbarSplitPane = new javax.swing.JSplitPane();
        toolbarPanel = new javax.swing.JPanel();
        mapToolbarPanel = new javax.swing.JPanel();
        mapToolbarTitle = new javax.swing.JLabel();
        createRoomButton = new javax.swing.JButton();
        createHallwayButton = new javax.swing.JButton();
        createWorldButton = new javax.swing.JButton();
        createPortalButton = new javax.swing.JButton();
        deleteWorldButton = new javax.swing.JButton();
        entityListToolbarPanel = new javax.swing.JPanel();
        entityTitle = new javax.swing.JLabel();
        createEntityButton = new javax.swing.JButton();
        deleteEntityButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();

        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mapToolbarTitle.setText("Map");

        createRoomButton.setText("Create Room");
        createRoomButton.addActionListener(createRoomListener);

        createHallwayButton.setText("Create Hallway");
        createHallwayButton.addActionListener(createHallwayListener);
        
        createWorldButton.setText("Create World");
        createWorldButton.addActionListener(createWorldListener);

        createPortalButton.setText("Create Portal");
        createPortalButton.addActionListener(createPortalListener);

        deleteWorldButton.setText("Delete World");
        deleteWorldButton.addActionListener(deleteWorldListener);

        javax.swing.GroupLayout worldSelectorPanelLayout = new javax.swing.GroupLayout(worldSelectorPanel);
        worldSelectorPanel.setLayout(worldSelectorPanelLayout);
        worldSelectorPanelLayout.setHorizontalGroup(
            worldSelectorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        worldSelectorPanelLayout.setVerticalGroup(
            worldSelectorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 185, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout mapToolbarPanelLayout = new javax.swing.GroupLayout(mapToolbarPanel);
        mapToolbarPanel.setLayout(mapToolbarPanelLayout);
        mapToolbarPanelLayout.setHorizontalGroup(
            mapToolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mapToolbarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mapToolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mapToolbarTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createRoomButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createHallwayButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createWorldButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createPortalButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mapToolbarPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(deleteWorldButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(worldSelectorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mapToolbarPanelLayout.setVerticalGroup(
            mapToolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mapToolbarPanelLayout.createSequentialGroup()
                .addComponent(mapToolbarTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createRoomButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createHallwayButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createWorldButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createPortalButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteWorldButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(worldSelectorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        entityTitle.setText("Entities");

        createEntityButton.setText("Create Entity");
        createEntityButton.addActionListener(createEntityListener);

        deleteEntityButton.setText("Delete Entity");
        deleteEntityButton.addActionListener(deleteEntityListener);

        javax.swing.GroupLayout entityListToolbarPanelLayout = new javax.swing.GroupLayout(entityListToolbarPanel);
        entityListToolbarPanel.setLayout(entityListToolbarPanelLayout);
        entityListToolbarPanelLayout.setHorizontalGroup(
            entityListToolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(entityListToolbarPanelLayout.createSequentialGroup()
                .addComponent(entityTitle)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(entityListToolbarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(entityListToolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(createEntityButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteEntityButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        entityListToolbarPanelLayout.setVerticalGroup(
            entityListToolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(entityListToolbarPanelLayout.createSequentialGroup()
                .addComponent(entityTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createEntityButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteEntityButton)
                .addGap(0, 26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout toolbarPanelLayout = new javax.swing.GroupLayout(toolbarPanel);
        toolbarPanel.setLayout(toolbarPanelLayout);
        toolbarPanelLayout.setHorizontalGroup(
            toolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolbarPanelLayout.createSequentialGroup()
                .addGroup(toolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(entityListToolbarPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mapToolbarPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        toolbarPanelLayout.setVerticalGroup(
            toolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolbarPanelLayout.createSequentialGroup()
                .addComponent(mapToolbarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(entityListToolbarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        toolbarSplitPane.setLeftComponent(toolbarPanel);

        javax.swing.GroupLayout mapPanelLayout = new javax.swing.GroupLayout(mapPanel);
        mapPanel.setLayout(mapPanelLayout);
        mapPanelLayout.setHorizontalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 382, Short.MAX_VALUE)
        );
        mapPanelLayout.setVerticalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 361, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout entityListPanelLayout = new javax.swing.GroupLayout(entityListPanel);
        entityListPanel.setLayout(entityListPanelLayout);
        entityListPanelLayout.setHorizontalGroup(
            entityListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 382, Short.MAX_VALUE)
        );
        entityListPanelLayout.setVerticalGroup(
            entityListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 103, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(entityListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(mapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(entityListPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        toolbarSplitPane.setRightComponent(mainPanel);

        javax.swing.GroupLayout mainBackgroundPanelLayout = new javax.swing.GroupLayout(mainBackgroundPanel);
        mainBackgroundPanel.setLayout(mainBackgroundPanelLayout);
        mainBackgroundPanelLayout.setHorizontalGroup(
            mainBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbarSplitPane) 
        );
        mainBackgroundPanelLayout.setVerticalGroup(
            mainBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbarSplitPane)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainBackgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainBackgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        frame.pack();
        return frame.getContentPane();
    }// </editor-fold>                        

                                   

    // Variables declaration - do not modify                     
    private javax.swing.JButton createEntityButton;
    private javax.swing.JButton createHallwayButton;
    private javax.swing.JButton createPortalButton;
    private javax.swing.JButton createRoomButton;
    private javax.swing.JButton createWorldButton;
    private javax.swing.JButton deleteEntityButton;
    private javax.swing.JButton deleteWorldButton;
    private javax.swing.JPanel entityListToolbarPanel;
    private javax.swing.JLabel entityTitle;
    private javax.swing.JPanel mainBackgroundPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel mapToolbarPanel;
    private javax.swing.JLabel mapToolbarTitle;
    private javax.swing.JPanel toolbarPanel;
    private javax.swing.JSplitPane toolbarSplitPane;
    // End of variables declaration                   
}

package ui.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import ui.ColorPaletteConstants;
import ui.windows.map.main.MapComponent;
/*IMPORTANT! When replacing this with new code from net beans:
 * make this extend nothing
 * make initComponents() return a Container
 * add parameters for action listeners for buttons
 * replace action listeners for each button with those
 * create a frame inside the method, add everything to that frame
 * return frame.getContentPane() 
 */
public class BackgroundUI {

	public Container initComponents(
    		ActionListener dragToolListener,
    		ActionListener createRoomListener, 
    		ActionListener createHallwayListener, 
    		ActionListener createWorldListener, 
    		ActionListener createPortalListener, 
    		ActionListener deleteWorldListener, 
    		ActionListener createEntityListener, 
    		ActionListener deleteEntityListener, 
    		final MapComponent mapPanel, 
    		JPanel worldSelectorPanel,
    		JPanel entityListPanel) {

    	JFrame frame = new JFrame();
    	
        mainBackgroundPanel = new javax.swing.JPanel();
        toolbarPanel = new javax.swing.JPanel();
        toolbarPanel.setBackground(ColorPaletteConstants.TOOLBAR_BACKGROUND);
        mapToolbarPanel = new javax.swing.JPanel();
        mapToolbarPanel.setBackground(ColorPaletteConstants.TOOLBAR_BACKGROUND);
        
        mapToolbarTitle = new javax.swing.JLabel();
        dragToolButton = new UIButton("Drag Tool", true);
        createRoomButton = new UIButton("Create Room",true);
        createHallwayButton = new UIButton("Create Hallway", true);
        createWorldButton = new UIButton("Create World", true);
        createPortalButton = new UIButton("Create Portal", true);
        deleteWorldButton = new UIButton("Delete World", false);
        entityListToolbarPanel = new javax.swing.JPanel();
        entityListToolbarPanel.setBackground(ColorPaletteConstants.TOOLBAR_BACKGROUND);
        
        entityTitle = new javax.swing.JLabel();
        createEntityButton = new UIButton("Create Entity", true);
        deleteEntityButton = new UIButton("Delete Entity", false);
        mainPanel = new javax.swing.JPanel();
        toolbarSplitPane = new javax.swing.JSplitPane();
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
        
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mapToolbarTitle.setText("Map");
        mapToolbarTitle.setHorizontalAlignment(JLabel.CENTER);
        mapToolbarTitle.setBackground(ColorPaletteConstants.LABEL_BACKGROUND);
        mapToolbarTitle.setForeground(ColorPaletteConstants.LABEL_TEXT);
        mapToolbarTitle.setOpaque(true);
        mapToolbarTitle.setBorder(new RoundedCornerBorder());
       // mapToolbarTitle.setOpaque(true);


        createRoomButton.addActionListener(createRoomListener);

        createHallwayButton.addActionListener(createHallwayListener);

        createWorldButton.addActionListener(createWorldListener);

        createPortalButton.addActionListener(createPortalListener);

        deleteWorldButton.addActionListener(deleteWorldListener);

        dragToolButton.addActionListener(dragToolListener);

        javax.swing.GroupLayout worldSelectorPanelLayout = new javax.swing.GroupLayout(worldSelectorPanel);
        worldSelectorPanel.setLayout(worldSelectorPanelLayout);
        worldSelectorPanelLayout.setHorizontalGroup(
            worldSelectorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        worldSelectorPanelLayout.setVerticalGroup(
            worldSelectorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 155, Short.MAX_VALUE)
        );
        
        javax.swing.GroupLayout mapToolbarPanelLayout = new javax.swing.GroupLayout(mapToolbarPanel);
        mapToolbarPanel.setLayout(mapToolbarPanelLayout);
        mapToolbarPanelLayout.setHorizontalGroup(
            mapToolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mapToolbarPanelLayout.createSequentialGroup()
                .addGroup(mapToolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mapToolbarTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dragToolButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createRoomButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createHallwayButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createWorldButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createPortalButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteWorldButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(worldSelectorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mapToolbarPanelLayout.setVerticalGroup(
            mapToolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mapToolbarPanelLayout.createSequentialGroup()
                .addComponent(mapToolbarTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dragToolButton)
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
        entityTitle.setHorizontalAlignment(JLabel.CENTER);
        entityTitle.setBackground(ColorPaletteConstants.LABEL_BACKGROUND);
        entityTitle.setForeground(ColorPaletteConstants.LABEL_TEXT);
        entityTitle.setOpaque(true);
        entityTitle.setBorder(new RoundedCornerBorder());


        createEntityButton.addActionListener(createEntityListener);


        deleteEntityButton.addActionListener(deleteEntityListener);

        javax.swing.GroupLayout entityListToolbarPanelLayout = new javax.swing.GroupLayout(entityListToolbarPanel);
        entityListToolbarPanel.setLayout(entityListToolbarPanelLayout);
        entityListToolbarPanelLayout.setHorizontalGroup(
            entityListToolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(entityListToolbarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(entityListToolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(entityTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private UIButton dragToolButton;
    private UIButton createEntityButton;
    private UIButton createHallwayButton;
    private UIButton createPortalButton;
    private UIButton createRoomButton;
    private UIButton  createWorldButton;
    private UIButton deleteEntityButton;
    private UIButton deleteWorldButton;
    private javax.swing.JPanel entityListToolbarPanel;
    private javax.swing.JLabel entityTitle;
    private javax.swing.JPanel mainBackgroundPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel mapToolbarPanel;
    private javax.swing.JLabel mapToolbarTitle;
    private javax.swing.JPanel toolbarPanel;
    private javax.swing.JSplitPane toolbarSplitPane;
    // End of variables declaration                   
    
    class RoundedCornerBorder extends AbstractBorder {
    	  @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    	    Graphics2D g2 = (Graphics2D)g.create();
    	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	    int r = height-10;
    	    RoundRectangle2D round = new RoundRectangle2D.Float(x, y, width-1, height-1, r, r);
    	    Container parent = c.getParent();
    	    if(parent!=null) {
    	      g2.setColor(parent.getBackground());
    	      Area corner = new Area(new Rectangle2D.Float(x, y, width, height));
    	      corner.subtract(new Area(round));
    	      g2.fill(corner);
    	    }
    	    g2.setColor(Color.GRAY);
    	    g2.draw(round);
    	    g2.dispose();
    	  }
    	  @Override public Insets getBorderInsets(Component c) {
    	    return new Insets(4, 8, 4, 8);
    	  }
    	  @Override public Insets getBorderInsets(Component c, Insets insets) {
    	    insets.left = insets.right = 8;
    	    insets.top = insets.bottom = 4;
    	    return insets;
    	  }
}
}

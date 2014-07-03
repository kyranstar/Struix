package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ui.windows.BackgroundUI;
import ui.windows.WindowComponent;
import ui.windows.map.MapComponent;
import ui.windows.map.MapRoom;

public class CreatorUI extends JFrame{
	
	private static final long serialVersionUID = 1L;

	private JPanel moveablePane = new JPanel();
	private Container backgroundPane;
	private JLayeredPane layers = new JLayeredPane();
	private ComponentMover componentMover = new ComponentMover();
	private ComponentResizer componentResizer = new ComponentResizer();
	
	private MapComponent mapPanel;
	
	public CreatorUI(){

		componentMover.setAutoLayout(true);
		
		ActionListener createRoomListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				MapRoom mapRoom = new MapRoom(Color.RED, new Point(0, 0));
				//mapRoom.setSize(100, 100);
				mapPanel.addAndCenterRoom(mapRoom);
			}};
		ActionListener createHallwayListener = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			};
		ActionListener createWorldListener = new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
				 
			};
		ActionListener createPortalListener = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			 };
			
		ActionListener deleteWorldListener = new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						
					}
				 }; 
		ActionListener createEntityListener = new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							
						}
					 };
		ActionListener deleteEntityListener = new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						
					}
		};
		layers.addComponentListener(new ComponentListener(){
			public void componentHidden(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			
			public void componentResized(ComponentEvent e) {
				backgroundPane.setSize(getSize());
				moveablePane.setSize(getSize());
			}
		});
			 
		mapPanel = new MapComponent();
		JPanel worldSelectorPanel = new JPanel();
		worldSelectorPanel.setBackground(new Color(255,0,100,100));
		JPanel entityListPanel = new JPanel();
		entityListPanel.setBackground(new Color(0,100,255,100));
		
		backgroundPane = new BackgroundUI().initComponents(createRoomListener, 
				createHallwayListener, 
				createWorldListener, 
				createPortalListener,
				deleteWorldListener, 
				createEntityListener, 
				deleteEntityListener, 
				mapPanel, 
				worldSelectorPanel, 
				entityListPanel);
		
		moveablePane.setLayout(null);
		moveablePane.setOpaque(false);
		
		layers.setLayout(new BorderLayout());


		layers.setLayer(moveablePane, JLayeredPane.DRAG_LAYER);
		layers.add(moveablePane, BorderLayout.CENTER);
		
		layers.setLayer(backgroundPane, JLayeredPane.DEFAULT_LAYER);
		layers.add(backgroundPane, BorderLayout.CENTER);

		
		add(layers, BorderLayout.CENTER);
		layers.setPreferredSize(backgroundPane.getPreferredSize());
		
		pack();
	}
	private static void setPLAF(String name){
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (name.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BackgroundUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BackgroundUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BackgroundUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BackgroundUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
	}
	
	public void addWindow(WindowComponent window){
		this.componentMover.registerComponent(window);
		this.componentResizer.registerComponent(window);
		
		this.componentMover.setDragInsets(this.componentResizer.getDragInsets());
		
		this.moveablePane.add(window);
	}
	
	
	public static void main(String[] args){
		setPLAF("Nimbus");
		
		CreatorUI frame = new CreatorUI();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
}

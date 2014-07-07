package ui.windows;

import java.awt.Font;

import javax.swing.JButton;

import ui.ColorPaletteConstants;

public class UIButton extends JButton{
	private static final long serialVersionUID = 1L;

	public static final Font BUTTON_FONT = new Font("Forte", Font.PLAIN, 13);
	
	public UIButton(String text, boolean positive){
		super(text);
		if(positive){
			this.setBackground(ColorPaletteConstants.BUTTON_POSITIVE);
			this.setForeground(ColorPaletteConstants.BUTTON_POSITIVE_TEXT);
		}else{
			this.setBackground(ColorPaletteConstants.BUTTON_NEGATIVE);
			this.setForeground(ColorPaletteConstants.BUTTON_NEGATIVE_TEXT);
		}
		setFont(BUTTON_FONT); 
		setSize(getPreferredSize());
		setFocusPainted(false);
	}
}

package ui.windows;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;

import ui.ColorPaletteConstants;

public class UIButton extends JButton {
	private static final long serialVersionUID = 1L;

	private static final Font BUTTON_FONT;
	private static final float FONT_SIZE = 13;

	private static final String BUTTON_FONT_FILEPATH = "/fonts/Aller_Rg.ttf";

	static {
		try {
			InputStream is = UIButton.class.getResourceAsStream(BUTTON_FONT_FILEPATH);
			if (is == null) {
				throw new IOException("file not found");
			}
			BUTTON_FONT = Font.createFont(Font.TRUETYPE_FONT,
					new BufferedInputStream(is)).deriveFont(FONT_SIZE);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(BUTTON_FONT);
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		} catch (FontFormatException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public UIButton(String text, boolean positive) {
		super(text);
		if (positive) {
			this.setBackground(ColorPaletteConstants.BUTTON_POSITIVE);
			this.setForeground(ColorPaletteConstants.BUTTON_POSITIVE_TEXT);
		} else {
			this.setBackground(ColorPaletteConstants.BUTTON_NEGATIVE);
			this.setForeground(ColorPaletteConstants.BUTTON_NEGATIVE_TEXT);
		}
		setFont(BUTTON_FONT);
		setSize(getPreferredSize());
		setFocusPainted(false);
	}

	public UIButton(boolean positive) {
		super();
		if (positive) {
			this.setBackground(ColorPaletteConstants.BUTTON_POSITIVE);
			this.setForeground(ColorPaletteConstants.BUTTON_POSITIVE_TEXT);
		} else {
			this.setBackground(ColorPaletteConstants.BUTTON_NEGATIVE);
			this.setForeground(ColorPaletteConstants.BUTTON_NEGATIVE_TEXT);
		}
		setFont(BUTTON_FONT);
		setSize(getPreferredSize());
		setFocusPainted(false);
	}
}

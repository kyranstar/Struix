package ui.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JLabel;
import javax.swing.border.AbstractBorder;

import ui.ColorPaletteConstants;

public class UILabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private static final Font BUTTON_FONT;
	private static final float FONT_SIZE = 13;

	private static final String BUTTON_FONT_FILEPATH = "/fonts/Aller_Rg.ttf";

	static {
		try {
			InputStream is = UIButton.class
					.getResourceAsStream(BUTTON_FONT_FILEPATH);
			if (is == null) {
				throw new IOException("file not found");
			}
			BUTTON_FONT = Font.createFont(Font.TRUETYPE_FONT,
					new BufferedInputStream(is)).deriveFont(FONT_SIZE);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(
					BUTTON_FONT);
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		} catch (FontFormatException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public UILabel(String text, boolean positive) {
		super(text);
		if (positive) {
			setBackground(ColorPaletteConstants.LABEL_BACKGROUND_POSITIVE);
			setForeground(ColorPaletteConstants.LABEL_TEXT_POSITIVE);
		} else {
			this.setBackground(ColorPaletteConstants.LABEL_BACKGROUND_NEGATIVE);
			this.setForeground(ColorPaletteConstants.LABEL_TEXT_NEGATIVE);
		}
		setFont(BUTTON_FONT);
		setSize(getPreferredSize());
		setHorizontalAlignment(JLabel.CENTER);
		setOpaque(true);
		setBorder(new RoundedCornerBorder(ColorPaletteConstants.LABEL_BORDER));
	}

	public UILabel(boolean positive) {
		super();
		if (positive) {
			setBackground(ColorPaletteConstants.LABEL_BACKGROUND_POSITIVE);
			setForeground(ColorPaletteConstants.LABEL_TEXT_POSITIVE);
		} else {
			this.setBackground(ColorPaletteConstants.LABEL_BACKGROUND_NEGATIVE);
			this.setForeground(ColorPaletteConstants.LABEL_TEXT_NEGATIVE);
		}
		setFont(BUTTON_FONT);
		setSize(getPreferredSize());
		setHorizontalAlignment(JLabel.CENTER);
		setOpaque(true);
		setBorder(new RoundedCornerBorder(ColorPaletteConstants.LABEL_BORDER));
	}

	static class RoundedCornerBorder extends AbstractBorder {
		private static final long serialVersionUID = 1L;

		private final Color color;
		
		public RoundedCornerBorder(Color color){
			super();
			this.color = color;
		}
		
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int r = height - 10;
			RoundRectangle2D round = new RoundRectangle2D.Float(x, y,
					width - 1, height - 1, r, r);
			Container parent = c.getParent();
			if (parent != null) {
				g2.setColor(parent.getBackground());
				Area corner = new Area(new Rectangle2D.Float(x, y, width,
						height));
				corner.subtract(new Area(round));
				g2.fill(corner);
			}
			g2.setColor(this.color);
			g2.draw(round);
			g2.dispose();
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(4, 8, 4, 8);
		}

		@Override
		public Insets getBorderInsets(Component c, Insets insets) {
			insets.left = insets.right = 8;
			insets.top = insets.bottom = 4;
			return insets;
		}
	}
}

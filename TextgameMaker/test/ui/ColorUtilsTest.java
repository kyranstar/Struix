package ui;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;


public class ColorUtilsTest {

	@Test
		public void testFindHighContrast() {
			Color color = new Color(102,153,51);
			Color expected = new Color(102,51,153);
			assertEquals(expected, ColorUtils.findHighContrast(color));
		}

}

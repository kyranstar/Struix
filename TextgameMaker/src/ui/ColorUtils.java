package ui;

import java.awt.Color;
import java.util.Random;

public final class ColorUtils {
	private static Random random = new Random();
	public static Color getRandomSimilarColor(Color c, int similarity){
		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		hsb[0] += random.nextFloat() * similarity;
		hsb[1] += random.nextFloat() * similarity;
		hsb[2] += random.nextFloat() * similarity;
		return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
	}
	public static Color generateRandomColor(final Color mix) {
	    int red = random.nextInt(256);
	    int green = random.nextInt(256);
	    int blue = random.nextInt(256);
	    
	    // mix the color
	    if (mix != null) {
	        red = (red + mix.getRed()) / 2;
	        green = (green + mix.getGreen()) / 2;
	        blue = (blue + mix.getBlue()) / 2;
	    }

	    return new Color(red, green, blue);
	}
	public static Color mapSaturation(final Color initial, float saturation){
		float[] hsb = Color.RGBtoHSB(initial.getRed(), initial.getGreen(), initial.getBlue(), null);
		hsb[1] = saturation;
		return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	}
	public static Color mapBrightness(final Color initial, float brightness){
		float[] hsb = Color.RGBtoHSB(initial.getRed(), initial.getGreen(), initial.getBlue(), null);
		hsb[2] = brightness;
		return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	}
}

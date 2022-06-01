package net.mattbenson.utils.legacy;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class ColorMap {
	public static HashMap<String, Color> colors;
	public static ColorMap INSTANCE;

	public ColorMap() {
		ColorMap.colors.put("Black", Color.BLACK);
		ColorMap.colors.put("White", Color.WHITE);
		ColorMap.colors.put("Light Gray", Color.LIGHT_GRAY);
		ColorMap.colors.put("Gray", Color.GRAY);
		ColorMap.colors.put("Dark Gray", Color.DARK_GRAY);
		ColorMap.colors.put("Red", Color.RED);
		ColorMap.colors.put("Pink", Color.PINK);
		ColorMap.colors.put("Orange", Color.ORANGE);
		ColorMap.colors.put("Yellow", Color.YELLOW);
		ColorMap.colors.put("Green", Color.GREEN);
		ColorMap.colors.put("Magenta", Color.MAGENTA);
		ColorMap.colors.put("Cyan", Color.CYAN);
		ColorMap.colors.put("Blue", Color.BLUE);
	}

	public static ArrayList<String> getColorArray() {
		final ArrayList<String> stringArrayList = new ArrayList<String>();
		for (final String key : ColorMap.colors.keySet()) {
			stringArrayList.add(key);
		}
		return stringArrayList;
	}

	static {
        ColorMap.colors = new HashMap<String, Color>();
        ColorMap.INSTANCE = new ColorMap();
    }
}

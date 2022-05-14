package mapwriter.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ColorCodes {
	static Map<String, String> colorCombos = new HashMap<String, String>();

	static {
		colorCombos.put("4", "be0000");
		colorCombos.put("c", "fe3f3f");
		colorCombos.put("6", "D9A334");
		colorCombos.put("e", "fefe3f");
		colorCombos.put("2", "00be00");
		colorCombos.put("a", "3ffe3f");
		colorCombos.put("b", "3ffefe");
		colorCombos.put("3", "00bebe");
		colorCombos.put("1", "0000be");
		colorCombos.put("9", "3f3ffe");
		colorCombos.put("d", "fe3ffe");
		colorCombos.put("5", "be00be");
		colorCombos.put("f", "ffffff");
		colorCombos.put("7", "bebebe");
		colorCombos.put("8", "3f3f3f");
		colorCombos.put("0", "000000");
	}

	public static Collection<String> getColors() {
		return colorCombos.values();
	}

	public static String getColor(String code) {
		code = code.replace("\u00A7", "");
		return colorCombos.get(code);
	}
}
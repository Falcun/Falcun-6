package falcun.net.api.colors;

public enum FalcunChatColor {
	BLACK("0"), DARK_BLUE("1"), DARK_GREEN("2"), DARK_AQUA("3"), DARK_RED("4"), DARK_PURPLE("5"), GOLD("6"), GRAY("7"), DARK_GRAY("8"), INDIGO("9"), GREEN("A"), AQUA("B"), RED("C"), PINK("D"), YELLOW("E"), WHITE("F"), STRIKE_THROUGH("M"), UNDRLINE("N"), BOLD("L"), RANDOM("K"), ITALIC("O"), RESET("R");

	private String colorCode;
	FalcunChatColor(String colorCode) {
		this.colorCode = colorCode;
	}

	@Override
	public String toString() {
		return "\247" + colorCode;
	}

	public String getColorSuffix() {
		return colorCode;
	}

	public static String stripColors(String string) {
		String finalStr = string;

		for(FalcunChatColor color : FalcunChatColor.values()) {
			finalStr = finalStr.replaceAll("(?i)§" + color.getColorSuffix(), "");
		}

		return finalStr;
	}
}
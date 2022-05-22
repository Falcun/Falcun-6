package falcun.net.api.fonts;

public interface FalcunFont {

	void drawString(String text, Number x, Number y, int color, boolean underline);

	Number getStringWidth(String text);

	Number stringHeight(String text);

	default Number stringHeight(){
		return 9;
	}

}

package falcun.net.api.fonts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface FalcunFont {

	void drawString(String text, Number x, Number y, int color, boolean underline);

	Number getStringWidth(String text);

	Number stringHeight(String text);

	default Number stringHeight() {
		return 9;
	}

	List<String> getLinesWrapped(String text, int maxWid);

	int size();

}

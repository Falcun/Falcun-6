package falcun.net.api.gui.components.text;


import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.gui.components.ColoredComponent;
import falcun.net.api.gui.region.GuiRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Paragraph extends ColoredComponent {

	public FalcunFont font = Fonts.MC;
	public boolean underline = false;

	String txt;

	public Paragraph(GuiRegion region, Supplier<Integer> color, String text) {
		super(region, color);
		this.txt = text;
	}

	public Paragraph(GuiRegion region, Supplier<Integer> color, String text, FalcunFont font) {
		this(region, color, text);
		this.font = font;
	}

	public int getY() {
		return 2 + region.y + (region.height - (int) font.stringHeight()) / 2;
	}

	@Override
	public void draw(int mX, int mY) {
		int y = getY() - 2;
		int yOffset = -font.size();
		int maxwid = (int) (region.width * 0.8);

		List<String> lines = font.getLinesWrapped(txt, maxwid);
		if (lines.size() > 1) {
			y += (yOffset * lines.size()) >> 2;
		}
		for (String line : lines) {
			yOffset += font.size();
			font.drawString(line, 2 + (region.getMidX() - (int) font.getStringWidth(line) / 2), y + yOffset, color.get(), underline);
		}

	}


}
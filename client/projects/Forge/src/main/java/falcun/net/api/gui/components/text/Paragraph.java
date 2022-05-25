package falcun.net.api.gui.components.text;


import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.gui.components.ColoredComponent;
import falcun.net.api.gui.region.GuiRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Paragraph extends ColoredComponent {

	List<String> lines;
	public FalcunFont font = Fonts.MC;

	public Paragraph(GuiRegion region, Supplier<Integer> color, String text) {
		super(region, color);
		lines = trimSentenceToWidth(text);
		region.height = lines.size() * 13;
	}

	public Paragraph(GuiRegion region, Supplier<Integer> color, String text, FalcunFont font) {
		this(region, color, text);
		this.font = font;
	}

	@Override
	public void draw(int mX, int mY) {
		int y = region.y;
		for (String line : lines) {
			font.drawString(line, region.getMidX() - (int)font.getStringWidth(line) / 2, y, color.get(), false);
//			font.drawString(line, region.x, y, color.get(), false);
			y += (int) font.stringHeight(line);
		}
	}

	public List<String> trimSentenceToWidth(String text) {
		final List<String> trimmedSentence = new ArrayList<>();
		String[] args = text.split(" ");

		StringBuilder stringBuilder = new StringBuilder();

		for (String arg : args) {
			final int currentWidth = (int) font.getStringWidth(stringBuilder.toString());
			int argLength = (int) font.getStringWidth(arg + " ");
			if (currentWidth + argLength >= this.region.width) {
				trimmedSentence.add(stringBuilder.toString());
				stringBuilder.delete(0, stringBuilder.length());
				stringBuilder.append(" ").append(arg);
				continue;
			}

			stringBuilder.append(" ").append(arg);
		}
		trimmedSentence.add(stringBuilder.toString());


		return trimmedSentence;

	}

}
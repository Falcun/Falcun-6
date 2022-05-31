package falcun.net.api.gui.components.text;

import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.gui.components.Component;
import falcun.net.api.gui.region.GuiRegion;

import java.util.List;
import java.util.function.Supplier;

public class Label extends Component {

	public static final int LEFT = 0, CENTER = 1, RIGHT = 2;

	public Supplier<String> text;
	public Supplier<Integer> alignment;
	public Supplier<Integer> color;
	public FalcunFont font = Fonts.MC;
	public boolean underline = false;

	public Label(GuiRegion region, Supplier<String> text, int alignment, Supplier<Integer> color) {
		super(region);
		this.text = text;
		this.alignment = () -> alignment;
		this.color = color;
	}

	public Label(GuiRegion region, Supplier<String> text, int alignment, Supplier<Integer> color, FalcunFont font) {
		super(region);
		this.text = text;
		this.alignment = () -> alignment;
		this.color = color;
		this.font = font;
	}

	public Label(GuiRegion region, Supplier<String> text, Supplier<Integer> alignment, Supplier<Integer> color) {
		super(region);
		this.text = text;
		this.alignment = alignment;
		this.color = color;
	}

	public Label(GuiRegion region, Supplier<String> text, Supplier<Integer> alignment, Supplier<Integer> color, FalcunFont font) {
		super(region);
		this.text = text;
		this.alignment = alignment;
		this.color = color;
		this.font = font;
	}

	public Label(GuiRegion region, String text, int alignment, int color) {
		this(region, () -> text, alignment, () -> color);
	}

	public Label(GuiRegion region, String text, int alignment, int color, FalcunFont font) {
		this(region, () -> text, alignment, () -> color, font);
	}

	@Override
	public void draw(int mx, int my) {
		int y = getY() - 2;
		int yOffset = -font.size();

		if (true) {

			switch (alignment.get()) {
				case LEFT:
					font.drawString(text.get(), region.x + 1, y, color.get(), underline);
					break;
				case CENTER:
					font.drawString(text.get(), 2 + (region.getMidX() - (int) font.getStringWidth(text.get()) / 2), y, color.get(), underline);
					break;
				case RIGHT:
					font.drawString(text.get(), region.getRight() - (int) font.getStringWidth(text.get()), y, color.get(), underline);
					break;
			}

return;
		}

		int maxwid = (int) (region.width * 0.8);

		List<String> lines = font.getLinesWrapped(text.get(), maxwid);
		if (lines.size() > 1) {
			y += (yOffset * lines.size()) >> 2;
		}
		for (String line : lines) {
			yOffset += font.size();
			switch (alignment.get()) {
				case LEFT:
					font.drawString(line, region.x + 1, y + yOffset, color.get(), underline);
					continue;
				case CENTER:
					font.drawString(line, 2 + (region.getMidX() - (int) font.getStringWidth(line) / 2), y + yOffset, color.get(), underline);
					continue;
				case RIGHT:
					font.drawString(line, region.getRight() - (int) font.getStringWidth(line), y + yOffset, color.get(), underline);
					continue;
			}


			//			switch (alignment.get()) {
			//				case LEFT:
			//					font.drawString(text.get(), region.x + 1, y, color.get(), underline);
			//					break;
			//				case CENTER:
			//					font.drawString(text.get(), 2 + (region.getMidX() - (int) font.getStringWidth(text.get()) / 2), y, color.get(), underline);
			//					break;
			//				case RIGHT:
			//					font.drawString(text.get(), region.getRight() - (int) font.getStringWidth(text.get()), y, color.get(), underline);
			//					break;
			//			}
		}

	}

	public int getY() {
		return 2 + region.y + (region.height - (int) font.stringHeight()) / 2;
	}

	public GuiRegion tighten() {
		int left = 0;
		int width = (int) font.getStringWidth(text.get());
		switch (alignment.get()) {
			case LEFT:
				left = region.x;
				break;
			case CENTER:
				left = region.getMidX() - (int) font.getStringWidth(text.get()) / 2;

				break;
			case RIGHT:
				left = region.getRight() - (int) font.getStringWidth(text.get());

				break;
		}
		return new GuiRegion(left, region.y, width, 9);
	}

}

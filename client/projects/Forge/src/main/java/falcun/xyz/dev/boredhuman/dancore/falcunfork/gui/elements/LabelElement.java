package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements;

import falcun.net.api.fonts.FalcunFont;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Alignment;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Colors;

import java.util.List;

public class LabelElement extends BasicElement<LabelElement> {

	public String text;
	public FalcunFont font;
	Alignment.Horizontal horizontalAlignment = Alignment.Horizontal.CENTER;
	Alignment.Vertical verticalAlignment = Alignment.Vertical.CENTER;
	boolean italic = false;

	public LabelElement(String text, FalcunFont font) {
		this.text = text;
		this.font = font;
		this.color = Colors.WHITE.getIntColor();
	}

	@Override
	public void render() {
		if (text == null || text.isEmpty()) {
			return;
		}
		int yOffset = 0;

		List<String> lines = this.font.getLinesWrapped(this.text, this.width);

		switch (this.verticalAlignment) {
			case TOP:
				yOffset = this.y;
				break;
			case CENTER:
				yOffset = this.y + this.height / 2 - (this.font.size() / 2 * lines.size());
				break;
			case BOTTOM:
				yOffset = this.y + this.height - this.font.size();
				break;
		}

		for (String line : lines) {
			switch (this.horizontalAlignment) {
				case LEFT:
					this.font.drawString(line, this.x, yOffset, this.color, this.italic);
					break;
				case RIGHT:
					this.font.drawString(line, this.x + this.width - (int)this.font.getStringWidth(this.text), yOffset, this.color, this.italic);
					break;
				case CENTER:
//					this.font.drawCenteredString(line, this.x + this.width / 2, yOffset, this.color, this.italic);
					this.font.drawString(line, (this.x + this.width) - (int) font.getStringWidth(line), y, this.color, this.italic);
					break;
			}
			yOffset += this.font.size();
		}
	}

	public LabelElement setText(String text) {
		this.text = text;
		return this;
	}

	public LabelElement setHorizontalAlignment(Alignment.Horizontal alignment) {
		this.horizontalAlignment = alignment;
		return this;
	}

	public LabelElement setVerticalAlignment(Alignment.Vertical alignment) {
		this.verticalAlignment = alignment;
		return this;
	}
}
package falcun.net.api.dangui.elements;

import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.dangui.util.Horizontal;
import falcun.net.api.dangui.util.Vertical;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.fonts.DanFont;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

import java.util.List;

public class FalcunLabel extends BasicElement<FalcunLabel> {

	public String text;
	public FalcunFont font = Fonts.RobotoMiniHeader;

	public boolean underline = false;

	public Vertical vertical = Vertical.CENTER;
	public Horizontal horizontal = Horizontal.CENTER;

	public FalcunLabel(String text, FalcunFont font) {
		this.text = text;
		this.font = font;
	}

	public FalcunLabel(String text) {
		this.text = text;
	}

//	public int getY() {
//		return 2 + region.y + (region.height - (int) font.stringHeight()) / 2;
//	}

	@Override
	public void render() {
		if (text == null || text.isEmpty()) {
			return;
		}
		int yOffset = 0;

		List<String> lines = this.font.getLinesWrapped(this.text, this.width);

		switch (horizontal.alignment) {
			case 0:
				yOffset = this.y;
				break;
			case 1:
//				yOffset = this.y + (this.height >> 1) - (this.font.size() / 2 * lines.size());
				yOffset = 2 + this.y + ((this.height - (int)font.stringHeight()) >> 1);
				break;
			case 2:
				yOffset = this.y + this.height - this.font.size();
				break;
			default:
				break;
		}
		int midx = this.x + (this.width >> 1);
		for (String line : lines) {
			switch (this.horizontal.alignment) {
				case 0:
					this.font.drawString(line, this.x, yOffset, this.color, this.underline);
					break;
				case 2:
					this.font.drawString(line, this.x + this.width - (int) this.font.getStringWidth(this.text), yOffset, this.color, this.underline);
					break;
				case 1: //	font.drawString(text.get(), 2 + (region.getMidX() - (int) font.getStringWidth(text.get()) / 2), y, color.get(), underline);
					if (this.font instanceof DanFont) {
						((DanFont) this.font).drawCenteredString(line, this.x + this.width / 2, yOffset, this.color, false);
					} else
//						this.font.drawString(line, (this.x + this.width) - (int) font.getStringWidth(line), y, this.color, this.underline);
						this.font.drawString(line, 2 + midx - ((int) font.getStringWidth(line) >> 1), y, this.color, this.underline);
					break;
			}
			yOffset += this.font.size();
		}
	}

	public FalcunLabel setVert(int vert) {
		this.vertical = Vertical.getAlignment(vert);
		return this;
	}

	public FalcunLabel setVert(Vertical vert) {
		this.vertical = Vertical.getAlignment(vert);
		return this;
	}

	public FalcunLabel setHoriz(int vert) {
		this.horizontal = Horizontal.getAlignment(vert);
		return this;
	}

	public FalcunLabel setHoriz(Horizontal vert) {
		this.horizontal = Horizontal.getAlignment(vert);
		return this;
	}

	public FalcunLabel setText(String text) {
		this.text = text;
		return this;
	}

	public FalcunLabel setFont(FalcunFont font) {
		this.font = font;
		return this;
	}

}

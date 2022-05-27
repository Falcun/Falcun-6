package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.ColorUtil;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.GuiUtil;
import net.minecraft.client.renderer.GlStateManager;

public class ContainerElement extends BasicElement<ContainerElement> {

	public int rounding = 0;

	public ContainerElement setRounding(int rounding) {
		this.rounding = rounding;
		return this;
	}

	@Override
	public void render() {
		if (this.color == 0) {
			return;
		}
		if (this.rounding <= 0) {
			GlStateManager.color(ColorUtil.getRed(this.color), ColorUtil.getGreen(this.color), ColorUtil.getBlue(this.color), ColorUtil.getAlpha(this.color));
			GuiUtil.drawSquare(this.x, this.y, this.width, this.height);
		} else {
			GuiUtil.drawRoundedQuad(this.x, this.y, this.width, this.height, this.rounding, this.color);
		}
	}
}
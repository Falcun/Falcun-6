package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.ColorUtil;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.GuiUtil;
import net.minecraft.client.renderer.GlStateManager;

public class OutlineElement<T extends OutlineElement<T>> extends BasicElement<T> {

	private int lineWidth = 2;

	@Override
	public void render() {
		int rounding = 0;
		if (this.parentElement instanceof ContainerElement) {
			rounding = ((ContainerElement) this.parentElement).rounding;
		}
		this.x = this.parentElement.x;
		this.y = this.parentElement.y;
		this.width = this.parentElement.width;
		this.height = this.parentElement.height;
		if (rounding <= 0) {
			GlStateManager.color(ColorUtil.getRed(this.color), ColorUtil.getGreen(this.color), ColorUtil.getBlue(this.color), ColorUtil.getAlpha(this.color));
			GuiUtil.drawSquare(this.x, this.y, this.width, this.lineWidth);
			GuiUtil.drawSquare(this.x, this.y + this.height - this.lineWidth, this.width, this.lineWidth);
			GuiUtil.drawSquare(this.x, this.y, this.lineWidth, this.height);
			GuiUtil.drawSquare(this.x + this.width - this.lineWidth, this.y, this.lineWidth, this.height);
		} else {
			GlStateManager.color(ColorUtil.getRed(this.color), ColorUtil.getGreen(this.color), ColorUtil.getBlue(this.color), ColorUtil.getAlpha(this.color));
			GuiUtil.drawSquare(this.x + rounding, this.y, this.width - rounding * 2, this.lineWidth);
			GuiUtil.drawSquare(this.x + rounding, this.y + this.height - this.lineWidth, this.width - rounding * 2, this.lineWidth);
			GuiUtil.drawSquare(this.x, this.y + rounding, this.lineWidth, this.height - rounding * 2);
			GuiUtil.drawSquare(this.x + this.width - this.lineWidth, this.y + rounding, this.lineWidth, this.height - rounding * 2);
			GuiUtil.drawRoundedQuadOutline(this.x, this.y, this.width, this.height, rounding, this.color, this.lineWidth);
		}
	}

	public T setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		return (T) this;
	}

	@Override
	public boolean shouldRender() {
		return this.visible && this.parentElement instanceof ContainerElement && this.parentElement.shouldRender() && (this.shouldRender == null || this.shouldRender.apply(this));
	}
}
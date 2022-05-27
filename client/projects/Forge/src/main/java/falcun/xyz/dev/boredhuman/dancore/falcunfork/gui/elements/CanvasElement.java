package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.GuiUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class CanvasElement extends BasicElement<CanvasElement> {

	DynamicTexture dynamicTexture;

	int lastWidth, lastHeight;

	@Override
	public void render() {
		if (this.width != this.lastWidth || this.height != this.lastHeight) {
			this.dynamicTexture = new DynamicTexture(this.width, this.height);
			this.lastWidth = this.width;
			this.lastHeight = this.height;
		}
		GlStateManager.color(1F, 1F, 1F, 1F);
		GuiUtil.drawSquare(this.x, this.y, this.width, this.height);
		if (this.dynamicTexture != null) {
			GlStateManager.bindTexture(this.dynamicTexture.getGlTextureId());
			GuiUtil.drawSquareTextured(this.x, this.y, this.width, this.height);
		}
	}
}
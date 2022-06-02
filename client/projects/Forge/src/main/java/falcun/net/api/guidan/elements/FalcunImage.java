package falcun.net.api.guidan.elements;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.ColorUtil;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class FalcunImage<T extends BasicElement<T>> extends BasicElement<T> {


	public ResourceLocation resourceLocation;

	public FalcunImage(ResourceLocation resourceLocation) {
		this.resourceLocation = resourceLocation;
		this.setColor(0xffffffff);
	}

	public void render() {
		GlStateManager.pushMatrix();
		GlStateManager.color(ColorUtil.getRed(this.color), ColorUtil.getGreen(this.color), ColorUtil.getBlue(this.color), ColorUtil.getAlpha(this.color));
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		GuiUtil.drawTextureSquare(this.x, this.y, this.width, this.height);
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

}

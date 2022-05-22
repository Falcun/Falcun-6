package falcun.net.api.gui.components.texture;

import falcun.net.api.gui.region.GuiRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class TextureSquareColored extends TextureSquare {
	public TextureSquareColored(GuiRegion region, ResourceLocation resourceLocation) {
		super(region, resourceLocation);
	}

	public TextureSquareColored(GuiRegion region, String resourceLocation) {
		super(region, resourceLocation);
	}

	public float a, r, g, b;

	public TextureSquareColored(GuiRegion gr, ResourceLocation loc, float a, float r, float g, float b) {
		super(gr, loc);
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public TextureSquareColored(GuiRegion gr, String loc, float a, float r, float g, float b) {
		super(gr, loc);
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	@Override
	public void draw(int mX, int mY) {
		GlStateManager.pushMatrix();
		GlStateManager.color(r, g, b, a);
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		Gui.drawModalRectWithCustomSizedTexture(region.x, region.y, 0, 0, region.width, region.height, region.width, region.height);
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
}

package falcun.net.api.guidragon.components.texture;

import falcun.net.api.guidragon.components.Component;
import falcun.net.api.guidragon.region.GuiRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class TextureSquare extends Component {

	public ResourceLocation resourceLocation;
	public boolean lowerColor = false;

	public TextureSquare(GuiRegion region, ResourceLocation resourceLocation) {
		super(region);
		this.resourceLocation = resourceLocation;
	}

	public TextureSquare(GuiRegion region, String resourceLocation) {
		this(region, new ResourceLocation(resourceLocation));
	}

	@Override
	public void draw(int mX, int mY) {
		GlStateManager.pushMatrix();
		if (lowerColor) {
			GlStateManager.color(0.7f, 0.7f, 0.7f, 0.7F);
		} else GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		Gui.drawModalRectWithCustomSizedTexture(region.x, region.y, 0, 0, region.width, region.height, region.width, region.height);
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

}
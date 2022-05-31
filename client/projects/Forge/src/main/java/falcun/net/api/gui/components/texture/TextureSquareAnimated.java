package falcun.net.api.gui.components.texture;

import falcun.net.api.gui.components.Component;
import falcun.net.api.gui.region.GuiRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class TextureSquareAnimated extends Component {

	public boolean lowerColor = false;
	int index = 0;
	public long timeBetweenImages;

	ResourceLocation[] images;

	public TextureSquareAnimated(GuiRegion region, long timeBetweenImages, ResourceLocation... resourceLocation) {
		super(region);
		images = resourceLocation;
		this.timeBetweenImages = timeBetweenImages;
	}

	long now = System.currentTimeMillis();

	@Override
	public void draw(int mX, int mY) {
		if (System.currentTimeMillis() - now > timeBetweenImages) {
			++index;
			if (index >= images.length) {
				index = 0;
			}
			now = System.currentTimeMillis();
		}
		GlStateManager.pushMatrix();
		if (lowerColor) {
			GlStateManager.color(0.7F, 0.7F, 0.7F, 0.7F);
		} else GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(images[index]);
		Gui.drawModalRectWithCustomSizedTexture(region.x, region.y, 0, 0, region.width, region.height, region.width, region.height);
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
}

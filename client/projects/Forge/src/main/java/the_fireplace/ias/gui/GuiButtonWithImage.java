package the_fireplace.ias.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
/**
 * The button with the image on it.
 * @author The_Fireplace
 */
public class GuiButtonWithImage extends GuiButton {
	private static final ResourceLocation customButtonTextures = new ResourceLocation("ias", "textures/gui/custombutton.png");
	public final Runnable action;
	public GuiButtonWithImage(int x, int y, Runnable p) {
		super(104027, x, y, 20, 20, "ButterDog");
		this.action = p;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			mc.getTextureManager().bindTexture(customButtonTextures);
			GlStateManager.color(1F, 1F, 1F, 1F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int k = getHoverState(hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			drawTexturedModalRect(this.xPosition, this.yPosition, 0, k * 20, 20, 20);
		}
	}

	public void click() {
		action.run();
	}
}

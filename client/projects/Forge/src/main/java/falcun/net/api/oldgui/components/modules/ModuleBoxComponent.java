package falcun.net.api.oldgui.components.modules;

import falcun.net.api.fonts.Fonts;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.oldgui.components.Component;
import falcun.net.api.oldgui.components.text.Label;
import falcun.net.api.oldgui.region.GuiRegion;
import falcun.net.api.textures.FalcunTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class ModuleBoxComponent extends Component {

	public boolean lowerColor = false;
	public final FalcunModule module;
	public final Label label;

	public ModuleBoxComponent(GuiRegion region, FalcunModule mod) {
		super(region);
		this.module = mod;
		label = new Label(region, mod.getName().toUpperCase(), 1, 0xffffffff, Fonts.Roboto);
		this.subComponents.add(label);
		this.label.pinTo(this);
	}

	@Override
	public void draw(int mX, int mY) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(module.isEnabled() ? FalcunTexture.moduleBoxToggle[0] : FalcunTexture.moduleBoxToggle[1]);
		Gui.drawModalRectWithCustomSizedTexture(region.x, region.y, 0, 0, region.width, region.height, region.width, region.height);
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
}

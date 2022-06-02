package falcun.net.api.guidragon.components.modules;

import falcun.net.Falcun;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.guidragon.components.Component;
import falcun.net.api.guidragon.components.text.Paragraph;
import falcun.net.api.guidragon.region.GuiRegion;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.textures.FalcunTexture;
import falcun.net.guidragonclient.ingame.mods.FalcunModPage;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class ModuleComponent extends Component {

	public final FalcunModule mod;
	public final Paragraph paragraph;

	public ModuleComponent(GuiRegion region, FalcunModule module) {
		super(region);
		this.mod = module;
		paragraph = new Paragraph(region, () -> 0xffffffff, module.getName().toUpperCase(), Fonts.Roboto);
		this.subComponents.add(paragraph);
	}

	public void onClicked(int mX, int mY, int mouseButton) {
		super.onClicked(mX, mY, mouseButton);
		if (!isOver(mX, mY))return;
		if (mouseButton == 0) {
			mod.toggle();
			return;
		} else if (mouseButton != 1) return;
		FalcunModPage.selectedModule = this.mod;
		if (FalcunModPage.settingsPage != null){
			FalcunModPage.settingsPage.update();
		}
	}

	@Override
	public void draw(int mX, int mY) {
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		if (isOver(mX, mY)) {
			Falcun.minecraft.getTextureManager().bindTexture(mod.isEnabled() ? FalcunTexture.moduleBoxToggle[3] : FalcunTexture.moduleBoxToggle[1]);
		} else {
			Falcun.minecraft.getTextureManager().bindTexture(mod.isEnabled() ? FalcunTexture.moduleBoxToggle[2] : FalcunTexture.moduleBoxToggle[0]);
		}
		Gui.drawModalRectWithCustomSizedTexture(region.x, region.y, 0, 0, region.width, region.height, region.width, region.height);
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
}

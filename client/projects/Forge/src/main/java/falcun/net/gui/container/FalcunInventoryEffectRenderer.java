package falcun.net.gui.container;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;

public abstract class FalcunInventoryEffectRenderer extends FalcunContainerGui {
	public FalcunInventoryEffectRenderer(Container inventorySlots) {
		super(inventorySlots);
	}

	protected void init() {
		this.updateActivePotionEffects();
	}

	private boolean hasActivePotionEffects;

	protected void updateActivePotionEffects() {
		boolean hasVisibleEffect = false;
		for (PotionEffect potioneffect : this.mc.thePlayer.getActivePotionEffects()) {
			Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
			if (potion.shouldRender(potioneffect)) {
				hasVisibleEffect = true;
				break;
			}
		}
		if (!this.mc.thePlayer.getActivePotionEffects().isEmpty() && hasVisibleEffect) {
			this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
			this.hasActivePotionEffects = true;
		} else {
			this.guiLeft = (this.width - this.xSize) / 2;
			this.hasActivePotionEffects = false;
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		if (this.hasActivePotionEffects) {
			this.drawActivePotionEffects();
		}
	}


	private void drawActivePotionEffects() {
		int i = this.guiLeft - 124;
		int j = this.guiTop;
		int k = 166;
		Collection<PotionEffect> collection = this.mc.thePlayer.getActivePotionEffects();

		if (!collection.isEmpty()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			int l = 33;

			if (collection.size() > 5) {
				l = 132 / (collection.size() - 1);
			}

			for (PotionEffect potioneffect : this.mc.thePlayer.getActivePotionEffects()) {
				Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
				if (!potion.shouldRender(potioneffect)) continue;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.mc.getTextureManager().bindTexture(inventoryBackground);
				this.drawTexturedModalRect(i, j, 0, 166, 140, 32);

				if (potion.hasStatusIcon()) {
					int i1 = potion.getStatusIconIndex();
					this.drawTexturedModalRect(i + 6, j + 7, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
				}

				potion.renderInventoryEffect(i, j, potioneffect, mc);
				if (!potion.shouldRenderInvText(potioneffect)) {
					j += l;
					continue;
				}
				String s1 = I18n.format(potion.getName(), new Object[0]);

				if (potioneffect.getAmplifier() == 1) {
					s1 = s1 + " " + I18n.format("enchantment.level.2");
				} else if (potioneffect.getAmplifier() == 2) {
					s1 = s1 + " " + I18n.format("enchantment.level.3");
				} else if (potioneffect.getAmplifier() == 3) {
					s1 = s1 + " " + I18n.format("enchantment.level.4");
				}

				this.fontRendererObj.drawStringWithShadow(s1, (float) (i + 10 + 18), (float) (j + 6), 16777215);
				String s = Potion.getDurationString(potioneffect);
				this.fontRendererObj.drawStringWithShadow(s, (float) (i + 10 + 18), (float) (j + 6 + 10), 8355711);
				j += l;
			}
		}
	}
}

package net.mattbenson.modules.types.mods;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;

public class OldAnimations extends Module {
	//Code at GuiIngame.java
	//Code at LayerHeldItem.java
	//Code at ItemRenderer.java
	//Code at Minecraft.java
	//Code at EntityRenderer.java
	
	@ConfigValue.Boolean(name = "Blocking")
	public boolean OLD_BLOCKING = true;
	
	@ConfigValue.Boolean(name = "Blocking Hitting")
	public boolean OLD_BLOCKING_HITTING= true;
	
	@ConfigValue.Boolean(name = "Item Held")
	public boolean OLD_ITEM_HELD = true;	
	
	@ConfigValue.Boolean(name = "Bow")
	public boolean OLD_BOW = true;	
	
	@ConfigValue.Boolean(name = "Eat/Break Animation")
	public boolean OLD_EAT_USE_ANIMATION = true;	
	
	@ConfigValue.Boolean(name = "Armor Damage Flash")
	public boolean ARMOR = true;	
	
	@ConfigValue.Boolean(name = "Disable Health Flash")
	public boolean DISABLE_HEALTH_FLASH = true;	
	
	public OldAnimations() {
		super("Old Animations", ModuleCategory.MODS);
	}
	
	public void attemptSwing() {
		if (this.mc.thePlayer.getHeldItem() != null && this.isEnabled() && OLD_EAT_USE_ANIMATION) {
			boolean mouseDown = this.mc.gameSettings.keyBindAttack.isKeyDown()
					&& this.mc.gameSettings.keyBindUseItem.isKeyDown();

			if (mouseDown && this.mc.objectMouseOver != null
					&& this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				this.swingItem(this.mc.thePlayer);
			}
		}

	}

	private void swingItem(EntityPlayerSP entityplayersp) {
		final int swingAnimationEnd = entityplayersp.isPotionActive(Potion.digSpeed)
				? (6 - (1 + entityplayersp.getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1)
				: (entityplayersp.isPotionActive(Potion.digSlowdown)
						? (6 + (1 + entityplayersp.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2)
						: 6);
		if (!entityplayersp.isSwingInProgress || entityplayersp.swingProgressInt >= swingAnimationEnd / 2
				|| entityplayersp.swingProgressInt < 0) {
			entityplayersp.swingProgressInt = -1;
			entityplayersp.isSwingInProgress = true;
		}
	}
}

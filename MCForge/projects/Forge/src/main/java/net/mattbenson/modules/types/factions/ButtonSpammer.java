package net.mattbenson.modules.types.factions;

import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class ButtonSpammer extends Module {
	public ButtonSpammer() {
		super("Button Spammer", ModuleCategory.FACTIONS);
	}
	
	@SubscribeEvent
	public void onTick(OnTickEvent event) {
		MovingObjectPosition mop = mc.objectMouseOver;

		if (mop == null)
			return;

		if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
			Block block = mc.theWorld.getBlockState(mop.getBlockPos()).getBlock();
			BlockPos blockPos = mop.getBlockPos();

			if(block.getLocalizedName().equalsIgnoreCase("button")) {
				mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockPos, mop.sideHit, mop.hitVec);
				mc.thePlayer.swingItem();
			}
		}
	}
}

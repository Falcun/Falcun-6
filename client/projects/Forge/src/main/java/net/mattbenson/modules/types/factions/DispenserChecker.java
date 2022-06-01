package net.mattbenson.modules.types.factions;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.mattbenson.config.ConfigValue;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.entity.EntityCreateEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.events.types.world.OnTickEvent;
import net.mattbenson.modules.Module;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;

public class DispenserChecker extends Module {
	@ConfigValue.Color(name = "Empty Highlight Color")
	private Color hColor = Color.GREEN;
	
	@ConfigValue.Color(name = "Tnt Highlight Color")
	private Color tColor = Color.RED;
	
	@ConfigValue.Color(name = "Not Empty Color")
	private Color notEmptyColor = Color.YELLOW;
	
	@ConfigValue.List(name = "Mode", values = {"Manual", "Dispense"})
	private String mode = "Manual";
	
	private List<BlockPos> empty = new ArrayList();
	private List<BlockPos> tnt = new ArrayList();
	private List<BlockPos> found = new ArrayList();
	private float lineWidth = 1;
	private Block block;
	private BlockPos blockPos;

	private String old = "BlockPos{x=377, y=5, z=-1193}";
	private boolean isUsingChroma = false;
	
	public DispenserChecker() {
		super("Dispenser Checker", ModuleCategory.FACTIONS);
	}
	
	@Override
	public void onDisable() {
		found.clear();
		empty.clear();
		tnt.clear();
	}
	
	@SubscribeEvent
	public void onSpawnEntity(EntityCreateEvent event) {
		if(event.isCancelled())return;
		if (!event.getWorld().isRemote) {
			return;
		}
		if(mode.equalsIgnoreCase("dispense")) {
		if(event.getEntity() instanceof EntityItem) {
			for(BlockPos pos : getNearbyDispensers(event.getEntity().getPosition())) {
				if(!found.contains(pos)) {
					found.add(pos);
				}
			}
		}
		if(event.getEntity() instanceof EntityTNTPrimed) {
			for(BlockPos pos : getNearbyDispensers(event.getEntity().getPosition())) {
				if(!tnt.contains(pos)) {
					tnt.add(pos);
				}
			}
		}
		}
	}

	@SubscribeEvent
	public void onWorldRenderLast(RenderEvent event )
	{
		if(event.getRenderType() != RenderType.WORLD) {
			return;
		}
		
		if (mc.theWorld != null)
		{
			float f = event.getPartialTicks();
			float px = (float)mc.thePlayer.posX;
			float py = (float)mc.thePlayer.posY;
			float pz = (float)mc.thePlayer.posZ;
			float mx = (float)mc.thePlayer.prevPosX;
			float my = (float)mc.thePlayer.prevPosY;
			float mz = (float)mc.thePlayer.prevPosZ;
			float dx = mx + ( px - mx ) * f;
			float dy = my + ( py - my ) * f;
			float dz = mz + ( pz - mz ) * f;

			highlight(dx,dy,dz); 

		}
	}

	private List<BlockPos> getNearbyDispensers(BlockPos pos) {
		List<BlockPos> toReturn = new ArrayList();
		WorldClient world = mc.theWorld;
		if(Block.getIdFromBlock(world.getBlockState(pos.add(1, 0, 0)).getBlock()) == 23) {
			toReturn.add(pos.add(1,0,0));
		}
		if(Block.getIdFromBlock(world.getBlockState(pos.add(-1, 0, 0)).getBlock()) == 23) {
			toReturn.add(pos.add(-1,0,0));
		}
		if(Block.getIdFromBlock(world.getBlockState(pos.add(0, 0, 1)).getBlock()) == 23) {
			toReturn.add(pos.add(0,0,1));
		}
		if(Block.getIdFromBlock(world.getBlockState(pos.add(0, 0, -1)).getBlock()) == 23) {
			toReturn.add(pos.add(0,0,-1));
		}
		if(Block.getIdFromBlock(world.getBlockState(pos.add(0, 1, 0)).getBlock()) == 23) {
			toReturn.add(pos.add(0,1,0));
		}
		if(Block.getIdFromBlock(world.getBlockState(pos.add(0, -1, 0)).getBlock()) == 23) {
			toReturn.add(pos.add(0,-1,0));
		}
		return toReturn;

	}
	
	@SubscribeEvent
	public void onTic(OnTickEvent event) {
		if (mc.theWorld != null && mode.equalsIgnoreCase("manual")) {

			if(mc.thePlayer == null) return;
			if(mc.theWorld == null) return;

			MovingObjectPosition mop = mc.objectMouseOver;

			if (mop == null)
				return;

			if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {

				block = mc.theWorld.getBlockState(mop.getBlockPos()).getBlock();
				blockPos = mop.getBlockPos();
				if(block.getLocalizedName().equalsIgnoreCase("dispenser")) {

					if (!(old.equals(mop.getBlockPos().toString()))) {
						if(!this.found.contains(blockPos) &&!this.tnt.contains(blockPos) && !this.empty.contains(blockPos)) {
							if(!mc.thePlayer.isSneaking()) {
								rightClick(this.mc.theWorld, this.mc.thePlayer, this.mc.thePlayer.getHeldItem(), mop.getBlockPos(), mop.sideHit, mop.hitVec);
								old = mop.getBlockPos().toString();
							}
						}
					}
				}
			}
			if (mc.currentScreen instanceof GuiDispenser) {

				if(mc.thePlayer.openContainer instanceof ContainerDispenser) {
					Container container = mc.thePlayer.openContainer;
					Block block = mc.theWorld.getBlockState(blockPos).getBlock();
					if(block.getIdFromBlock(block) != 23) return;
					boolean foundTNT = false;
					boolean found = false;
					for(int i = 0; i < 9; i ++) {

						Slot slot = container.inventorySlots.get(i);
						if(slot.getStack() == null) {

						} else if(Item.getIdFromItem(slot.getStack().getItem()) == 46) { 
							foundTNT = true;
						} else {
							found = true;
						}
					}

					if(!this.found.contains(blockPos) &&!this.tnt.contains(blockPos) && !this.empty.contains(blockPos)) {
						if(found) {
							this.found.add(blockPos);

						} else if(foundTNT){
							this.tnt.add(blockPos);

						} else if((!foundTNT && !found)){
							this.empty.add(blockPos);

						}
					}
					mc.displayGuiScreen(null);
					return;

				}
			}
		}
	}
	

	private boolean rightClick(final WorldClient world, final EntityPlayerSP player, final ItemStack itemStack, final BlockPos pos, final EnumFacing side, final Vec3 hitVec) {
		this.mc.playerController.onPlayerRightClick(player, world, itemStack, pos, side, hitVec);
		player.swingItem();
		return true;
	}


	private void highlight( float px, float py, float pz )
	{
		float red = hColor.getRed()/ 255.0f;
		float green = hColor.getGreen()/ 255.0f;
		float blue = hColor.getBlue()/ 255.0f;
		float alpha = hColor.getAlpha()/ 255.0f;

		float bx, by, bz;

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer vertexBuffer = tessellator.getWorldRenderer();

		for(BlockPos pos : tnt) {
			Block block = mc.theWorld.getBlockState(pos).getBlock();
			highlight(block,pos,tColor);
		}

		for(BlockPos pos : empty) {
			Block block = mc.theWorld.getBlockState(pos).getBlock();
			highlight(block,pos,hColor);
		}

		for(BlockPos pos : found) {
			Block block = mc.theWorld.getBlockState(pos).getBlock();
			highlight(block,pos,notEmptyColor);
		}
	}


	private void highlight(Block block, BlockPos pos, Color hColor)
	{

		float red = hColor.getRed()/ 255.0f;
		float green = hColor.getGreen()/ 255.0f;
		float blue = hColor.getBlue()/ 255.0f;
		float alpha = hColor.getAlpha()/ 255.0f;

		float bx, by, bz;

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer vertexBuffer = tessellator.getWorldRenderer();



		double renderPosX = mc.getRenderManager().viewerPosX;
		double renderPosY = mc.getRenderManager().viewerPosY;
		double renderPosZ = mc.getRenderManager().viewerPosZ;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GL11.glColor4f(red, green, blue, 0.5F);

		AxisAlignedBB bb = block.getSelectedBoundingBox(mc.theWorld, pos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-renderPosX, -renderPosY, -renderPosZ);
		drawFilledBoundingBox(bb);
		GL11.glLineWidth(1F);
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();

	}
	private void drawFilledBoundingBox(AxisAlignedBB box) {

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		tessellator.draw();

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
		worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
		worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
		worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
		tessellator.draw();
	}
}

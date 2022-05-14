package com.github.lunatrius.schematica.client.gui.control;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import com.github.lunatrius.core.client.gui.GuiHelper;
import com.github.lunatrius.schematica.client.util.BlockList;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import net.mattbenson.Wrapper;
import net.mattbenson.modules.types.mods.Schematica;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

class GuiMaterialLocationSlot extends GuiSlot {

	private final Minecraft minecraft = Minecraft.getMinecraft();

	private final GuiMaterialLocation guiMaterialLocation;
	private boolean selected = false;
	
	protected int selectedIndex = -1;

	public GuiMaterialLocationSlot(GuiMaterialLocation par1, int index) {
		super(Minecraft.getMinecraft(), par1.width, par1.height, 16, par1.height - 34, 24);
		this.guiMaterialLocation = par1;
		this.selectedIndex = index;
	}

	@Override
	protected int getSize() {
		return this.guiMaterialLocation.blockList.get(selectedIndex).positions.size();
	}

	@Override
	protected void elementClicked(final int index, final boolean par2, final int par3, final int par4) {
		
	}

	@Override
	protected boolean isSelected(final int index) {
		return index == this.selectedIndex;
	}

	@Override
	protected void drawBackground() {
	}

	@Override
	protected void drawContainerBackground(final Tessellator tessellator) {
	}

	@Override
	protected int getScrollBarX() {
		return this.width / 2 + getListWidth() / 2 + 2;
	}

	@Override
	protected void drawSlot(final int index, final int x, final int y, final int par4, final int mouseX, final int mouseY) {
		
		final BlockList.WrappedItemStack wrappedItemStack = this.guiMaterialLocation.blockList.get(selectedIndex);
		final ItemStack itemStack = wrappedItemStack.itemStack;

		final String itemName = wrappedItemStack.getItemStackDisplayName();
		final String amount = wrappedItemStack.getFormattedAmount();
		
		boolean isMouseOver = mouseX >= x+200 && mouseX <= x+214 && mouseY >= y+3 && mouseY <= y+17;
		
		
		GuiHelper.drawItemStackWithSlot(this.minecraft.renderEngine, itemStack, x, y);
		
		BlockPos blockPos = new BlockPos(wrappedItemStack.positions.get(index).getX(),  wrappedItemStack.positions.get(index).getY(), wrappedItemStack.positions.get(index).getZ());
		if(isMouseOver && Mouse.isButtonDown(0) && !selected) {
			
			if(!Schematica.missinglocation.contains(blockPos)) {
				Schematica.missinglocation.add(blockPos);
			} else {
				Schematica.missinglocation.remove(blockPos);
			}
			selected = true;
		}
		if(selected && !Mouse.isButtonDown(0)) {
			selected = false;
		}
		

		boolean hovered = mouseX >= x + 200 && mouseY <= x + 215 && mouseY >= y + 3 && mouseY <= y+17;
		Wrapper.getInstance().drawRoundedRect(x + 200, y + 2, x + 215, y + 17, 4, hovered ? new Color(200,200,200,100).getRGB() : new Color(100,100,100,100).getRGB());
 		Wrapper.getInstance().drawRoundedRect(x + 200 + 1, y + 2 + 1, x + 215 - 1, y + 17 - 1,  3, new Color(22, 24, 27,100).getRGB());
 		
		
		if(Schematica.missinglocation.contains(blockPos)) {
			Wrapper.getInstance().drawRoundedRect(x + 200 + 2, y + 2 + 2, x + 215 - 2, y + 17 - 2,  2, new Color(32, 117, 56,216).getRGB());
		} 
		
		this.guiMaterialLocation.drawString(this.minecraft.fontRendererObj, "X: " + wrappedItemStack.positions.get(index).getX() +", Y: " + wrappedItemStack.positions.get(index).getY()+", Z: " + wrappedItemStack.positions.get(index).getZ()  , x + 24, y + 6, 0xFFFFFF);
		
		if (mouseX > x && mouseY > y && mouseX <= x + 18 && mouseY <= y + 18) {
			this.guiMaterialLocation.renderToolTip(itemStack, mouseX, mouseY);
			GlStateManager.disableLighting();
		}
	}
}

package com.github.lunatrius.schematica.client.gui.control;

import java.io.IOException;
import java.util.List;

import com.github.lunatrius.core.client.gui.GuiScreenBase;
import com.github.lunatrius.schematica.client.util.BlockList;
import com.github.lunatrius.schematica.reference.Names;

import net.mattbenson.modules.types.mods.Schematica;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class GuiMaterialLocation extends GuiScreenBase {

	private GuiButton btnDone = null;
	private GuiButton btnBack = null;
	private GuiButton btnSelectAll = null;
	protected final List<BlockList.WrappedItemStack> blockList;
	private final String strMaterialName = I18n.format(Names.Gui.Control.MATERIAL_NAME);
	private final String strMaterialAmount = I18n.format(Names.Gui.Control.MATERIAL_AMOUNT);

	private GuiMaterialLocationSlot guiSchematicMaterialsSlot;
	private final GuiSchematicMaterials guiSchematicMaterials;

	String selectAll = "Select All";
	boolean isSelectedAll = false;

	public int selectedIndex = -1;

	public GuiMaterialLocation(GuiSchematicMaterials guiSchematicMaterials, List<BlockList.WrappedItemStack>  blocks, int index) {
		this.guiSchematicMaterials = guiSchematicMaterials;
		this.blockList = blocks;
		this.selectedIndex = index;
	}

	@Override
	public void initGui() {
		int id = 0;

		this.btnBack = new GuiButton(++id, this.width / 2 - 50, this.height - 30, 100, 20, "Back");
		this.buttonList.add(this.btnBack);



		this.btnDone = new GuiButton(++id, this.width / 2 + 54, this.height - 30, 100, 20, I18n.format(Names.Gui.DONE));
		this.buttonList.add(this.btnDone);


		if(this.isSelectedAll) {
			selectAll = "Unselect All";
		} else {
			selectAll = "Select All";
		}
		
		this.btnSelectAll = new GuiButton(++id, this.width / 2 - 150, this.height - 30, 100, 20, selectAll);
		this.buttonList.add(this.btnSelectAll);
		
		guiSchematicMaterialsSlot = new GuiMaterialLocationSlot(this,selectedIndex);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.guiSchematicMaterialsSlot.handleMouseInput();
	}

	@Override
	protected void actionPerformed(final GuiButton guiButton) {
		if (guiButton.enabled) {
			if (guiButton.id == this.btnDone.id) {
				this.mc.displayGuiScreen(this.parentScreen);
			} 
			if (guiButton.id == this.btnBack.id) {
				this.mc.displayGuiScreen(this.guiSchematicMaterials);
			} 
			if(guiButton.id == this.btnSelectAll.id) {
				if(this.isSelectedAll) {
					Schematica.missinglocation.clear();
					this.isSelectedAll = false;
					selectAll = "Select All";
				
				} else {
					if(blockList.size() > 0 && selectedIndex != -1) {
						Schematica.missinglocation.clear();
						BlockList.WrappedItemStack wrappedItemStack = blockList.get(selectedIndex);
						for(BlockPos pos : wrappedItemStack.positions) {
							Schematica.missinglocation.add(pos);
						}
						this.isSelectedAll = true;
						selectAll = "Unselect All";
					}
				}
				btnSelectAll.displayString = selectAll;
			}
		}
	}


	@Override
	public void renderToolTip(final ItemStack stack, final int x, final int y) {
		super.renderToolTip(stack, x, y);
	}

	@Override
	public void drawScreen(final int x, final int y, final float partialTicks) {
		this.guiSchematicMaterialsSlot.drawScreen(x, y, partialTicks);

		this.drawString(this.fontRendererObj, "Remaining Materials", this.width / 2 - 50, 4, 0x00FFFFFF);

		super.drawScreen(x, y, partialTicks);
	}

}

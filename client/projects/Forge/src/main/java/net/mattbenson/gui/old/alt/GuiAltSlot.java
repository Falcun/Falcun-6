package net.mattbenson.gui.old.alt;

import java.awt.Color;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.mattbenson.accountmanager.Account;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.old.GuiScrollingList;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiAltSlot extends GuiScrollingList {

	private GuiAccountManager parent;
	private List<Account> altList;

	int uploadSuccess = -1;

	public GuiAltSlot(GuiAccountManager parent, List<Account> altList, int listWidth)
	{
		super(parent.mc, listWidth, parent.height, 32, parent.height - 88 + 4, 10, 21, parent.width, parent.height);

		this.parent = parent;
		this.altList = altList;
	}

	@Override
	public int getContentHeight()
	{
		return (this.getSize()) * this.slotHeight + 1;
	}

	public List<Account> getAlts()
	{
		return altList;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return altList.size();
	}

	@Override
	public void elementClicked(int index, int left, int right, boolean doubleClick) {
		try {
			boolean isOverDelete = mouseX >= right - 15 && mouseX <= right - 5;
			Account element = altList.get(index);
			if(element != null) {
				if(isOverDelete) {
					element.removeAccount();
					parent.altList.remove(element);
				} else {
					this.parent.displayErrorMessage(element.loadUser());
				}
			}
		}
		catch(Exception ex) {

		}

	}

	@Override
	public boolean isSelected(int index) {
		return true;
	}

	@Override
	public void drawBackground() {
		this.parent.drawDefaultBackground();
	}

	@Override
	public void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		// TODO Auto-generated method stub
		try {
			Account alt = altList.get(slotIdx);
			String name = alt.getUsername();
			if(name.equals("N/A")) {
				name = alt.getUsername();
			}
			boolean isBeingUsed = false;
			boolean isOverDelete = mouseX >= entryRight - 15 && mouseX <= entryRight - 5 && mouseY >= slotTop + 3 && mouseY <= slotTop+slotHeight - 5;
			
			if(!name.equalsIgnoreCase("none")) {

				boolean isOver = mouseX >= this.left && mouseX <= entryRight && mouseY >= slotTop && mouseY <= slotTop+slotHeight - 2;
				DrawUtils.drawRoundedRect(this.left, slotTop, entryRight, slotTop+slotHeight - 2, 4, isOver ? new Color(200,200,200,100).getRGB() :new Color(100,100,100,100).getRGB());
				DrawUtils.drawRoundedRect(this.left+1, slotTop + 1, entryRight-1, slotTop+slotHeight-3, 3, new Color(22, 24, 27,100).getRGB());

				if(Minecraft.getMinecraft().getSession().getUsername().equalsIgnoreCase(alt.getUsername())) {
					isBeingUsed = true;
				}
				
				Fonts.RalewayExtraBoldExtraSmall.drawString("USERNAME:", this.left + 5  , slotTop + 5, Color.WHITE.getRGB());
				Fonts.RalewayExtraBoldExtraSmall.drawString((isBeingUsed ? EnumChatFormatting.GREEN  : "") + name.toUpperCase(), this.left + Fonts.RalewayExtraBold.getStringWidth("USERNAME:"), slotTop + 5, Color.WHITE.getRGB());
				
				GL11.glColor4f(1.0F, 1.0F, 1.0F, isOverDelete ? 0.5F : 1.0F);
				DrawUtils.drawImage(new ResourceLocation("textures/falcun/gui/mainmenu/exit.png"), entryRight - 13, slotTop + 6, 6, 6);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
		catch(Exception ex) {
		
		}

	}

}

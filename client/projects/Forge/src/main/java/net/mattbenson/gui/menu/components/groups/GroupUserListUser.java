package net.mattbenson.gui.menu.components.groups;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MenuPriority;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.utils.DrawUtils;
import net.minecraft.util.ResourceLocation;

public class GroupUserListUser extends MenuComponent {
	protected ResourceLocation location;
	protected String name;
	protected String rank;
	protected boolean online;
	
	public GroupUserListUser(ResourceLocation location, String name, String rank, boolean online, int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.location = location;
		this.name = name;
		this.rank = rank + " | ";
		this.online = online;
	}

	@Override
	public void onInitColors() {
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(255, 255, 255, IngameMenu.MENU_ALPHA));
		setColor(DrawType.TEXT, ButtonState.POPUP, new Color(98, 98, 99, IngameMenu.MENU_ALPHA));
		
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(164, 164, 164, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.POPUP, new Color(0, 250, 6, IngameMenu.MENU_ALPHA));
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int width = this.width;
		
		int rankColor = getColor(DrawType.TEXT, ButtonState.NORMAL);
		int textColor = getColor(DrawType.TEXT, ButtonState.POPUP);
		int color = online ? getColor(DrawType.BACKGROUND, ButtonState.POPUP) : getColor(DrawType.BACKGROUND, ButtonState.NORMAL);
		
		drawImage(location, x + 10, y + 1, 20, 20);
		
		drawText(rank, x + 40, y + height / 2 - getStringHeight(rank) / 2, rankColor);
		drawText(name, x + 40 + getStringWidth(rank), y + height / 2 - getStringHeight(name) / 2, textColor);
		
		int roundX = x + width - 25;
		int roundY = y + height / 2 - 2;
		
		DrawUtils.drawRoundedRect(roundX, roundY, roundX + 4, roundY + 4, 3, color);
	}
	
	@Override
	public void drawText(String string, int x, int y, int color) {
		Fonts.Roboto.drawString(string, x, y, color);
	}
	
	@Override
	public int getStringWidth(String string) {
		return Fonts.Roboto.getStringWidth(string);
	}
	
	@Override
	public int getStringHeight(String string) {
		return Fonts.Roboto.getStringHeight(string);
	}

}

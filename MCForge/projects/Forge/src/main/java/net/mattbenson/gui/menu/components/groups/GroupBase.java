package net.mattbenson.gui.menu.components.groups;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.components.profiles.ProfilesBase;

public class GroupBase extends ProfilesBase {
	protected static int GREEN_COLOR = new Color(2, 222, 9, 255).getRGB();
	protected static int GRAY_COLOR = new Color(164, 164, 164, 255).getRGB();
	
	protected String leader;
	protected int membersOnline;
	protected int totalMembers;
		
	public GroupBase(String text, String leader, int membersOnline, int totalMembers, int x, int y, int width, int height) {
		super(text, x, y, width, height);
		
		this.leader = leader;
		this.membersOnline = membersOnline;
		this.totalMembers = totalMembers;
	}

	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int width = this.width;
		
		int lineColor = getColor(DrawType.LINE, ButtonState.NORMAL);
		int textColor = getColor(DrawType.TEXT, ButtonState.NORMAL);
		int backgroundColor = getColor(DrawType.BACKGROUND, ButtonState.NORMAL);

		drawRectFalcun(x, y, width, height, backgroundColor);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);

		drawShadowUp(x, y, width + 1);
		drawShadowLeft(x, y, height + 1);
		drawShadowDown(x, y + height + 1, width + 1);
		drawShadowRight(x + width + 1, y, height + 1);
		
		Fonts.Roboto.drawString(text, x + width / 2 - Fonts.Roboto.getStringWidth(text) / 2, y + 30, textColor);
		
		String text = "LEADER: ";
		int tWidth = getStringWidth(text);
		int yAdd = 50;
		int xOffset = 5;
		
		drawText(text, x + xOffset, y + yAdd, textColor);
		drawText(leader, x + xOffset + tWidth, y + yAdd, GRAY_COLOR);
		
		yAdd += 10;
		text = "MEMBERS ONLINE: ";
		tWidth = getStringWidth(text);
		drawText(text, x + xOffset, y + yAdd, textColor);
		drawText(membersOnline + "", x + xOffset + tWidth, y + yAdd, GREEN_COLOR);
		
		yAdd += 10;
		text = "CURRENT MEMBERS: ";
		tWidth = getStringWidth(text);
		drawText(text, x + xOffset, y + yAdd, textColor);
		drawText(totalMembers + "", x + xOffset + tWidth, y + yAdd, GRAY_COLOR);
	}
	
	@Override
	public void drawText(String string, int x, int y, int color) {
		Fonts.RobotoSmall.drawString(string, x, y, color);
	}
	
	@Override
	public int getStringWidth(String string) {
		return Fonts.RobotoSmall.getStringWidth(string);
	}
	
	@Override
	public int getStringHeight(String string) {
		return Fonts.RobotoSmall.getStringHeight(string);
	}

}

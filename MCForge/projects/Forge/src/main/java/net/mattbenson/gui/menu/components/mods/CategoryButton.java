package net.mattbenson.gui.menu.components.mods;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.components.MenuButton;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.Category;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CategoryButton extends MenuButton {
	public CategoryButton(Category category, int x, int y) {
		super(category.getName(), x, y);
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(126, 126, 126, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(160, 160, 160, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(180, 180, 180, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
		
		super.onInitColors();
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = (this.width == -1 && this.height == -1) ? (getStringWidth(text) + minOffset * 2) : this.width;
		int height = (this.width == -1 && this.height == -1) ? (getStringHeight(text) + minOffset * 2) : this.height;
		
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int textColor = getColor(DrawType.TEXT, lastState);
		
		Fonts.Roboto.drawString(text, x + (width / 2 - getStringWidth(text) / 2), y + height / 2 - (getStringHeight(text) / 2) - 3, textColor);
		
		if(isActive()) {
			drawHorizontalLine(x + (width / 2 - getStringWidth(text) / 2), y + 29, (int)Fonts.Roboto.getStringWidth(text), 2, textColor);
		}
		
		mouseDown = false;
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

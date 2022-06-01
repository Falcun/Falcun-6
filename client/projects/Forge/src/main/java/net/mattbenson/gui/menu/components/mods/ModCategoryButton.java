package net.mattbenson.gui.menu.components.mods;

import java.awt.Color;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.components.MenuButton;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.modules.ModuleCategory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ModCategoryButton extends MenuButton {
	public static final int MAIN_COLOR = new Color(18, 17, 22, IngameMenu.MENU_ALPHA).getRGB();
	
	private final int IMAGE_SIZE = 22;
	
	private ResourceLocation image;
	
	public ModCategoryButton(ModuleCategory category, ResourceLocation image, int x, int y, int width, int height) {
		super(category.getText(), x, y, width, height);
		this.image = image;
	}

	public ModCategoryButton(String text, int x, int y, int width, int height) {
		super(text, x, y, width, height);
	}

	@Override
	public void onInitColors() {
		super.onInitColors();
		
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(MAIN_COLOR, true));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(25, 25, 29, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(20, 20, 24, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(35, 35, 39, IngameMenu.MENU_ALPHA));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, IngameMenu.MENU_ALPHA));	
	}
	
	@Override
	public void onRender() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int width = (this.width == -1 && this.height == -1) ? (getStringWidth(text) + minOffset * 2) : this.width;
		int height = (this.width == -1 && this.height == -1) ? (getStringHeight(text) + minOffset * 2) : this.height;
		
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int textColor = getColor(DrawType.TEXT, lastState);
		
		GlStateManager.color(1, 1, 1);
		drawRectFalcun(x, y, width, height, backgroundColor);
		
		if(image != null) {
			drawImage(image, x + 5, y + (height / 2 - IMAGE_SIZE / 2), IMAGE_SIZE, IMAGE_SIZE);
			Fonts.Roboto.drawString(text, x + IMAGE_SIZE + 5 + 5, y + height / 2 - (getStringHeight(text) / 2) - 1, textColor);
		} else {
			Fonts.Roboto.drawString(text, x + (width / 2 - getStringWidth(text) / 2), y + height / 2 - (getStringHeight(text) / 2), textColor);
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

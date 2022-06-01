package net.mattbenson.gui.menu.components.mods;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.draw.ButtonState;
import net.mattbenson.gui.framework.draw.DrawType;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.modules.Module;
import net.mattbenson.utils.AssetUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ModuleBox extends MenuComponent {
	protected static final int INACTIVE = new Color(10, 90, 32, IngameMenu.MENU_ALPHA).getRGB();
	protected static final int ACTIVE = new Color(90, 10, 12, IngameMenu.MENU_ALPHA).getRGB();
	
	protected static final int COG_BORDER = new Color(57, 57, 59, IngameMenu.MENU_ALPHA).getRGB();
	
	protected static final ResourceLocation COG = AssetUtils.getResource("/gui/cog.png");
	
	private static final int MIN_SPACING = 8;
	
	protected Module module;
	protected ButtonState lastState = ButtonState.NORMAL;
	protected boolean mouseDown;
	
	private List<String> lines = new ArrayList<>();
	private int tHeight;
	
	public ModuleBox(Module module, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.module = module;
		
		String text = module.getName().toUpperCase();
		
		String[] words = text.split(" ");
		StringBuilder curWord = new StringBuilder();
		
		for(String word : words) {
			String toAdd = word;
			
			if(!curWord.toString().isEmpty()) {
				toAdd = " " + toAdd;
			}
			
			if(Fonts.MC.getStringWidth(curWord.toString() + toAdd) + MIN_SPACING > width) {
				lines.add(curWord.toString());
				curWord.setLength(0);
				toAdd = word;
			}
			
			curWord.append(toAdd);
		}
		
		lines.add(curWord.toString());
		
		tHeight = 0;
		
		for(String line : lines) {
			tHeight += Fonts.MC.getStringHeight(line);
		}
	}
	
	@Override
	public void onInitColors() {
		setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(18, 17, 22, 255));
		setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(18, 17, 22, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(0, 0, 0, 255));
		setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(0, 0, 0, 255));
		setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(18, 17, 22, 255));	
		
		setColor(DrawType.LINE, ButtonState.NORMAL, new Color(36, 36, 38, 255));
		setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(36, 36, 38, 255));
		setColor(DrawType.LINE, ButtonState.HOVER, new Color(36, 36, 38, 255));
		setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(36, 36, 38, 255));
		setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));

		setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(213, 213, 213, 255));
		setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(213, 213, 213, 255));
		setColor(DrawType.TEXT, ButtonState.HOVER, new Color(213, 213, 213, 255));
		setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(213, 213, 213, 255));
		setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
	}
	
	@Override
	public void onMouseClick(int button) {
		if(button == 0) {
			mouseDown = true;
		}
	}
	
	@Override
	public boolean passesThrough() {
		if(disabled || parent == null) {
			return true;
		}

		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		if(mouseDown) {
			mouseDown = false;
			
			if(mouseX >= x && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height + 1) {
					
					if(mouseX >= x + 10 && mouseX <= x + width - 11) {
						if(mouseY >= y + height - 10 - 20 && mouseY <= y + height - 10) {
							module.toggle();
							onToggle();
						}
					}
					
					if(!module.getEntries().isEmpty()) {
						if(mouseX >= x + width - 14 - 17 - 4 && mouseX <= x + width - 14 - 17 - 4 + 24) {
							if(mouseY >= y + 14 - 4 && mouseY <= y + 14 - 3 + 23) {
								onOpenSettings();
							}
						}
					}
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public void onPreSort() {
		int x = this.getRenderX();
		int y = this.getRenderY();
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		ButtonState state = ButtonState.NORMAL;
		
		if(!disabled) {
			if(mouseX >= x && mouseX <= x + width) {
				if(mouseY >= y && mouseY <= y + height) {
					state = ButtonState.HOVER;
				}
			}
		} else {
			state = ButtonState.DISABLED;
		}
		
		lastState = state;
	}
	
	@Override
	public void onRender() {
		int backgroundColor = getColor(DrawType.BACKGROUND, lastState);
		int lineColor = getColor(DrawType.LINE, lastState);
		int textColor = getColor(DrawType.TEXT, lastState);
		
		int x = this.getRenderX();
		int y = this.getRenderY();

		int defaultColor = getColor(DrawType.BACKGROUND, ButtonState.NORMAL);
		int drawColor = defaultColor;
		
		drawRectFalcun(x + 1, y + 1, width - 1, height - 1, defaultColor);
		
		drawHorizontalLine(x, y, width + 1, 1, lineColor);
		drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
		drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
		drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
		
		drawShadowUp(x, y, width + 1);
		drawShadowLeft(x, y, height + 1);
		drawShadowDown(x, y + height + 1, width + 1);
		drawShadowRight(x + width + 1, y, height + 1);
		
		int yPos = y + (height / 2) - tHeight / 2 - 5;
		
		for(String line : lines) {
			Fonts.MC.drawString(line, x + width / 2 - Fonts.MC.getStringWidth(line) / 2, yPos, textColor);
			yPos += Fonts.MC.getStringHeight(line);		}
		
		if ((module.getName() == "Patchcrumbs 2") || (module.getName() == "Patchcrumbs OLD")) {
			Fonts.RobotoSmall.drawString("DEPRECATED", x + 13 + width / 2 - Fonts.MC.getStringWidth("Deprecated") / 2, yPos, Color.red.getRGB());
		}
		
		int mouseX = parent.getMouseX();
		int mouseY = parent.getMouseY();
		
		if(mouseX >= x + 10 && mouseX <= x + width - 11) {
			if(mouseY >= y + height - 10 - 20 && mouseY <= y + height - 10) {
				drawColor = backgroundColor;
			}
		}
		
		if ((module.getName() == "Group Location") || (module.getName() == "Group Patchcrumbs") || (module.getName() == "Group Ping Location")) {
		} else {
		
		
		drawShadowUp(x + 10, y + height - 30, width - 20);
		drawShadowLeft(x + 10,  y + height - 30, 20);
		drawShadowDown(x + 10, y + height - 10, width - 20);
		drawShadowRight(x + width - 10,  y + height - 30, 20);
		

		
			
		
		
		drawRectFalcun(x + 10, y + height - 10 - 20, width - 20, 20, module.isEnabled() ? INACTIVE : ACTIVE);
		drawRectFalcun(x + 11, y + height - 10 - 19, width - 22, 18, drawColor);
		
		String text = module.isEnabled() ? "ENABLED" : "DISABLED";
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		Fonts.Roboto.drawString(text, x + 13 + width / 2 - Fonts.MC.getStringWidth(text) / 2, y + height - 10 - 17, textColor);
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		if(!module.getEntries().isEmpty()) {
			drawColor = defaultColor;
			
			if(mouseX >= x + width - 14 - 17 - 4 && mouseX <= x + width - 14 - 17 - 4 + 24) {
				if(mouseY >= y + 14 - 4 && mouseY <= y + 14 - 3 + 23) {
					drawColor = Color.black.getRGB();
				}
			}
		}
		}
		
		drawShadowUp(x + width - 14 - 17 - 4,  y + 14 - 4, 25);
		drawShadowLeft(x + width - 14 - 17 - 4,  y + 14 - 4, 25);
		drawShadowDown(x + width - 14 - 17 - 4,  y + 14 - 4 + 25, 25);
		drawShadowRight(x + width - 14 - 17 - 4 + 25,  y + 14 - 4, 25);
		
		drawRectFalcun(x + width - 14 - 17 - 4, y + 14 - 4, 25, 25, COG_BORDER);
		drawRectFalcun(x + width - 14 - 17 - 3, y + 14 - 3, 23, 23, drawColor);
		
		drawImage(COG, x + width - 14 - 17, y + 14, 17, 17);
		
		
	}
	
	public Module getModule() {
		return module;
	}
	
	public void onOpenSettings() {}
	public void onToggle() {}
}

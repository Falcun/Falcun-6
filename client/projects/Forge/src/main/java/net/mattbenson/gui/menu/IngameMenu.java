package net.mattbenson.gui.menu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import java.util.NoSuchElementException;
import java.io.IOException;

import net.mattbenson.Falcun;
import net.mattbenson.config.Config;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.MenuComponent;
import net.mattbenson.gui.framework.MinecraftMenuImpl;
import net.mattbenson.gui.framework.components.MenuButton;
import net.mattbenson.gui.framework.components.MenuDraggable;
import net.mattbenson.gui.framework.components.MenuScrollPane;
import net.mattbenson.gui.framework.draw.DrawImpl;
import net.mattbenson.gui.menu.components.mods.CategoryButton;
import net.mattbenson.gui.menu.pages.ModsPage;
import net.mattbenson.modules.Module;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.mattbenson.utils.AssetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class IngameMenu extends MinecraftMenuImpl implements DrawImpl {
	public static final int MENU_ALPHA = 255;
	public static final int MENU_TOP_BG_COLOR = new Color(18, 17, 22, MENU_ALPHA).getRGB();
	public static final int MENU_PANE_BG_COLOR = new Color(15, 14, 19, MENU_ALPHA).getRGB();
	public static final int MENU_HEADER_TEXT_COLOR = new Color(255, 255, 255, MENU_ALPHA).getRGB();
	public static final int MENU_LINE_COLOR = new Color(25, 25, 28, IngameMenu.MENU_ALPHA).getRGB();
		
	protected static final ResourceLocation LOGO = AssetUtils.getResource("/gui/logo-bg-new.png");
	
	public static PageManager pageManager;
	public static Category category = Category.MODS;
		
	private static boolean initd;
	private static int savedWidth = -1;
	private static int savedHeight = -1;
	
	public IngameMenu(Module feature, Menu menu) {
		super(feature, menu);
		
		pageManager = new PageManager(this, menu);
	}
	
	@Override
	public void initGui() {
		

		if(initd) {
			menu.getComponents().clear();
			initd = false;
		}

		// REMOVE CODE ABOVE ONCE MENU FULLY DONE \\
		
		if(!initd) {
			for(IPage page : pageManager.getPages().values()) {
				page.onInit();
			}
			
			menu.addComponent(new MenuDraggable(0, 0, menu.getWidth(), 58));
			
			int x = 175;
			int y = 58 / 2 + 2;
			
			for(Category category : Category.values()) {
				MenuButton comp = new CategoryButton(category, x, y - (int)Fonts.Roboto.getStringHeight(category.getName()) / 2) {
					@Override
					public void onAction() {
						if(IngameMenu.this.category != null) {
							pageManager.getPage(IngameMenu.this.category).onUnload();
						}
						
						IngameMenu.this.category = category;
						
						for(MenuComponent component : menu.getComponents()) {
							if(component instanceof CategoryButton) {
								CategoryButton button = (CategoryButton) component;
								button.setActive(component == this);
							}
						}
						
						initPage();
					}
				};
				
				if(category == this.category) {
					comp.setActive(true);
				}
				
				menu.addComponent(comp);
				
				x += Fonts.Roboto.getStringWidth(category.getName()) + 20;
			}
			
			initPage();
			initd = true;
		}
		
		if(category != null) {
			pageManager.getPage(category).onOpen();
		}
		
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(savedWidth != mc.displayWidth || savedHeight != mc.displayHeight) {
			savedWidth = mc.displayWidth;
			savedHeight = mc.displayHeight;
			ScaledResolution sr = new ScaledResolution(mc, 1);
			menu.setX(sr.getScaledWidth() / 2 - menu.getWidth() / 2);
			menu.setY(sr.getScaledHeight() / 2 - menu.getHeight() / 2);
		}
		
		GlStateManager.pushMatrix();
		float value = guiScale / new ScaledResolution(mc).getScaleFactor();
		GlStateManager.scale(value, value, value);
		
		drawRectFalcun(menu.getX(), menu.getY(), menu.getWidth(), 58, MENU_TOP_BG_COLOR);
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		//drawTexturedModalRect(MENU_LOGO, menu.getX() / 2, menu.getY() / 2, 0, 100, 249, 338, 249, 338);
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		drawImage(LOGO, menu.getX(), menu.getY(), 175, 63);

		//Fonts.RobotoHeader.drawString("FALCUN", menu.getX() + 30, menu.getY() + 17, MENU_HEADER_TEXT_COLOR);
		drawRectFalcun(menu.getX(), menu.getY() + 58, menu.getWidth(), menu.getHeight() - 58, MENU_PANE_BG_COLOR);
		
		drawShadowDown(menu.getX(), menu.getY() + 58, menu.getWidth());
		
		drawShadowUp(menu.getX(), menu.getY(), menu.getWidth());
		drawShadowDown(menu.getX(), menu.getY() + menu.getHeight(), menu.getWidth());
		drawShadowLeft(menu.getX(), menu.getY(), menu.getHeight());
		drawShadowRight(menu.getX() + menu.getWidth(), menu.getY(), menu.getHeight());
		
		if(category != null) {
			pageManager.getPage(category).onRender();
		}
		
		GlStateManager.popMatrix();
		
		super.drawScreen(mouseX, mouseY, partialTicks);

		GlStateManager.pushMatrix();
		GlStateManager.scale(value, value, value);
		
		for(MenuComponent component : menu.getComponents()) {
			if(component instanceof MenuScrollPane) {
				MenuScrollPane scrollpane = (MenuScrollPane) component;
				
				scrollpane.drawExtras();
			}
		}
		
		GlStateManager.popMatrix();
	}
	
	public void initPage() {
		List<MenuComponent> remove = new ArrayList<>();
		
		for(MenuComponent component : menu.getComponents()) {
			if(component instanceof CategoryButton || component instanceof MenuDraggable) {
				continue;
			}
			
			remove.add(component);
		}
		
		menu.getComponents().removeAll(remove);
		
		pageManager.getPage(category).onLoad();
	}

	public void openSettings(Module parent) {
		if(category != null) {
			pageManager.getPage(category).onUnload();
		}
		
		category = Category.MODS;
		
		pageManager.getPage(ModsPage.class, Category.MODS).activeModule = parent;
		
		initPage();
	}
	
	@Override
	public void onGuiClosed() {
		if(category != null) {
			pageManager.getPage(category).onClose();
		}
		
		super.onGuiClosed();
		
		new Thread(() -> {
			Config config = Falcun.getInstance().configManager.getLoadedConfig();
			
			if(config != null) {
				config.save();
			}
		}).start();
	}
}

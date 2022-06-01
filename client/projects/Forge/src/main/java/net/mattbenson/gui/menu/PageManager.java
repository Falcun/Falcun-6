package net.mattbenson.gui.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.menu.pages.ConsolePage;
import net.mattbenson.gui.menu.pages.CosmeticsPage;
import net.mattbenson.gui.menu.pages.FPSPage;
import net.mattbenson.gui.menu.pages.GroupsPage;
import net.mattbenson.gui.menu.pages.HUDPage;
import net.mattbenson.gui.menu.pages.MacrosPage;
import net.mattbenson.gui.menu.pages.ModsPage;
import net.mattbenson.gui.menu.pages.ProfilesPage;
import net.mattbenson.gui.menu.pages.WaypointsPage;
import net.minecraft.client.Minecraft;

public class PageManager {
	private IngameMenu parent;
	private Menu menu;
	private Map<Category, Page> pages;
	
	public PageManager(IngameMenu parent, Menu menu) {
		this.parent = parent;
		this.menu = menu;
		this.pages = new HashMap<>();
		init();
	}
	
	private void init() {
		Minecraft mc = Minecraft.getMinecraft();
		
		pages.put(Category.MODS, new ModsPage(mc, menu, parent));
		pages.put(Category.MACROS, new MacrosPage(mc, menu, parent));
		pages.put(Category.WAYPOINTS, new WaypointsPage(mc, menu, parent));
		pages.put(Category.PROFILES, new ProfilesPage(mc, menu, parent));
		pages.put(Category.COSMETICS, new CosmeticsPage(mc, menu, parent));
		pages.put(Category.CONSOLE, new ConsolePage(mc, menu, parent));
		pages.put(Category.FPS, new FPSPage(mc, menu, parent));
		pages.put(Category.GROUPS, new GroupsPage(mc, menu, parent));
		pages.put(Category.HUD, new HUDPage(mc, menu, parent));
	}
	
	public Map<Category, Page> getPages() {
		return pages;
	}
	
	public <T extends Page> T getPage(Category category) {
		return (T) pages.get(category);
	}
	
	public <T extends Page> T getPage(Class<T> cast, Category category) {
		return (T) pages.get(category);
	}
}

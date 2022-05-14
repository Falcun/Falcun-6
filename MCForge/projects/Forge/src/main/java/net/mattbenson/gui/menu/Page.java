package net.mattbenson.gui.menu;

import net.mattbenson.gui.framework.Menu;
import net.mattbenson.gui.framework.draw.DrawImpl;
import net.minecraft.client.Minecraft;

public abstract class Page implements IPage, DrawImpl {
	protected static Minecraft mc;
	protected Menu menu;
	protected IngameMenu parent;
	
	public Page(Minecraft mc, Menu menu, IngameMenu parent) {
		this.mc = mc;
		this.menu = menu;
		this.parent = parent;
	}
}

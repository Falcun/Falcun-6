package net.mattbenson.events.types.render.gui;

import net.mattbenson.events.Event;
import net.minecraft.client.gui.GuiScreen;

//Hooked at net.minecraft.client.Minecraft.java
public class GuiInitEvent extends Event {
	public GuiScreen target;
	
	public GuiInitEvent(GuiScreen target) {
		this.target = target;
	}
	
	public GuiScreen getTarget() {
		return target;
	}
	
	public void setTarget(GuiScreen screen) {
		this.target = screen;
	}
	
	public static class Post extends GuiInitEvent {
		public Post(GuiScreen target) {
			super(target);
		}
	}
}

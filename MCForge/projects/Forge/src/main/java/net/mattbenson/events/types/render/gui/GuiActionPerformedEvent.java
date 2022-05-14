package net.mattbenson.events.types.render.gui;

import net.mattbenson.events.Event;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

//Hooked at net.minecraft.client.gui.GuiScreen.java
public class GuiActionPerformedEvent extends Event {
	private GuiScreen gui;
	private GuiButton button;
	
	public GuiActionPerformedEvent(GuiScreen gui, GuiButton button) {
		this.gui = gui;
		this.button = button;
	}
	
	public GuiScreen getGui() {
		return gui;
	}
	
	public GuiButton getButton() {
		return button;
	}
}

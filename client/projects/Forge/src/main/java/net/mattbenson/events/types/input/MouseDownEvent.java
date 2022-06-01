package net.mattbenson.events.types.input;

import net.mattbenson.events.Event;

//Hooked at net.minecraft.client.Minecraft.java
public class MouseDownEvent extends Event {
	private int button;
	
	public MouseDownEvent(int button) {
		this.button = button;
	}
	
	public int getButton() {
		return button;
	}
}

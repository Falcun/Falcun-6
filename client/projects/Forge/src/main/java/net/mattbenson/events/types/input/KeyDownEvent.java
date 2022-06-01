package net.mattbenson.events.types.input;

import net.mattbenson.events.Event;

//Hooked at net.minecraft.client.Minecraft.java
public class KeyDownEvent extends Event {
	private int key;
	
	public KeyDownEvent(int key) {
		this.key = key;
	}
	
	public int getKey() {
		return key;
	}
}

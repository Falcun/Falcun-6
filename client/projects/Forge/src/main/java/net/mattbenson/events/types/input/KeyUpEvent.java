package net.mattbenson.events.types.input;

import net.mattbenson.events.Event;

//Hooked at net.minecraft.client.Minecraft.java
public class KeyUpEvent extends Event {
	private int key;
	
	public KeyUpEvent(int key) {
		this.key = key;
	}
	
	public int getKey() {
		return key;
	}
}

package net.mattbenson.events.types;

import net.mattbenson.events.Event;
import net.minecraft.client.Minecraft;

//Hooked at net.minecraft.client.Minecraft.java
public class MinecraftInitEvent extends Event {
	private Minecraft minecraft;
	
	public MinecraftInitEvent(Minecraft minecraft) {
		this.minecraft = minecraft;
	}
	
	public Minecraft getMinecraft() {
		return minecraft;
	}
}

package net.mattbenson.console;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.input.MouseDownEvent;
import net.minecraft.client.Minecraft;

public class ConsoleManager {
	private List<Console> consoles;
	
	public ConsoleManager() {
		this.consoles = new CopyOnWriteArrayList<>();
	}
	
	public List<Console> getConsoles() {
		return consoles;
	}
}

package net.mattbenson.events.types.network;

import net.mattbenson.events.Event;
import net.minecraft.util.IChatComponent;

//Hooked at net.minecraft.client.network.NetHandlerPlayClient.java
public class ChatMessageEvent extends Event {
	public IChatComponent message;
	
	public ChatMessageEvent(IChatComponent message) {
		this.message = message;
	}
	
	public IChatComponent getMessage() {
		return message;
	}
}

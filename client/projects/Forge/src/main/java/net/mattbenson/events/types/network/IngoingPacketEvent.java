package net.mattbenson.events.types.network;

import net.mattbenson.events.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

//Hook at net.minecraft.network.NetworkManager.java
public class IngoingPacketEvent extends Event {
	private Packet<?> packet;
	
	public IngoingPacketEvent(Packet<?> packet) {
		this.packet = packet;
	}
	
	public Packet<?> getPacket() {
		return packet;
	}
}

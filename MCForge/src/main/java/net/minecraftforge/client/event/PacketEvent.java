package net.minecraftforge.client.event;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PacketEvent extends Event {

	private Packet packet;
	
	public PacketEvent(Packet packet) {
		this.packet = packet;
	}
	
	public static class Recieve extends PacketEvent {
		public Recieve(Packet packet) {
			super(packet);
		}
	}
	
	public static class Output extends PacketEvent {
		public Output(Packet packet) {
			super(packet);
		}
	}
	
	public Packet getPacket() {
		return this.packet;
	}
}

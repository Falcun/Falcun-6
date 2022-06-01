package net.mattbenson.network.network.packets.sessions;

import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.network.Packet;

public class RejectSession implements Packet {

	@Override
	public String getPacketName() {
		return "RejectSession";
	}

	@Override
	public int getArgsWanted() {
		return 0;
	}

	@Override
	public void onReceive(String[] args) {
		NetworkingClient.nextFresh = true;
	}
}
package net.mattbenson.network.network.packets.account;

import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.network.Packet;

public class Heartbeat implements Packet {

	@Override
	public String getPacketName() {
		return "Heartbeat";
	}

	@Override
	public int getArgsWanted() {
		return 0;
	}

	@Override
	public void onReceive(String[] args) {
		NetworkingClient.networkManager.updateLastHeartbeat();
	}
}

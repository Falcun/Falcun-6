package net.mattbenson.network.network;

public interface Packet {
	String getPacketName();
	int getArgsWanted();
	
	void onReceive(String[] args);
}

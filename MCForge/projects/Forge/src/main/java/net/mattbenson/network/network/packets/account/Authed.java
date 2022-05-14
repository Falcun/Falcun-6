package net.mattbenson.network.network.packets.account;

import net.mattbenson.Falcun;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.network.Packet;
import net.mattbenson.utils.NetworkUtils;
import net.minecraft.client.Minecraft;

public class Authed implements Packet {
	
	@Override
	public String getPacketName() {
		return "Authed";
	}

	@Override
	public int getArgsWanted() {
		return 0;
	}

	@Override
	public void onReceive(String[] args) {
		NetworkingClient.networkManager.setAuthed();
		NetworkingClient.displayNextError = true;
		
		if(NetworkUtils.getServer().length() > 0) {
			NetworkingClient.sendLine("ConnectServer", NetworkUtils.getServer());
		}
	}
}

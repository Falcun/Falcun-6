package net.mattbenson.events.types.network;

import net.mattbenson.events.Event;

//Hooked at net.mattbenson.utils.NetworkUtils.java
public class ServerSwitchEvent extends Event {
	private String serverData;
	
	public ServerSwitchEvent(String serverData) {
		this.serverData = serverData;
	}
	
	public String getServerData() {
		return serverData;
	}
}

package net.mattbenson.network.common;

import java.util.UUID;

public class UserInfo {
	private long id;
	private UUID uuid;
	private String name;
	private boolean online;
	private ServerStatus serverStatus;
	private String server;
	
	public UserInfo(long id, UUID uuid, String name, boolean online, ServerStatus serverStatus, String server) {
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.online = online;
		this.serverStatus = serverStatus;
		this.server = server;
	}
	
	public long getId() {
		return id;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isOnline() {
		return online;
	}
	
	public ServerStatus getServerStatus() {
		return serverStatus;
	}
	
	public String getServer() {
		return server;
	}
}

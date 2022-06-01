package net.mattbenson.network.common;

import java.util.List;
import java.util.UUID;

public class GroupData {
	private long id;
	private String name;
	private UUID owner;
	private int color;
	private List<UserInfo> users;
	public String channel;
	public String discordunique;
	public String inviteCode;
	public boolean wallChecker;
	public int checktime;
	public int checkdelay;
	
	public GroupData(long id, String name, UUID owner, int color, String channel, String discordunique, boolean wallChecker, int checktime, int checkdelay, String inviteCode, List<UserInfo> users) {
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.color = color;
		this.users = users;
		this.channel = channel;
		this.discordunique = discordunique;
		this.wallChecker = wallChecker;
		this.checktime = checktime;
		this.checkdelay = checkdelay;
		this.inviteCode = inviteCode;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public UUID getOwner() {
		return owner;
	}
	
	public int getColor() {
		return color;
	}

	public String GetChannel() {
		return channel;
	}
	
	public String GetDiscordUnique() {
		return discordunique;
	}
	
	public boolean GetwallChecker() {
		return wallChecker;
	}
	
	public int GetcheckTime() {
		return checktime;
	}
	
	public int GetcheckDelay() {
		return checkdelay;
	}
	
	public List<UserInfo> getUsers() {
		return users;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public UserInfo getUserByUUID(UUID uuid) {
		for(UserInfo user : users) {
			if(user.getUUID().equals(uuid)) {
				return user;
			}
		}
		
		return null;
	}
}

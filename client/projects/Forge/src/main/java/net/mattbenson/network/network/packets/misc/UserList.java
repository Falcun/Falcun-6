package net.mattbenson.network.network.packets.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;

import net.mattbenson.network.network.Packet;

public class UserList implements Packet {
	private static List<UUID> users;
	
	public UserList() {
		users = new ArrayList<>();
	}
	
	@Override
	public String getPacketName() {
		return "UserList";
	}

	@Override
	public int getArgsWanted() {
		return 1;
	}

	@Override
	public void onReceive(String[] args) {
		String data = args[0];
		
		JSONArray array = new JSONArray(data);
		
		List<UUID> users = new ArrayList<>();
		
		for(int i = 0; i < array.length(); i++) {
			users.add(UUID.fromString(array.getString(i)));
		}
		
		UserList.users = users;
	}
	
	public static boolean usesFalcun(UUID uuid) {
		return users.contains(uuid);
	}
}

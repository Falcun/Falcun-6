package net.mattbenson.network.network.packets.friends;

import java.util.HashMap;
import java.util.Map;

import net.mattbenson.network.network.Packet;

public class FriendChatMessage implements Packet {
	private final static long EXPIRE_TIME = 1 * 1000L;
	private Map<String, Long> times;
	
	public FriendChatMessage() {
		this.times = new HashMap<>();
	}
	
	@Override
	public String getPacketName() {
		return "FriendChatMessage";
	}

	@Override
	public int getArgsWanted() {
		return 2;
	}
	
	@Override
	public void onReceive(String[] args) {
		String user = args[0];
		String message = args[1];
		
		if(hasExpired(user + ":" + message)) {
			return;
		}
		
		//Wrapper.getInstance().addChat(user + " > " + message);
	}
	
	public boolean hasExpired(String message) {
		long time = System.currentTimeMillis();
		
		if(times.containsKey(message)) {
			if(time - times.get(message) < EXPIRE_TIME) {
				return true;
			} else {
				times.entrySet().removeIf(entry -> {
					return time - entry.getValue() > EXPIRE_TIME;
				});
			}
		}
		
		times.put(message, time);
		return false;
	}
}

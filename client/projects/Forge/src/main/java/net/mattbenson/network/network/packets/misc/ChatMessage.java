package net.mattbenson.network.network.packets.misc;

import java.util.HashMap;
import java.util.Map;

import net.mattbenson.chat.ChatUtils;
import net.mattbenson.network.network.Packet;

public class ChatMessage implements Packet {
	private final static long EXPIRE_TIME = 1 * 1000L;
	private Map<String, Long> times;
	
	public ChatMessage() {
		this.times = new HashMap<>();
	}
	
	@Override
	public String getPacketName() {
		return "ChatMessage";
	}

	@Override
	public int getArgsWanted() {
		return 1;
	}

	@Override
	public void onReceive(String[] args) {
		String message = args[0];
		
		if(hasExpired(message)) {
			return;
		}
		
		ChatUtils.sendLocalMessage(message, true);
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

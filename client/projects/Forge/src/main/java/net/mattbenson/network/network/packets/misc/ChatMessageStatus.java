package net.mattbenson.network.network.packets.misc;

import java.util.HashMap;
import java.util.Map;

import net.mattbenson.chat.ChatUtils;
import net.mattbenson.network.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatMessageStatus implements Packet {
	private final static long EXPIRE_TIME = 1 * 1000L;
	private Map<String, Long> times;
	
	public ChatMessageStatus() {
		this.times = new HashMap<>();
	}
	
	@Override
	public String getPacketName() {
		return "ChatMessageStatus";
	}

	@Override
	public int getArgsWanted() {
		return 3;
	}

	@Override
	public void onReceive(String[] args) {
		String message = args[0];
		String group = args[1];
		String name = args[2];

		
		if(hasExpired(message)) {
			return;
		}
		

		ChatComponentText cpt = new ChatComponentText(EnumChatFormatting.BLUE + group + EnumChatFormatting.WHITE + " | " + EnumChatFormatting.GOLD + name + EnumChatFormatting.WHITE + " " + message);
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(cpt);
		
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

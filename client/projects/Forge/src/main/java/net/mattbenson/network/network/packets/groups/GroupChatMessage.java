package net.mattbenson.network.network.packets.groups;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class GroupChatMessage implements Packet {
	private final static long EXPIRE_TIME = 1 * 1000L;
	private Map<String, Long> times;
	
	public GroupChatMessage() {
		this.times = new HashMap<>();
	}
	
	@Override
	public String getPacketName() {
		return "GroupChatMessage";
	}

	@Override
	public int getArgsWanted() {
		return 3;
	}

	@Override
	public void onReceive(String[] args) {
		if(!isLong(args[0])) {
			return;
		}
		
		long group = Long.parseLong(args[0]);
		
		GroupData data = GroupList.getGroupById(group);
		
		if(data != null) {
			try{
			    UUID sender = UUID.fromString(args[1]);
				String message = args[2];
				
				
				System.out.println(data + ":" + message);
				if(hasExpired(message)) {
					return;
				}
				

	
						
					ChatComponentText cpt = new ChatComponentText(EnumChatFormatting.BLUE + " | " + EnumChatFormatting.GOLD + data.getName() + EnumChatFormatting.WHITE + " just went online.");
					Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(cpt);
				
				
				
				
			} catch (IllegalArgumentException exception){
			    String sender1 = args[1];
			    
				String message = args[2];
				
				if(hasExpired(message)) {
					return;
				}
				System.out.println(message);
				//Wrapper.getInstance().onGroupMessage(data, sender1, message);			
			}
		}
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
	
	public boolean isLong(String string) {
		try {
			Long.valueOf(string);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
}

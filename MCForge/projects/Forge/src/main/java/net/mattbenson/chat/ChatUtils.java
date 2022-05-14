package net.mattbenson.chat;

import net.mattbenson.Falcun;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ChatUtils {
	static String chatPrefix = ChatColor.DARK_BLUE + "FALCUN" + ChatColor.GOLD + " > " + ChatColor.YELLOW;
	
	public ChatUtils() {}
	
	public static void sendLocalMessage(String message, boolean prefix) {
		if(Minecraft.getMinecraft().thePlayer != null)
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText((prefix ? chatPrefix : "") + message.replaceAll(" ", " " + ChatColor.YELLOW.toString())));
	}
	
	public static void sendMessage(String message) {
		if(Minecraft.getMinecraft().thePlayer != null)
			Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
	}
	
	public static void addMessageToHistory(String message) {
		if(Minecraft.getMinecraft().thePlayer != null)
			Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(message);
	}
	
	public static void clearChat() {
		if(Minecraft.getMinecraft().thePlayer != null)
			Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
	}
}

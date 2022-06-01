package net.mattbenson.network.network.packets.emoticons;

import java.util.UUID;

import mchorse.emoticons.common.EmoteAPI;
import net.mattbenson.Falcun;
import net.mattbenson.network.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;

public class EmoticonStop implements Packet {

	@Override
	public String getPacketName() {
		return "EmoticonStop";
	}

	@Override
	public int getArgsWanted() {
		return 1;
	}

	@Override
	public void onReceive(String[] args) {
		if(!Falcun.getInstance().emoteSettings.isEnabled()) {
			return;
		}
		
		UUID user = UUID.fromString(args[0]);
		
		WorldClient world = Minecraft.getMinecraft().theWorld;
		
		if(world == null) {
			return;
		}
		
		EntityPlayer player = world.getPlayerEntityByUUID(user);
		
		if(player == null) {
			return;
		}
		
		if(player.getDisplayName().getUnformattedText().equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText())) {
			return;
		}
		
		EmoteAPI.setEmote("", player);		
	}
}

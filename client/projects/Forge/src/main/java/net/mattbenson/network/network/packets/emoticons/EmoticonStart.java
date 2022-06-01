package net.mattbenson.network.network.packets.emoticons;

import java.util.UUID;

import mchorse.emoticons.common.EmoteAPI;
import net.mattbenson.Falcun;
import net.mattbenson.modules.types.other.PerspectiveMod;
import net.mattbenson.network.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;

public class EmoticonStart implements Packet {

	@Override
	public String getPacketName() {
		return "EmoticonStart";
	}

	@Override
	public int getArgsWanted() {
		return 2;
	}

	@Override
	public void onReceive(String[] args) {
		if(!Falcun.getInstance().emoteSettings.isEnabled()) {
			return;
		}
		
		UUID user = UUID.fromString(args[0]);
		String emote = args[1];
		
		WorldClient world = Minecraft.getMinecraft().theWorld;
		
		if(world == null) {
			return;
		}
		
		EntityPlayer player = world.getPlayerEntityByUUID(user);
		
		if(player == null) {
			return;
		}
		
		if(player.getDisplayName().getUnformattedText().equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText())) {
			if(!PerspectiveMod.perspectiveToggled) {
				PerspectiveMod.cameraYaw = Minecraft.getMinecraft().getRenderManager().playerViewY + 180;
				PerspectiveMod.cameraPitch = 0;
				PerspectiveMod.perspectiveToggled = true; 
				Falcun.getInstance().moduleManager.getModule(PerspectiveMod.class).isEmoting = true; 
				Minecraft.getMinecraft().gameSettings.thirdPersonView = 1;
			}
		}
		
		EmoteAPI.setEmote(emote, player);
	}
}

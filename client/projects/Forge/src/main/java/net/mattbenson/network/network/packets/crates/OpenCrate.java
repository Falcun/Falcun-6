package net.mattbenson.network.network.packets.crates;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import net.mattbenson.Falcun;
import net.mattbenson.network.common.Reward;
import net.mattbenson.network.common.RewardRating;
import net.mattbenson.network.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;

public class OpenCrate implements Packet {

	@Override
	public String getPacketName() {
		return "OpenCrate";
	}

	@Override
	public int getArgsWanted() {
		return 3;
	}

	@Override
	public void onReceive(String[] args) {
		if(!Falcun.getInstance().emoteSettings.isEnabled()) {
			return;
		}
		
		UUID user = UUID.fromString(args[0]);
		String items = args[1];
		String reward = args[2];

		WorldClient world = Minecraft.getMinecraft().theWorld;
		
		if(world == null) {
			return;
		}
		
		EntityPlayer player = world.getPlayerEntityByUUID(user);
		
		if(player == null) {
			return;
		}
		
		List<Reward> rewards = new ArrayList<>();
		
		JSONArray array = new JSONArray(items);
		
		for(int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			
			String name = obj.getString("name");
			String rating = obj.getString("rating");
			
			RewardRating found = RewardRating.valueOf(rating.toUpperCase());
			
			if(found != null) {
				rewards.add(new Reward(name, found));
			}
		}
		
		Falcun.getInstance().crateManager.addCrate(player, rewards, reward);
	}
}

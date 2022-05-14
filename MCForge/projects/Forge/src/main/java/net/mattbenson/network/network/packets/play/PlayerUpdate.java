package net.mattbenson.network.network.packets.play;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import net.mattbenson.Falcun;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.network.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

public class PlayerUpdate implements Packet {
	private static final long MIN_UPDATE_INTERVAL = 1500L;

	private static long lastSend;
	
	public PlayerUpdate() {
		lastSend = System.currentTimeMillis();
	}
	
	@Override
	public String getPacketName() {
		return "PlayerUpdate";
	}

	@Override
	public int getArgsWanted() {
		return 2;
	}
	
	@Override
	public void onReceive(String[] args) {
		String group = args[0];
		String json = args[1];

		if(!StringUtils.isLong(group)) {
			return;
		}
		
		long groupId = Long.parseLong(group);
		GroupData found = null;	
		
		for(GroupData data : GroupList.getGroups()) {
			if(data.getId() != groupId) {
				continue;
			}
			
			found = data;
			break;
		}
		
		if(found == null) {
			return;
		}
		
		try {
			JSONObject obj = new JSONObject(json);
			
			String name = "";
			UUID uuid = null;
			long time = 0;
			double x = 0;
			double y = 0;
			double z = 0;
			float health = 0;
			int potions = 0;
			int dimension = 0;
			
			if(obj.has("name")) {
				name = obj.getString("name");
			}
			
			if(obj.has("uuid")) {
				try {
					uuid = UUID.fromString(obj.getString("uuid"));
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
				}
			}

			if(obj.has("x")) {
				x = obj.getDouble("x");
			}
			
			if(obj.has("y")) {
				y = obj.getDouble("y");
			}
			
			if(obj.has("z")) {
				z = obj.getDouble("z");
			}
			
			if(obj.has("time")) {
				time = obj.getLong("time");
			}
			
			if(obj.has("health")) {
				health = obj.getLong("health");
			}
			
			if(obj.has("potions")) {
				potions = obj.getInt("potions");
			}
			
			if(obj.has("dimension")) {
				dimension = obj.getInt("dimension");
			}
			
			Falcun.getInstance().locationManager.addLocation(uuid, name, found, time, x, y, z, dimension, health, potions);
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void writePlayerUpdate() {
		if(System.currentTimeMillis() - lastSend > MIN_UPDATE_INTERVAL) {
			lastSend = System.currentTimeMillis();
			
			EntityPlayerSP entity = Minecraft.getMinecraft().thePlayer;
			
			if(entity == null) {
				return;
			}
			
			JSONObject obj = new JSONObject();
			
			obj.put("name", Minecraft.getMinecraft().thePlayer.getName());
			obj.put("uuid", entity.getUniqueID());
			obj.put("x", entity.posX + entity.width / 2);
			obj.put("y", entity.posY + entity.height);
			obj.put("z", entity.posZ + entity.width / 2);
			obj.put("time", System.currentTimeMillis());
			obj.put("dimension", entity.dimension);
			
			float healthPercentage = entity.getHealth();
			
			if(healthPercentage > 1) {
				healthPercentage = 1;
			} else if(healthPercentage < 0) {
				healthPercentage = 0;
			}
		
			obj.put("health", Minecraft.getMinecraft().thePlayer.getHealth());
			
			int potions = 0;
			
			for(ItemStack stack : entity.inventory.mainInventory) {
				if(stack == null || stack.getItem() == null) {
					continue;
				}
				
				if(stack.getItem() instanceof ItemPotion) {
					potions++;
				}
			}
			
			obj.put("potions", potions);
			
			for(GroupData group : GroupList.getGroups()) {
				if (Minecraft.getMinecraft().theWorld != null)
					NetworkingClient.sendLine("PlayerIngameUpdate", group.getId() + "", obj.toString());
			}
		}
	}
}

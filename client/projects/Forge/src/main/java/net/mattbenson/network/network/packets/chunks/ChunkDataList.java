package net.mattbenson.network.network.packets.chunks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mapwriter.overlay.OverlayGroup;
import net.mattbenson.Falcun;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.common.ChunkData;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.network.packets.chunks.data.ChunkColor;
import net.mattbenson.network.network.packets.chunks.data.ChunkName;
import net.mattbenson.utils.NetworkUtils;

public class ChunkDataList implements Packet {
	private static List<ChunkData> chunkDatas;
	
	public ChunkDataList() {
		chunkDatas = new CopyOnWriteArrayList<>();
	}
	
	@Override
	public String getPacketName() {
		return "ChunkDataList";
	}

	@Override
	public int getArgsWanted() {
		return 1;
	}

	@Override
	public void onReceive(String[] args) {
		String json = args[0];
		
		JSONArray array = new JSONArray(json);
		List<ChunkData> chunkDatas = new CopyOnWriteArrayList<>();
		
		for(int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			String data = obj.getString("data");
			long id = obj.getLong("id");
			long group = obj.getLong("group");
			
			try {
				ChunkData chunkData = null;
				JSONObject info = new JSONObject(data);
				
				String type = info.getString("type");
				int x = info.getInt("x");
				int y = info.getInt("y");
				int z = info.getInt("z");
				String server = info.getString("server");
				int dimension = info.getInt("dimension");
				
				if(type.equalsIgnoreCase("color")) {
					int color = info.getInt("color");
					
					chunkData = new ChunkColor(id, data, group, x, y, z, server, dimension, color);
				} else if(type.equalsIgnoreCase("name")) {
					String chunkName = info.getString("name");
					
					chunkData = new ChunkName(id, data, group, x, y, z, server, dimension, chunkName);
				} else {
					continue;
				}
				
				chunkDatas.add(chunkData);
			} catch(JSONException e) {
				Falcun.getInstance().log.error("Failed to parse chunk data list.", e);
			}
		}
		
		List<Long> remove = new ArrayList<>();
		
		for(ChunkData first : chunkDatas) {
			if(remove.contains(first.getId())) {
				continue;
			}
			
			for(ChunkData second : chunkDatas) {
				if(first.getId() == second.getId()) {
					continue;
				}
				
				if(first.getClass() != second.getClass()) {
					continue;
				}
				
				if(first.equals(second)) {
					if(!remove.contains(second.getId())) {
						remove.add(second.getId());
					}
				}
			}
		}
		
		chunkDatas.removeIf(entry -> {
			return remove.contains(entry.getId());	
		});
		
		ChunkDataList.chunkDatas = chunkDatas;
		
		OverlayGroup.updateMap();
		//Wrapper.getInstance().updateGroups(false);
	}
	
	public static List<ChunkData> getChunkDatas() {
		return chunkDatas;
	}
	
	public static void sendChunkColor(int x, int y, int z, int color, int dimension, long group) {
		JSONObject json = new JSONObject();
		json.put("x", x);
		json.put("y", y);
		json.put("z", z);
		json.put("server", NetworkUtils.getServer());
		json.put("dimension", dimension);
		json.put("color", color);
		json.put("type", "color");
		
		NetworkingClient.sendLine("NewChunkData", json.toString(), group + "");
	}
	
	public static void sendNameChunk(int x, int y, int z, String name, int dimension, long group) {
		JSONObject json = new JSONObject();
		json.put("x", x);
		json.put("y", y);
		json.put("z", z);
		json.put("server", NetworkUtils.getServer());
		json.put("dimension", dimension);
		json.put("name", name);
		json.put("type", "name");
		
		NetworkingClient.sendLine("NewChunkData", json.toString(), group + "");
	}
	
	public static void deleteChunkData(long group) {
		NetworkingClient.sendLine("RemoveWaypoint", group + "");
	}
}

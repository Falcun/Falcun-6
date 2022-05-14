package net.mattbenson.network.network.packets.chunks;

import org.json.JSONException;
import org.json.JSONObject;

import mapwriter.overlay.OverlayGroup;
import net.mattbenson.Falcun;
import net.mattbenson.network.common.ChunkData;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.network.packets.chunks.data.ChunkColor;
import net.mattbenson.network.network.packets.chunks.data.ChunkName;

public class NewChunkData implements Packet {
	@Override
	public String getPacketName() {
		return "NewChunkData";
	}

	@Override
	public int getArgsWanted() {
		return 1;
	}

	@Override
	public void onReceive(String[] args) {
		String json = args[0];
		
		try {
			JSONObject obj = new JSONObject(json);
			String data = obj.getString("data");
			long id = obj.getLong("id");
			long group = obj.getLong("group");
			
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
				return;
			}
			
			ChunkDataList.getChunkDatas().add(chunkData);
		} catch(JSONException e) {
			Falcun.getInstance().log.error("Failed to parse new chunk data block.", e);
		}
		
		OverlayGroup.updateMap();
	}
}
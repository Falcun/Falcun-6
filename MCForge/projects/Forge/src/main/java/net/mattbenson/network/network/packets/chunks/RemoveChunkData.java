package net.mattbenson.network.network.packets.chunks;

import mapwriter.overlay.OverlayGroup;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.utils.StringUtils;

public class RemoveChunkData implements Packet {
	@Override
	public String getPacketName() {
		return "RemoveChunkData";
	}

	@Override
	public int getArgsWanted() {
		return 1;
	}

	@Override
	public void onReceive(String[] args) {
		String id = args[0];
		
		if(!StringUtils.isLong(id)) {
			return;
		}
		
		long rowId = Long.parseLong(id);
		
		ChunkDataList.getChunkDatas().removeIf(entry -> {
			return entry.getId() == rowId;
		});
		
		OverlayGroup.updateMap();
	}
}
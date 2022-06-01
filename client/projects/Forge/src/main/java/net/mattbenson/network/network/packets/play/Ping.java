package net.mattbenson.network.network.packets.play;

import net.mattbenson.Falcun;
import net.mattbenson.modules.types.mods.GroupPingLocation;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.network.utils.StringUtils;
import net.minecraft.util.BlockPos;

public class Ping implements Packet {
	@Override
	public String getPacketName() {
		return "Ping";
	}

	@Override
	public int getArgsWanted() {
		return 7;
	}
	
	@Override
	public void onReceive(String[] args) {
		String group = args[0];
		String direction = args[1];
		String face = args[2];
		String x = args[3];
		String y = args[4];
		String z = args[5];
		String name = args[6];
		
		if(!StringUtils.isLong(group) || !StringUtils.isDouble(x) || !StringUtils.isDouble(y) || !StringUtils.isDouble(z)) {
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
		
		double xPos = Double.parseDouble(x);
		double yPos = Double.parseDouble(y);
		double zPos = Double.parseDouble(z);
		
		GroupPingLocation location = Falcun.getInstance().moduleManager.getModule(GroupPingLocation.class);
		location.addPing(direction, face, new BlockPos(xPos, yPos, zPos), found, name);
	}
}

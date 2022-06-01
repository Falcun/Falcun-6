package net.mattbenson.network.network.packets.play;

import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.network.utils.StringUtils;

public class WallChecker implements Packet {
	@Override
	public String getPacketName() {
		return "WallChecker";
	}

	@Override
	public int getArgsWanted() {
		return 1;
	}
	
	@Override
	public void onReceive(String[] args) {
		String group = args[0];
		
		if(!StringUtils.isLong(group)) {
			return;
		}
		
		long groupId = Long.parseLong(group);
		GroupData found = null;
		
		for(GroupData data : GroupList.getGroups()) {
			if(data.getId() != groupId) {
				continue;
			}
			
			if(!GroupList.getSettings(data.getName()).isPatchcrumbs()) {
				return;
			}
			
			found = data;
			break;
		}
		
		if(found == null) {
			return;
		}
		

	}
}

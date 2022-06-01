package net.mattbenson.network.network.packets.play;

import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.network.utils.StringUtils;

public class Patchcrumb implements Packet {
	@Override
	public String getPacketName() {
		return "Patchcrumb";
	}

	@Override
	public int getArgsWanted() {
		return 11;
	}
	
	@Override
	public void onReceive(String[] args) {
		String group = args[0];
		String x = args[1];
		String y = args[2];
		String z = args[3];
		String minX = args[4];
		String minY = args[5];
		String minZ = args[6];
		String maxX = args[7];
		String maxY = args[8];
		String maxZ = args[9];
		String server = args[10];
		
		if(!StringUtils.isLong(group) || !StringUtils.isDouble(x) || !StringUtils.isDouble(y)) {
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
		
		double xPos = Double.parseDouble(x);
		double yPos = Double.parseDouble(y);
		double zPos = Double.parseDouble(z);
		
		//GroupPatchcrumb location = Falcun.MM.get(GroupPatchcrumb.class);
		//location.addCrumb(xPos, yPos, zPos, minX, minY, minZ, maxX, maxY, maxZ, found, server);

	}
}

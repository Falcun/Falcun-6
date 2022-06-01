package net.mattbenson.network.network.packets.play;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

import com.github.lunatrius.schematica.proxy.ClientProxy;

import net.mattbenson.Falcun;
import net.mattbenson.file.FileHandler;
import net.mattbenson.modules.types.mods.Schematica;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.network.utils.StringUtils;
import net.minecraft.util.EnumFacing;

public class SchemShare implements Packet {
	@Override
	public String getPacketName() {
		return "SchemShare";
	}

	@Override
	public int getArgsWanted() {
		return 8;
	}
	
	@Override
	public void onReceive(String[] args) {
		String group = args[0];
		String name = args[1];
		String x = args[2];
		String y = args[3];
		String z = args[4];
		String schem = args[5];
		String flips = args[6];
		String rotations = args[7];
		
		if(!StringUtils.isLong(group) || !StringUtils.isDouble(x) || !StringUtils.isDouble(y) || !StringUtils.isDouble(z)) {
			return;
		}
		
		List<EnumFacing> flipList = new ArrayList<>();
		List<EnumFacing> rotationList = new ArrayList<>();
		
		for(String f : flips.split(Pattern.quote("\\n"))) {
			EnumFacing facing = EnumFacing.byName(f);
			
			if(facing == null) {
				continue;
			}
			
			flipList.add(facing);
		}
		
		for(String f : rotations.split(Pattern.quote("\\n"))) {
			EnumFacing facing = EnumFacing.byName(f);
			
			if(facing == null) {
				continue;
			}
			
			rotationList.add(facing);
		}
		
		long groupId = Long.parseLong(group);
		
		GroupData data = GroupList.getGroupById(groupId);
		
		if(data == null) {
			return;
		}
		
		int xPos = Integer.parseInt(x);
		int yPos = Integer.parseInt(y);
		int zPos = Integer.parseInt(z);
		
		Falcun.getInstance().schemShareManager.addNewRequest(data, name, xPos, yPos, zPos, schem, flipList, rotationList);
	}
	
	public static void shareSchem(long group) {
		if(ClientProxy.schematic == null || ClientProxy.loadedFile == null) {
			return;
		}
		
		String name = ClientProxy.loadedFile.getName();
		int x = ClientProxy.schematic.position.x;
		int y = ClientProxy.schematic.position.y;
		int z = ClientProxy.schematic.position.z;
		String schem = "";
		StringBuilder flips = new StringBuilder();
		StringBuilder rotations = new StringBuilder();
		
		for(String action : Schematica.flipsAndRotations.split("\n")) {
			String prefix = "";
			int mode = 0;
			
			if(action.startsWith("FLIP:")) {
				prefix = "FLIP:";
				mode = 1;
			} else if(action.startsWith("ROTATION:")) {
				prefix = "ROTATION:";
				mode = 2;
			}
			
			if(mode == 0) {
				continue;
			}
			
			String facing = action.substring(prefix.length());
			
			if(mode == 1) {
				if(!flips.toString().isEmpty()) {
					flips.append("\n");
				}
				
				flips.append(facing);
			} else if(mode == 2) {
				if(!rotations.toString().isEmpty()) {
					rotations.append("\n");
				}
				
				rotations.append(facing);
			}
		}
		
		FileHandler handler = new FileHandler(ClientProxy.loadedFile);
		
		try {
			handler.init();
			schem = Base64.getEncoder().encodeToString(handler.getContentInBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		if(flips.toString().isEmpty()) {
			flips.append("\n");
		}
		
		if(rotations.toString().isEmpty()) {
			rotations.append("\n");
		}
		
		NetworkingClient.sendLine("SchemShare", name, group + "", x + "", y + "", z + "", schem, flips.toString(), rotations.toString());
	}
}

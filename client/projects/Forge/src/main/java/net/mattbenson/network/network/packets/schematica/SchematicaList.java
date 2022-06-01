package net.mattbenson.network.network.packets.schematica;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.lunatrius.schematica.proxy.ClientProxy;

import net.mattbenson.Falcun;
import net.mattbenson.file.FileHandler;
import net.mattbenson.modules.types.mods.Schematica;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.common.SchemShareRequest;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.utils.NetworkUtils;
import net.minecraft.util.EnumFacing;

public class SchematicaList implements Packet {
	private static List<SchemShareRequest> schematicas;
	
	public SchematicaList() {
		schematicas = new ArrayList<>();
	}
	
	@Override
	public String getPacketName() {
		return "SchematicaList";
	}

	@Override
	public int getArgsWanted() {
		return 1;
	}

	@Override
	public void onReceive(String[] args) {
		String json = args[0];
		
		JSONArray array = new JSONArray(json);
		List<SchemShareRequest> schematicas = new ArrayList<>();
		
		for(int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			
			try {
				long id = obj.getLong("id");
				long group = obj.getLong("group");
				String name = obj.getString("name");
				int x = obj.getInt("x");
				int y = obj.getInt("y");
				int z = obj.getInt("z");
				String schem = obj.getString("schem");
				String flips = obj.getString("flips");
				String rotations = obj.getString("rotations");
				String server = obj.getString("server");
				
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
				
				GroupData data = GroupList.getGroupById(group);
				
				if(data == null) {
					continue;
				}
				
				schematicas.add(new SchemShareRequest(id, data, name, x, y, z, schem, flipList, rotationList, server));
			} catch(JSONException e) {
				e.printStackTrace();
			}
		}
		
		SchematicaList.schematicas = schematicas;
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
		
		NetworkingClient.sendLine("NewSchematica", name, group + "", x + "", y + "", z + "", schem, flips.toString(), rotations.toString(), NetworkUtils.getServer());
	}
	
	public static List<SchemShareRequest> getSchematics() {
		return schematicas;
	}
}

package net.mattbenson.network.network.packets.waypoints;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import mapwriter.Mw;
import mapwriter.map.Marker;
import mapwriter.util.Utils;
import net.mattbenson.gui.menu.Category;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.pages.GroupsPage;
import net.mattbenson.gui.menu.pages.groups.GroupSubTab;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.common.GroupSetting;
import net.mattbenson.network.common.WaypointMarker;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.utils.NetworkUtils;

public class WaypointList implements Packet {
	private static List<WaypointMarker> waypoints;
	
	public WaypointList() {
		waypoints = new ArrayList<>();
	}
	
	@Override
	public String getPacketName() {
		return "WaypointList";
	}

	@Override
	public int getArgsWanted() {
		return 1;
	}

	@Override
	public void onReceive(String[] args) {
		String json = args[0];
		
		JSONArray array = new JSONArray(json);
		List<WaypointMarker> waypoints = new ArrayList<>();
		
		for(int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			String name = obj.getString("name");
			String marker = obj.getString("marker");
			long id = obj.getLong("id");
			long group = obj.getLong("group");
			
			waypoints.add(new WaypointMarker(id, name, marker, group));
			
			
			
			GroupData data = GroupList.getGroupById(group);
			
			if(data == null) {
				continue;
			}
			
		
			
				try {
					JSONObject obj1 = new JSONObject(marker);
					String server = obj1.getString("server");
		
					
						
						double x = obj1.getDouble("x");
						double y = obj1.getDouble("y");
						double z = obj1.getDouble("z");
						int dimension = obj1.getInt("dimension");
						int color = obj1.getInt("color");
						boolean found = false;
						
						for(Marker m : Mw.getInstance().markerManagerGroup.markerList) {
							GroupData g = GroupList.getGroupById(group);
							
							if(g == null) {
								continue;
							}
							
							if(!m.groupName.equalsIgnoreCase(Utils.mungeStringForConfig(g.getName()))) {
								continue;
							}
							
							if(!m.name.equalsIgnoreCase(name)) {
								continue;
							}
							
							found = true;
						}
						
						
						
						if(!found) {
							Mw.getInstance().markerManagerGroup.addMarker(name, data.getName(), (int)Math.round(x), (int)Math.round(y), (int)Math.round(z), dimension, color, server);
						}
					
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				Mw.getInstance().markerManagerGroup.setVisibleGroupName("all");
	
		
		Mw.getInstance().markerManagerGroup.update();
			
			
		}
		
		WaypointList.waypoints = waypoints;
		
		if(IngameMenu.pageManager != null && IngameMenu.category == Category.GROUPS) {
			GroupsPage page = (GroupsPage) IngameMenu.pageManager.getPage(Category.GROUPS);
			
			if(page.subTab == GroupSubTab.WAYPOINTS) {
				page.populateScrollPane();
			}
		}
		
		
		
		
	

	}
	
		
		
		
		
	
	
	public static List<WaypointMarker> getWaypoints() {
		return waypoints;
	}
}

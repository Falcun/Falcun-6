package net.mattbenson.network.network.packets.groups;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mapwriter.Mw;
import mapwriter.map.Marker;
import mapwriter.util.Utils;
import net.mattbenson.Falcun;
import net.mattbenson.gui.menu.Category;
import net.mattbenson.gui.menu.IngameMenu;
import net.mattbenson.gui.menu.pages.GroupsPage;
import net.mattbenson.modules.types.mods.Friends;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.common.GroupSetting;
import net.mattbenson.network.common.Request;
import net.mattbenson.network.common.RequestType;
import net.mattbenson.network.common.ServerStatus;
import net.mattbenson.network.common.UserInfo;
import net.mattbenson.network.common.WaypointMarker;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.network.packets.friends.FriendList;
import net.mattbenson.network.network.packets.waypoints.WaypointList;
import net.mattbenson.utils.NetworkUtils;
import net.mattbenson.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class GroupList implements Packet {
	private static List<GroupData> groups;
	private static List<Request> ingoing;
	private static List<Request> outgoing;

	private static List<Long> outgoingTrack;
	private static List<String> loggedIn;
	private static boolean online = false;
    
	private static Map<String, GroupSetting> settings;
    
	public GroupList() {
		groups = new ArrayList<>();
		ingoing = new ArrayList<>();
		outgoing = new ArrayList<>();
		
		outgoingTrack = new ArrayList<>();
		loggedIn = new ArrayList<>();
		
		settings = new HashMap<>();
	}
	
	@Override
	public String getPacketName() {
		return "GroupList";
	}

	@Override
	public int getArgsWanted() {
		return 1;
	}

	@Override
	public void onReceive(String[] args) {
		
		String json = args[0];
		
		JSONObject obj = new JSONObject(json);
		
		if(obj.has("ingoing")) {
			JSONArray array = obj.getJSONArray("ingoing");
			List<Request> requests = new ArrayList<>();
			
			for(int i = 0; i < array.length(); i++) {
				JSONObject entry = array.getJSONObject(i);
				
				try {
					long id = entry.getLong("id");
					String name = entry.getString("name");
					
					requests.add(new Request(id, -1, name, null, RequestType.GROUP));
				} catch(JSONException e) {
					e.printStackTrace();
				}
			}
			
			GroupList.ingoing = requests;
		}
		
		if(obj.has("outgoing")) {

			JSONArray array = obj.getJSONArray("outgoing");
			List<Request> requests = new ArrayList<>();
			
			for(int i = 0; i < array.length(); i++) {
				JSONObject entry = array.getJSONObject(i);
				
				try {
					long id = entry.getLong("id");
					String name = entry.getString("name");
					
					requests.add(new Request(id, -1, name, null, RequestType.GROUP));
				} catch(JSONException e) {
					e.printStackTrace();
				}
			}
			
			GroupList.outgoing = requests;
		}
		
		if(obj.has("relationships")) {
			JSONArray array = obj.getJSONArray("relationships");
			List<GroupData> groups = new ArrayList<>();
			
			for(int i = 0; i < array.length(); i++) {
				JSONObject entry = array.getJSONObject(i);
				
				try {
					long id = entry.getLong("id");
					String name = entry.getString("name");
					int color = entry.getInt("color");
					String channel = entry.getString("channel");
					String discordunique = entry.getString("discordunique");
					boolean wallChecker = entry.getBoolean("wallchecker");
					int checktime = entry.getInt("checktime");
					int checkdelay = entry.getInt("checkdelay");
					String inviteCode = entry.getString("inviteCode");
					
					UUID owner = UUID.fromString(entry.getString("owner"));
					
					List<UserInfo> users = new ArrayList<>();
					
					JSONArray userArray = entry.getJSONArray("users");
					
					for(int ii = 0; ii < userArray.length(); ii++) {
						JSONObject userObj = userArray.getJSONObject(ii);
						
						long userId = userObj.getLong("id");
						String userName = userObj.getString("name");
						boolean online = userObj.getBoolean("online");
						UUID userUUID = UUID.fromString(userObj.getString("uuid"));
						String server = userObj.getString("server");
						String serverStatus = userObj.getString("serverstatus");
						
						ServerStatus status = ServerStatus.valueOf(serverStatus);
						
						if(status == null) {
							status = ServerStatus.OFFLINE;
						}
						
						users.add(new UserInfo(userId, userUUID, userName, online, status, server));
					}
					
					groups.add(new GroupData(id, name, owner, color, channel, discordunique, wallChecker, checktime, checkdelay, inviteCode, users));
				} catch(JSONException e) {
					e.printStackTrace();
				}
			}
			
			GroupList.groups = groups;
		}
		
		if(IngameMenu.pageManager != null && IngameMenu.category == Category.GROUPS) {
			GroupsPage page = (GroupsPage) IngameMenu.pageManager.getPage(Category.GROUPS);
			page.populateScrollPane();
		}
		onUpdateGroups();
		} 
	
	
	public static void onUpdateGroups() {
		
	
		
		for(GroupData group : GroupList.getGroups()) {
			for(UserInfo user : group.getUsers()) {
				if(user.isOnline()) {
					if(!loggedIn.contains(user.getName())) {
						loggedIn.add(user.getName());
					}
				} else {
					if(loggedIn.contains(user.getName())) {
						loggedIn.remove(user.getName());
					}
				
			}
		}
		}
		online = true;
	}
	
	public static List<GroupData> getGroups() {
		return groups;
	}
	
	public static List<Request> getIngoingRequests() {
		return ingoing;
	}
	
	public static List<Request> getOutgoingRequests() {
		return outgoing;
	}
	
	public static GroupData getGroupById(long id) {
		for(GroupData group : groups) {
			if(group.getId() == id) {
				return group;
			}
		}
		
		return null;
	}
	
	public static GroupData getGroupByName(String name) {
		for(GroupData group : groups) {
			if(group.getName().equalsIgnoreCase(name)) {
				return group;
			}
		}
		
		return null;
	}

	public static GroupSetting getSettings(String group) {
		if(settings == null) {
			settings = new HashMap<>();
		}
		
		if(!settings.containsKey(group)) {
			settings.put(group, new GroupSetting());
		}
		
		return settings.get(group);
	}
	
	public static String getGroupName(GroupData group) {
		String name = group.getName();
		
		return getGroupName(name);
	}
	
	public static String getGroupName(String name) {
		return name.trim();
	}
}

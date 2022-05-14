package net.mattbenson.network.network.packets.friends;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.mattbenson.Falcun;
import net.mattbenson.modules.types.mods.Friends;
import net.mattbenson.network.common.Request;
import net.mattbenson.network.common.RequestType;
import net.mattbenson.network.common.ServerStatus;
import net.mattbenson.network.common.UserInfo;
import net.mattbenson.network.network.Packet;

public class FriendList implements Packet {
	private static List<UserInfo> friends;
	private static List<Request> ingoing;
	private static List<Request> outgoing;
	
	private static List<Long> onlineTrack;
	private static List<Long> acceptedFriends;
	private static List<Long> outgoingTrack;
	private static boolean onlineInitd;
	
	public FriendList() {
		friends = new ArrayList<>();
		ingoing = new ArrayList<>();
		outgoing = new ArrayList<>();
		
		onlineTrack = new ArrayList<>();
		acceptedFriends = new ArrayList<>();
		outgoingTrack = new ArrayList<>();
	}
	
	@Override
	public String getPacketName() {
		return "FriendList";
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
					long userid = entry.getLong("userid");
					String name = entry.getString("name");
					UUID uuid = UUID.fromString(entry.getString("uuid"));
					
					requests.add(new Request(id, userid, name, uuid, RequestType.FRIEND));
				} catch(JSONException e) {
					e.printStackTrace();
				}

			}
			
			FriendList.ingoing = requests;
		}
		
		if(obj.has("outgoing")) {
			JSONArray array = obj.getJSONArray("outgoing");
			List<Request> requests = new ArrayList<>();
			
			for(int i = 0; i < array.length(); i++) {
				JSONObject entry = array.getJSONObject(i);
				
				try {
					long id = entry.getLong("id");
					long userid = entry.getLong("userid");
					String name = entry.getString("name");
					UUID uuid = UUID.fromString(entry.getString("uuid"));
					
					requests.add(new Request(id, userid, name, uuid, RequestType.FRIEND));
				} catch(JSONException e) {
					e.printStackTrace();
				}
			}
			
			FriendList.outgoing = requests;
		}
		
		if(obj.has("relationships")) {
			JSONArray array = obj.getJSONArray("relationships");
			List<UserInfo> friends = new ArrayList<>();
			
			for(int i = 0; i < array.length(); i++) {
				JSONObject entry = array.getJSONObject(i);
				
				try {
					long id = entry.getLong("userid");
					UUID uuid = UUID.fromString(entry.getString("uuid"));
					String name = entry.getString("name");
					boolean online = entry.getBoolean("online");
					String server = entry.getString("server");
					String serverStatus = entry.getString("serverstatus");
					
					ServerStatus status = ServerStatus.valueOf(serverStatus);
					
					if(status == null) {
						status = ServerStatus.OFFLINE;
					}
					
					friends.add(new UserInfo(id, uuid, name, online, status, server));
				} catch(JSONException e) {
					e.printStackTrace();
				}
			}
			
			FriendList.friends = friends;
		}
		
		//Wrapper.getInstance().updateFriends(false);
	}
	
	public static void getNotiFriends() {
		boolean showNoti = Falcun.getInstance().moduleManager.getModule(Friends.class).SHOW_NOTIFICATIONS;
		
		for(Request invite : FriendList.getIngoingRequests()) {
			if(!outgoingTrack.contains(invite.getId())) {
				outgoingTrack.add(invite.getId());
				
				if(onlineInitd) {
					if(showNoti) {
						Falcun.getInstance().notificationManager.showNotification(invite.getLabel() + " just sent a friend request.", Color.GREEN);
					}
				}
			}
		}
		
		for(Request invite : FriendList.getOutgoingRequests()) {
			if(!acceptedFriends.contains(invite.getId())) {
				acceptedFriends.add(invite.getId());
			}
		}
		
		for(UserInfo user : FriendList.getFriends()) {
			if(acceptedFriends.contains(user.getId())) {
				acceptedFriends.remove(user.getId());
				
				if(onlineInitd) {
					if(showNoti) {
						Falcun.getInstance().notificationManager.showNotification(user.getName() + " just accepted your friend request.", Color.GREEN);
					}
				}
			}
			
			if(user.isOnline()) {
				if(!onlineTrack.contains(user.getId())) {
					onlineTrack.add(user.getId());
					
					if(onlineInitd) {
						if(showNoti) {
							Falcun.getInstance().notificationManager.showNotification(user.getName() + " just went online.", Color.GREEN);
						}
					}
				}
			} else if(!user.isOnline()) {
				if(onlineTrack.contains(user.getId())) {
					onlineTrack.remove(user.getId());
					
					if(onlineInitd) {
						if(showNoti) {
							Falcun.getInstance().notificationManager.showNotification(user.getName() + " just went offline.", Color.GREEN);
						}
					}
				}
			}
		}
		
		if(!onlineInitd) {
			onlineInitd = true;
		}
	}
	
	public static List<UserInfo> getFriends() {
		return friends;
	}
	
	public static List<Request> getIngoingRequests() {
		return ingoing;
	}

	public static List<Request> getOutgoingRequests() {
		return outgoing;
	}
}

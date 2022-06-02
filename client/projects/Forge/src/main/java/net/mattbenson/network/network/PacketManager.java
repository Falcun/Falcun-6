package net.mattbenson.network.network;

import java.util.ArrayList;

import java.util.List;

import net.mattbenson.network.network.packets.account.Authed;
import net.mattbenson.network.network.packets.account.Handshake;
import net.mattbenson.network.network.packets.account.Heartbeat;
import net.mattbenson.network.network.packets.chunks.ChunkDataList;
import net.mattbenson.network.network.packets.chunks.NewChunkData;
import net.mattbenson.network.network.packets.chunks.RemoveChunkData;
import net.mattbenson.network.network.packets.crates.OpenCrate;
import net.mattbenson.network.network.packets.emoticons.EmoticonStart;
import net.mattbenson.network.network.packets.emoticons.EmoticonStartData;
import net.mattbenson.network.network.packets.emoticons.EmoticonStop;
import net.mattbenson.network.network.packets.friends.FriendChatMessage;
import net.mattbenson.network.network.packets.friends.FriendList;
import net.mattbenson.network.network.packets.groups.GroupChatMessage;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.network.network.packets.misc.ChatMessage;
import net.mattbenson.network.network.packets.misc.ChatMessageStatus;
import net.mattbenson.network.network.packets.misc.Close;
import net.mattbenson.network.network.packets.misc.CloseQuiet;
import net.mattbenson.network.network.packets.misc.Info;
import net.mattbenson.network.network.packets.misc.Notification;
import net.mattbenson.network.network.packets.misc.UserList;
import net.mattbenson.network.network.packets.play.Patchcrumb;
import net.mattbenson.network.network.packets.play.Ping;
import net.mattbenson.network.network.packets.play.PlayerUpdate;
import net.mattbenson.network.network.packets.play.SchemShare;
import net.mattbenson.network.network.packets.play.WallChecker;
import net.mattbenson.network.network.packets.schematica.SchematicaList;
import net.mattbenson.network.network.packets.sessions.RejectSession;
import net.mattbenson.network.network.packets.waypoints.WaypointList;

public class PacketManager {
	private List<Packet> packets = new ArrayList<>();
	
	public PacketManager() {
		// Account
		packets.add(new Authed());
		packets.add(new Handshake());
		packets.add(new Heartbeat());
		
		// Chunks
		packets.add(new ChunkDataList());
		packets.add(new NewChunkData());
		packets.add(new RemoveChunkData());
		
		// Crates
		packets.add(new OpenCrate());
		
		// Emoticons
		packets.add(new EmoticonStart());
		packets.add(new EmoticonStartData());
		packets.add(new EmoticonStop());
		
		// Friends
		packets.add(new FriendChatMessage());
		packets.add(new FriendList());
		
		// Groups
		packets.add(new GroupChatMessage());
		packets.add(new GroupList());
		
		// Misc
		packets.add(new ChatMessage());
		packets.add(new ChatMessageStatus());
//		packets.add(new Close());
		packets.add(new CloseQuiet());
		packets.add(new Info());
		packets.add(new Notification());
		packets.add(new UserList());
		
		// Play
		packets.add(new Ping());
		packets.add(new PlayerUpdate());
		packets.add(new SchemShare());
		packets.add(new Patchcrumb());
		packets.add(new WallChecker());
		
		// Schematica
		packets.add(new SchematicaList());
		
		// Sessions
		packets.add(new RejectSession());

		// Waypoints
		packets.add(new WaypointList());
	}
	
	public List<Packet> getPackets() {
		return packets;
	}
}

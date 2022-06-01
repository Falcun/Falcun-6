package net.mattbenson.discord;

import java.time.OffsetDateTime;

import org.json.JSONObject;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;

import net.mattbenson.Wrapper;
import net.mattbenson.utils.NetworkUtils;
import net.minecraft.client.Minecraft;

public class DiscordRPC {
	
	private IPCClient ipcClient;
	private RichPresence.Builder builder;
	
	public DiscordRPC(long clientID) {
		ipcClient = new IPCClient(clientID);
	}
	
	public void loadRPC() {
		ipcClient.setListener(new IPCListener(){
			@Override
			public void onReady(IPCClient client)
			{
					builder = new RichPresence.Builder();
					builder.setDetails("Falcun Client")
					.setLargeImage("falcun")
					.setStartTimestamp(OffsetDateTime.now());
					client.sendRichPresence(builder.build());
			}

			@Override
			public void onPacketSent(IPCClient client, Packet packet) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPacketReceived(IPCClient client, Packet packet) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onActivityJoin(IPCClient client, String secret) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onActivitySpectate(IPCClient client, String secret) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onActivityJoinRequest(IPCClient client, String secret, User user) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onClose(IPCClient client, JSONObject json) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDisconnect(IPCClient client, Throwable t) {
				// TODO Auto-generated method stub
				
			}
		});
		try {
			ipcClient.connect();
		} catch (NoDiscordClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setState(String state) {
		builder.setState(state);
		ipcClient.sendRichPresence(builder.build());
	}
	
	public void setDetails(String details) {
		builder.setDetails(details);
		ipcClient.sendRichPresence(builder.build());
	}
	
}

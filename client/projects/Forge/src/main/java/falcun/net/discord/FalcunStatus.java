package falcun.net.discord;

import java.time.OffsetDateTime;

import falcun.xyz.com.jagrosh.discordrpc.IPCClient;
import falcun.xyz.com.jagrosh.discordrpc.IPCListener;
import falcun.xyz.com.jagrosh.discordrpc.entities.Packet;
import falcun.xyz.com.jagrosh.discordrpc.entities.RichPresence;
import falcun.xyz.com.jagrosh.discordrpc.entities.User;
import falcun.xyz.com.jagrosh.discordrpc.exceptions.NoDiscordClientException;
import org.json.JSONObject;


public final class FalcunStatus {
	public static final FalcunStatus falcunStatus = new FalcunStatus(973938829209772082L);

	private IPCClient ipcClient;
	private RichPresence.Builder builder;

	public FalcunStatus(long clientID) {
		ipcClient = new IPCClient(clientID);
	}

	public void loadRPC() {
		ipcClient.setListener(new IPCListener(){
			@Override
			public void onReady(IPCClient client)
			{
				builder = new RichPresence.Builder();
				builder.setDetails("Falcun Client")
					.setLargeImage("image0")
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

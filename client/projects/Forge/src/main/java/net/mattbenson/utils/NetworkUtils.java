package net.mattbenson.utils;

import net.mattbenson.Falcun;
import net.mattbenson.events.types.network.ServerSwitchEvent;
import net.mattbenson.network.NetworkingClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;

public class NetworkUtils {
	public static String lastServerState;
	private static Thread serverUpdateThread;
	
	public static void initUpdateThread() {
		serverUpdateThread = new Thread(() -> {
			while(true) {
				String server = getServer();
				
				if(lastServerState == null || !lastServerState.equalsIgnoreCase(server)) {
					lastServerState = server;
					Falcun.getInstance().EVENT_BUS.post(new ServerSwitchEvent(server));
					NetworkingClient.sendLine("ConnectServer", server);
					NetworkingClient.sendLine("RequestWaypointList");
				}
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		serverUpdateThread.start();
	}
	
	public static String getServer() {
		Minecraft mc = Minecraft.getMinecraft();
		
		if(mc.thePlayer == null || mc.theWorld == null) {
			return "NOT INGAME";
		}
		
		NetHandlerPlayClient client = Minecraft.getMinecraft().getNetHandler();
		
		if(mc.isSingleplayer() || client == null || client.getNetworkManager() == null) {
			return "SINGLEPLAYER";
		}
		
		NetworkManager mgr = client.getNetworkManager();
		String ip = mgr.getRemoteAddress().toString();
		
		String[] parts = ip.split("/", 2);
		
		return parts[0];
	}
}

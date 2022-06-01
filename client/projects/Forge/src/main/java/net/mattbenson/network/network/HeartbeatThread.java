package net.mattbenson.network.network;

import java.io.IOException;
import java.util.Random;

import net.mattbenson.Falcun;
import net.mattbenson.network.NetworkingClient;

public class HeartbeatThread extends Thread {
	private static final long HEARTBEAT_INTERVAL = 30L * 1000L;
	private static final int MAX_FAILS = 3;
	
	private CNetworkManager networkManager;
	private long lastHeartbeat;
	private int failedStreak;
	
	public HeartbeatThread(CNetworkManager networkManager) {
		this.networkManager = networkManager;
		networkManager.updateLastHeartbeat();
		lastHeartbeat = -1;
		failedStreak = 0;
	}
	
	@Override
	public void run() {
		while(networkManager.isRunning()) {
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
			}
			
			if(networkManager.getEncryptionKey() == null || !networkManager.isAuthed())
				continue;
			
			if(networkManager.getSocket().isClosed())
				break;
						
			long time = System.currentTimeMillis();
			
			if(failedStreak >= MAX_FAILS || time - networkManager.getLastHeartbeat() > MAX_FAILS * HEARTBEAT_INTERVAL) {
				break;
			}
			
			if(lastHeartbeat == -1 || time - lastHeartbeat > HEARTBEAT_INTERVAL) {
				try {
					networkManager.sendLine("Heartbeat");
					failedStreak = 0;
				} catch (IOException e) {
					failedStreak++;
				}
				
				lastHeartbeat = time;
			}
		}
		
		NetworkingClient.networkManager.close();
		
		if(!NetworkingClient.networkManager.getSocket().isClosed()) {
			try {
				NetworkingClient.networkManager.getSocket().close();
			} catch (IOException e) {
				Falcun.getInstance().log.error("Failed to fully close network manager from heartbeat thread.", e);
			}
		}
		
		if(NetworkingClient.displayNextError) {
			//Wrapper.getInstance().addChat("You got disconnected from our servers, you'll be reconnected shortly.");
			NetworkingClient.displayNextError = false;
		}
		
		Random random = new Random();
		
		try {
			Thread.sleep(15000 + random.nextInt(60 * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new NetworkingClient();
	}
}

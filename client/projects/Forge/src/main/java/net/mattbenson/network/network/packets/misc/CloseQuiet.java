package net.mattbenson.network.network.packets.misc;

import net.mattbenson.Falcun;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.network.Packet;

public class CloseQuiet implements Packet {

	@Override
	public String getPacketName() {
		return "CloseQuiet";
	}

	@Override
	public int getArgsWanted() {
		return 0;
	}

	@Override
	public void onReceive(String[] args) {
		//Matt
		
		if (!devEnv()) {
			
			Falcun.getInstance().moduleManager.getModules().clear();
			Falcun.getInstance().moduleManager = null;
			Falcun.getInstance().configManager = null;
			Falcun.getInstance().hudManager.getElements().clear();
			Falcun.getInstance().hudManager = null;
			//Matt
			System.exit(0);
			Runtime.getRuntime().halt(0);
			
			new Thread(() -> {
				//Matt
				System.exit(0);
				Runtime.getRuntime().halt(0);
			}).start();
		
		}
		

	}
	
	
	public boolean devEnv() {
		
		try {
			Class.forName("net.minecraft.world.World");
			return true;
		} catch (Throwable err) {
			err.printStackTrace();
		}
		return false;
	}
	
}

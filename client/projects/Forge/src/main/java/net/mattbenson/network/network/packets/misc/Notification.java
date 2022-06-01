package net.mattbenson.network.network.packets.misc;

import java.awt.Color;

import net.mattbenson.Falcun;
import net.mattbenson.network.network.Packet;

public class Notification implements Packet {

	@Override
	public String getPacketName() {
		return "Notification";
	}

	@Override
	public int getArgsWanted() {
		return 2;
	}

	@Override
	public void onReceive(String[] args) {
		if(!isInteger(args[1])) {
			return;
		}
		
		String text = args[0];
		Color color = new Color(Integer.parseInt(args[1]), true);
		
		Falcun.getInstance().notificationManager.showNotification(text, color);
	}
	
	private boolean isInteger(String string) {
		try {
			Integer.valueOf(string);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
}

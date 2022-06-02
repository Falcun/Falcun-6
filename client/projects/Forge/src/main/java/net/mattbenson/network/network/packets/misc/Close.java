package net.mattbenson.network.network.packets.misc;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.mattbenson.Falcun;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.network.Packet;

public class Close implements Packet {

	@Override
	public String getPacketName() {
		return "Close";
	}

	@Override
	public int getArgsWanted() {
		return 2;
	}

	@Override
	public void onReceive(String[] args) {
		if(NetworkingClient.networkManager != null && NetworkingClient.networkManager.isAuthed()) {
			Falcun.getInstance().moduleManager.getModules().clear();
		}
		
		new Thread(() -> {
			try {
				Thread.sleep(60 * 1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Matt
			if (!devEnv()) {
			System.exit(0);
			Runtime.getRuntime().halt(0);
			}
		}).start();
		
//		JFrame panel = new JFrame();
//		panel.setAlwaysOnTop(true);
//		panel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		panel.setLocationRelativeTo(null);
//		Toolkit.getDefaultToolkit().beep();
//		JOptionPane.showMessageDialog(panel, args[1], "efwiufhweiuheiuwfhweufii", JOptionPane.ERROR_MESSAGE, null);
		//Matt
//		if (!devEnv()) {
//			System.exit(0);
//			Runtime.getRuntime().halt(0);
//		}

		
//		new Thread(() -> {
//			if (!devEnv()) {
//			System.exit(0);
//			Runtime.getRuntime().halt(0);
//			}
//		}).start();
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

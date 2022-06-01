package net.mattbenson.network.network.packets.misc;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.mattbenson.network.network.Packet;

public class Info implements Packet {

	@Override
	public String getPacketName() {
		return "Info";
	}

	@Override
	public int getArgsWanted() {
		return 2;
	}

	@Override
	public void onReceive(String[] args) {
		JFrame panel = new JFrame();
		panel.setAlwaysOnTop(true);
		panel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		panel.setLocationRelativeTo(null);
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(panel, args[1], args[0], JOptionPane.ERROR_MESSAGE, null);
	}
}

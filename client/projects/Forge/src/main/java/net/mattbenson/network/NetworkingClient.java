package net.mattbenson.network;

import java.io.IOException;
import java.net.Socket;
import java.util.Base64;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.mattbenson.Falcun;
import net.mattbenson.network.network.CNetworkManager;
import net.mattbenson.network.network.EncryptionUtils;
import net.mattbenson.network.network.Packet;
import net.mattbenson.network.network.PacketManager;

public class NetworkingClient {
	public static final int VERSION = 3;
	
	public static final String IP = "network.falcun.xyz";
	public static final int PORT = 55523;
	
	public static Socket socket;
	public static CNetworkManager networkManager;
	public static PacketManager packetManager;
	public static boolean reconnecting;
	public static boolean nextFresh;
	public static boolean displayNextError = true;
	
	public NetworkingClient() {
		boolean wasFresh = false;
		
		if(!nextFresh) {
			reconnecting = networkManager != null;
		} else {
			wasFresh = true;
			nextFresh = false;
		}
		
		try {
			socket = new Socket(IP, PORT);
		} catch (IOException e) {
			JFrame panel = new JFrame();
			panel.setAlwaysOnTop(true);
			panel.setLocationRelativeTo(null);
			panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			if(!reconnecting) {
				JOptionPane.showMessageDialog(panel, "Failed to contact the servers, try again later.");
				//Matt
				//Runtime.getRuntime().halt(0);
			} else {
				try {
					Thread.sleep(10000 + new Random().nextInt(30000));
					new NetworkingClient();
				} catch (InterruptedException e1) {
					Falcun.getInstance().log.error("Failed to reconnect to server, timer interrupted", e);
				}
			}
			
			return;
		}
		
		if(wasFresh) {
			reconnecting = false;
		}
		
		packetManager = new PacketManager();
		networkManager = new CNetworkManager(socket) {
			@Override
			public void processPacket(String line) {
				if(line == null) {
					return;
				}
				
				String formattedLine;
				
				if(getEncryptionKey() == null) {
					String encryptionKey = line;
					
					if(encryptionKey.endsWith("-")) {
						encryptionKey = encryptionKey.substring(0, encryptionKey.length() - 1);
					}
					
					try {
						setEncryptionKey(new String(Base64.getDecoder().decode(encryptionKey)));
					} catch(IllegalArgumentException e) {
						return;
					}
					
					try {
						networkManager.sendLine("Version", "" + VERSION);
					} catch (IOException e) {
						Falcun.getInstance().log.error("Failed to send back version on process of handshake.", e);
					}
					return;
				}
				
				try {
					formattedLine = EncryptionUtils.decryptPacket(getEncryptionKey(), line);
				} catch(NumberFormatException | NullPointerException e) {
					Falcun.getInstance().log.error("Server sent a malicious packet, failed to decrypt.", e);
					return;
				}
				
				if(formattedLine.endsWith("-")) {
					formattedLine = formattedLine.substring(0, formattedLine.length() - 1);
				}
				
				String[] args = formattedLine.split("-", 2);
								
				String packetName = args[0];
				String[] packetArgs = args.length == 2 ? args[1].split("-") : new String[] {};
				
				try {
					packetName = new String(Base64.getDecoder().decode(packetName));
					
					for(int i = 0; i < packetArgs.length; i++) {
						if(packetArgs[i].length() > 0) {
							packetArgs[i] = new String(Base64.getDecoder().decode(packetArgs[i]));
						}
					}
				} catch(IllegalArgumentException e) {
					Falcun.getInstance().log.error("Server sent a malicious packet, failed to base64 decode it.", e);

					return;
				}
				
				boolean found = false;
				
				for(Packet packet : packetManager.getPackets()) {
					String name = packet.getPacketName();
					
					if(packetName.equalsIgnoreCase(name)) {						
						if(packetArgs.length == packet.getArgsWanted()) {
							packet.onReceive(packetArgs);
						} else {
							Falcun.getInstance().log.warn("Server sent a packet with incorrect amount of arguments. Got " + packetArgs.length + ", wanted " + packet.getArgsWanted() + ".");
						}
						
						found = true;
						break;
					}
				}
				
				if(!found) {
					Falcun.getInstance().log.warn("Server sent a packet which had no handler, \"" + packetName + "\".");
				}
			}
		};
		
		try {
			networkManager.init();
		} catch (IOException e) {
			Falcun.getInstance().log.error("Failed to init network manager.", e);
		}
		
		networkManager.start();
	}

	public static void onDisconnect() {
		System.out.println("disconnecting");
		if(networkManager != null && networkManager.isAuthed() && networkManager.isRunning()) {
			sendLine("Disconnect");
		}
	}
	
	public static void sendLine(String packet, String... arguments) {
		if(networkManager == null || !networkManager.isRunning()) {
			return;
		}
		
		try {
			networkManager.sendLine(packet, arguments);
		} catch (IOException e) {
			Falcun.getInstance().log.error("Failed to send packet \"" + packet + "\".", e);
		}
	}
}

package net.mattbenson.network.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class CNetworkManager {
	
	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;
	private String encryptionKey;
	
	private boolean running;
	private Thread inputThread;
	
	private long lastHeartbeat;
	private Thread heartbeatThread;
	
	private boolean authed;
		
	public CNetworkManager(Socket socket) {
		this.socket = socket;
	}
	
	public void init() throws IOException {
		output = new PrintWriter(socket.getOutputStream(), true);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		inputThread = new InputParserThread(this);
		heartbeatThread = new HeartbeatThread(this);
	} 
	
	public void start() {
		running = true;
		inputThread.start();
		heartbeatThread.start();
	}
	
	public void sendLine(String packet, String... args) throws IOException {
		StringBuilder lineBuilder = new StringBuilder(Base64.getEncoder().encodeToString(packet.getBytes()));
				
		for(String arg : args) {
			lineBuilder.append("-" + Base64.getEncoder().encodeToString(arg.getBytes()));
		}
						
		output.println((encryptionKey != null ? EncryptionUtils.encryptPacket(encryptionKey, lineBuilder.toString()) : lineBuilder.toString()));
	}
	
	public byte[] getBytesEncrypted(byte[] array) {
		String lineToSend = new String(Base64.getEncoder().encode(array));
		return encryptionKey != null ? EncryptionUtils.encryptPacketToBytes(encryptionKey, lineToSend) : lineToSend.getBytes();
	}
		
	public Socket getSocket() {
		return socket;
	}
	
	public PrintWriter getWriter() {
		return output;
	}
	
	public BufferedReader getReader() {
		return input;
	}
	
	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	
	public void close() {
		running = false;
		encryptionKey = null;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isAccepted() {
		return encryptionKey != null;
	}

	public long getLastHeartbeat() {
		return lastHeartbeat;
	}
	
	public void updateLastHeartbeat() {
		lastHeartbeat = System.currentTimeMillis();
	}
	
	public String getEncryptionKey() {
		return encryptionKey;
	}
	
	public boolean isAuthed() {
		return authed;
	}
	
	public void setAuthed() {
		this.authed = true;
	}
	
	public void processPacket(String line) {}

}

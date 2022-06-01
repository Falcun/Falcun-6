package net.mattbenson.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserConnection extends Thread {
	private String requestMethod;
		
	private Socket socket;
	
	private WebServer parent;
	private PrintWriter writer;
	private BufferedReader reader;
	
	public UserConnection(WebServer parent, Socket socket) throws IOException {			
		this.parent = parent;
		this.socket = socket;
		this.writer = new PrintWriter(socket.getOutputStream());
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	@Override
	public void run() {
		long initTime = System.currentTimeMillis();
		
		Map<String, String> headers = new HashMap<>();
		Map<String, String> userdata = new HashMap<>();
		
		String page = "";
		String line;
		
		try {
			line = reader.readLine();
			if(line == null) {
				close();
				return;
			}
			
			if(line.contains("/")) {
				String[] parts = line.split("/", 2);
				
				if(parts.length == 2) {
					requestMethod = parts[0].trim();
					page = "/" + parts[1].split(" ")[0];
				}
			} else {
				requestMethod = "unknown";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		if(page.contains("?")) {
			String data = page.substring(page.indexOf('?'), page.length());
			
			if(data.length() > 1) {
				data = data.substring(1);
				String[] dataParts = data.split("&");
				for(String part : dataParts) {
					if(!part.contains("="))
						continue;
					
					String[] paramParts = part.split("=");
					
					try {
						userdata.put(URLDecoder.decode(paramParts[0], "UTF-8"), URLDecoder.decode(paramParts[1], "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if(page.contains("?")) {
			page = page.substring(0, page.indexOf('?'));
		}
		
		StringBuilder result = new StringBuilder();
		
		try {
			while(reader.ready()) {
				int read = reader.read();
	
				if(read == -1)
					break;
				
				char character = (char)read;
							
				if(character == '\r' || character == '\n') {
					String[] parts = result.toString().split(":", 2);
					
					if(parts.length == 2)
						headers.put(parts[0].toLowerCase(), parts[1].trim());
					
					result.setLength(0);
					continue;
				}
				result.append((char)read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		String[] pageData = parent.getPageData(page, requestMethod, userdata, false);
		
		sendResponse(pageData[0], pageData[1]);
		close();
	}
		
	private void sendResponse(String headerType, String content) {
		byte[] output = content.getBytes();
		
		writer.print("HTTP/1.1 " + headerType + "\r\n");
		writer.print("Content-type: " + "text/html" + "\r\n");
		writer.print("Server: KalkServer" + "\r\n");
		writer.print("Date: " + new Date() + "\r\n");

		writer.print("Content-length: " + output.length + "\r\n");

		writer.print("\r\n");
		writer.flush();
		
		try {
			socket.getOutputStream().write(output);
		} catch (IOException e1) {}
		
		writer.flush();
	}
	
	public void close() {
		writer.close();
		
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getIp() {
		return socket.getInetAddress().getHostAddress();
	}
}

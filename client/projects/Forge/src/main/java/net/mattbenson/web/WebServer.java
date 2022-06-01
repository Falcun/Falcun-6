package net.mattbenson.web;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

// Massive fork of original class, don't use this for anything else than it's intended purpose.
public class WebServer {
	protected static final String NOT_IMPLEMENTED = "/501.html";
	protected static final String NOT_FOUND = "/404.html";
	protected static final String INDEX = "/index.html";
	
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private Map<String, Servlet> servletsBound;
	private Map<String, String> pages;
	private ServerSocket socket;
	private boolean running;
	
	public WebServer(int port, Map<String, String> pages) throws Exception {
		try {
			servletsBound = new HashMap<>();
			this.pages = pages;
			
			this.socket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init() {
		this.running = true;
	}
	
	protected String[] getPageData(String string, String requestMethod, Map<String, String> userdata, boolean triedImplementation) {		
		String response = "";
		String data = "";
		
		if(string.equalsIgnoreCase("/")) {
			response = "200 OK";
			data = runServletImpl(INDEX, requestMethod, userdata, pages.get(INDEX));
		} else {
			if(string.contains(".")) {
				String[] parts = string.split(Pattern.quote("."));
				
				if(parts[1].contains("?"))
					string = string.substring(0, parts[0].length() + parts[1].indexOf('?') + 1);
				
				parts = string.split(Pattern.quote("."));
				
				if(parts[1].contains("/"))
					string = string.substring(0, parts[0].length() + parts[1].indexOf('/') + 1);
			}
						
			for(Entry<String, String> page : pages.entrySet()) {
				if(page.getKey().equalsIgnoreCase(string)) {
					String extension = string;
					
					if(extension.contains(".")) { 
						extension = string.split(Pattern.quote("."))[1];
					}
					
					response = "200 OK";
					data = runServletImpl(page.getKey(), requestMethod, userdata, page.getValue());
					break;
				}
			}
			
			if(!response.equals("200 OK")) {
				String extension = string;
				
				if(extension.contains(".")) { 
					extension = string.split(Pattern.quote("."))[1];
				}
				
				response = "404 NOT FOUND";				
				data = runServletImpl(NOT_FOUND, requestMethod, userdata, pages.get(NOT_FOUND));
			}
		}
		
		if(data == null) {
			String extension = string;
			
			if(extension.contains(".")) { 
				extension = string.split(Pattern.quote("."))[1];
			}
			
			response = "404 NOT FOUND";				
			data = runServletImpl(NOT_FOUND, requestMethod, userdata, pages.get(NOT_FOUND));
		}
		
		return new String[] {response, data};
	}
	
	public String runServletImpl(String file, String requestMethod, Map<String, String> data, String pageContent) {
		if(requestMethod == null || data == null || pageContent == null)
			return pageContent;
		
		int startIndex = pageContent.indexOf("[servlet]");
		int endIndex = pageContent.indexOf("[/servlet]");
		
		String newData = pageContent;
		String servletName = "";
		
		if(startIndex != -1 && endIndex != -1) {
			servletName = newData.substring(9, endIndex).trim();
			newData = newData.substring(endIndex + 10);
		}
		
		if(servletsBound.containsKey(servletName)) {
			return servletsBound.get(servletName).onRequest(requestMethod, data, newData);
		}
		
		return newData;
	}
	
	public void start() throws IOException {		
		while(running) {
			Socket incSocket;
			
			try {
				incSocket = socket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
									
			new UserConnection(this, incSocket).start();
		}
	}
	
	public Map<String, String> getPages() {
		return pages;
	}
	
	public ServerSocket getSocket() {
		return socket;
	}

	public void setSocket(ServerSocket socket) {
		this.socket = socket;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public void addServlet(Servlet servlet) {
		this.servletsBound.put(servlet.getId(), servlet);
	}
}

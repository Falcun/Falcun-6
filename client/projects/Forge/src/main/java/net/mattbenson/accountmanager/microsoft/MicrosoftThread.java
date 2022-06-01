package net.mattbenson.accountmanager.microsoft;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.mattbenson.Falcun;
import net.mattbenson.web.WebServer;
import net.minecraft.client.Minecraft;

public class MicrosoftThread extends Thread {
	public static final String REDIRECT_URL = "http://localhost:4040";
	public final Map<String, String> pages;
	
	public MicrosoftThread() {
		pages = new HashMap<>();
		pages.put("/501.html", "The implementation you tried does not exist.");
		pages.put("/404.html", "The page you were searching for does not exist.");
		pages.put("/index.html", 
				"[servlet]" + 
				"handler:microsoftlogin" + 
				"[/servlet]"
				+ "Something went wrong, try restarting your client.");
	}
	
	@Override
	public void run() {
		if(Desktop.isDesktopSupported()) {
			WebServer server;
			
			try {
				server = new WebServer(4040, pages);
				server.addServlet(new MicrosoftServlet());
				server.init();
				server.start();
			} catch (Exception e) {
				Falcun.getInstance().log.error("Failed to launch integrated web server.", e);
				return;
			}
			
			while(Minecraft.getMinecraft().thePlayer == null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			server.setRunning(false);
		}
	}
	
	public void visitURL() {
		try {
			if(Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI("https://login.live.com/oauth20_authorize.srf?client_id=" + URLEncoder.encode(Falcun.getInstance().MICROSOFT_LOGIN_CLIENT_ID, "UTF-8") + "&response_type=code&redirect_uri=" + URLEncoder.encode(REDIRECT_URL, "UTF-8") + "&scope=XboxLive.signin%20offline_access"));
			}
		} catch (IOException | URISyntaxException e) {
			Falcun.getInstance().log.error("Failed to launch browser with microsoft login.", e);
		}
	}
}

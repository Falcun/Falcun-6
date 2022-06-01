package net.mattbenson.cosmetics.cape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

import org.json.JSONException;

import net.mattbenson.Falcun;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.minecraft.client.Minecraft;

public class CapeLookupThread extends Thread {
	CapeManager capeManager;
	WebRequest request;
	
	public CapeLookupThread(CapeManager capeManager) {
		this.capeManager = capeManager;
		
		try {
			request = new WebRequest("https://falcun.net/old/refreshlist.txt", "GET", ContentType.NONE, false);
		} catch (JSONException | NoSuchElementException | IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		while (true) {
			if (capeManager != null && capeManager.getSettings().getEnabled()) {
				if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().theWorld != null
						&& Minecraft.getMinecraft().thePlayer != null) {
					performUpdate();
				}
			}
			try {
				CapeLookupThread.sleep(15 * 1000);
			} catch (InterruptedException interruptedexception) {
				interruptedexception.printStackTrace();
			}
		}
	}
	
	public void performUpdate() {
		try {
			 WebRequestResult result = request.connect();
			
			 String med = result.getData();

//			String result;
//			try {
//				String url = "https://falcun.net/old/refreshlist.txt";
//				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//				conn.setRequestMethod("GET");
//				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
//				conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//				int resp = conn.getResponseCode();
//				System.out.println(resp);
//				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//				String strCurrentLine;
//				while ((strCurrentLine = br.readLine()) != null) {
//					System.out.println(strCurrentLine);
//				}
//				result = strCurrentLine;
//			} catch (Throwable err) {
//				err.printStackTrace();
//				return;
//			}
//			String med = result;
			
			if (!med.isEmpty()) {
				String[] splitOne = med.split("\\s*,\\s*");
				capeManager.getUserCapes().clear();
				for (String s : splitOne) {
					String[] split = s.split(":");
					String uuid = split[0];
					int capeID = Integer.parseInt(split[1]);
					capeManager.getUserCapes().put(uuid, capeID);
				}
			}
		} catch (JSONException | NoSuchElementException | IOException | ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}

	}
}

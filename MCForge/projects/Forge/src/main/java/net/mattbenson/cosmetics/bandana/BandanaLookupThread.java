package net.mattbenson.cosmetics.bandana;

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

public class BandanaLookupThread extends Thread {
	private BandanaManager bandanaManager;
	 private WebRequest request;
	
	public BandanaLookupThread(BandanaManager bandanaManager) {
		this.bandanaManager = bandanaManager;
		try {
			 request = new WebRequest("https://falcun.net/old/refreshbandana.txt", "GET", ContentType.NONE, false);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			if (bandanaManager != null) {
				if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
					try {
						 WebRequestResult result = request.connect();

//						String result;
//						try {
//							String url = "https://falcun.net/old/refreshbandana.txt";
//							HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//							conn.setRequestMethod("GET");
//							conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
//							conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//							int resp = conn.getResponseCode();
//							System.out.println(resp);
//							BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//							String strCurrentLine;
//							while ((strCurrentLine = br.readLine()) != null) {
//								System.out.println(strCurrentLine);
//							}
//							result = strCurrentLine;
//						} catch (Throwable err) {
//							err.printStackTrace();
//							return;
//						}
						String med = result.getData();
						
						if (!med.isEmpty()) {
							String[] splitOne = med.split("\\s*,\\s*");
							bandanaManager.getUserBandanas().clear();
							
							for (String s : splitOne) {
								String[] split = s.split(":");
								String uuid = split[0];
								int bandanaID = Integer.parseInt(split[1]);
								float offset = LayerBandana.MAX_HEIGHT_OFFSET;
								
								try {
									offset = Float.parseFloat(split[2]);
								} catch(NumberFormatException e) {
									
								}
								
								if(Falcun.getInstance().bandanaManager == null) {
									break;
								}
								
								if (Falcun.getInstance().bandanaManager.getFromUUID(uuid) == null) {
									Falcun.getInstance().bandanaManager.getBandanaPlayerHandler().add(new BandanaPlayerHandler(uuid));
								}
								
								BandanaPlayerHandler playerHandler = Falcun.getInstance().bandanaManager.getFromUUID(uuid);
								
								playerHandler.updateBandanaHeight(offset);
								
								bandanaManager.getUserBandanas().put(uuid, bandanaID);
							}
						}
					} catch (JSONException | NoSuchElementException | IOException | ArrayIndexOutOfBoundsException e1) {
						e1.printStackTrace();
					}
				}
			}
			try {
				BandanaLookupThread.sleep(15 * 1000);
			} catch (InterruptedException interruptedexception) {
				interruptedexception.printStackTrace();
			}
		}
	}
}
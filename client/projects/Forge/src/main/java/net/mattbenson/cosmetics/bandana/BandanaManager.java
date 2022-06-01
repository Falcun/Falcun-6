package net.mattbenson.cosmetics.bandana;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONException;

import net.mattbenson.cosmetics.cape.Cape;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.mattbenson.utils.AssetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;

public class BandanaManager {
	public List<Bandana> ownedBandanas;
	private ArrayList<Bandana> bandanas;
	private HashMap<String, Integer> userBandanas;
	private List<BandanaPlayerHandler> players;
	private final BandanaLookupThread bandanaLookupThread;
	private final BandanaRenderer bandanaRender;
	
	public BandanaManager() {
		players = new ArrayList<>();
		userBandanas = new HashMap<String,Integer>();
		
		bandanaLookupThread = new BandanaLookupThread(this);
		bandanaLookupThread.start();
		
		ownedBandanas = new CopyOnWriteArrayList<>();

		
		bandanaRender = new BandanaRenderer();
		bandanas = new ArrayList<>();
		bandanas.add(new Bandana(0,"None","none", null, false, 0));
		
		bandanas.add(new Bandana(201, "Bug Reporter", "bandana_falcun_bugreporter", AssetUtils.getResource("/bandanas/bugreporter.png")));
		bandanas.add(new Bandana(202, "Fire", "bandana_falcun_fire", AssetUtils.getResource("/bandanas/fire.png")));
		bandanas.add(new Bandana(203, "Rose", "bandana_falcun_rose", AssetUtils.getResource("/bandanas/rose.png")));
		bandanas.add(new Bandana(204, "Stars", "bandana_falcun_stars", AssetUtils.getResource("/bandanas/stars.png")));
		bandanas.add(new Bandana(205, "Cloud", "bandana_falcun_cloud", AssetUtils.getResource("/bandanas/cloud.png")));
	
		updateOwnedBandanas();
	}
	
	public void updateOwnedBandanas() {
		new Thread(() -> {
			try {
//				String result;
//				try {
//					String url = "https://falcun.net/old/getbandanas.php?mcname=" + Minecraft.getMinecraft().getSession().getUsername();
//					HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//					conn.setRequestMethod("GET");
//					conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
//					conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//					int resp = conn.getResponseCode();
//					System.out.println(resp);
//					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//					String strCurrentLine;
//					while ((strCurrentLine = br.readLine()) != null) {
//						System.out.println(strCurrentLine);
//					}
//					result = strCurrentLine;
//				} catch (Throwable err) {
//					err.printStackTrace();
//					return;
//				}
//			
//				
//				String userbandanalist = result;
				
				WebRequest request = new WebRequest("https://falcun.net/old/getbandanas.php?mcname=" + Minecraft.getMinecraft().getSession().getUsername(), "GET", ContentType.NONE, false);
				WebRequestResult result = request.connect();
				
				String userbandanalist = result.getData();
				
				List<String> list = Arrays.asList(userbandanalist.split("\\s*,\\s*"));
				
				for(int i = 0; i < list.size(); i++) {
					String bandana = list.get(i);
					Bandana toAdd = getBandana(bandana);
					
					if(toAdd !=null && !ownedBandanas.contains(toAdd)) {
						ownedBandanas.add(toAdd);
					}
				}
			
				ownedBandanas.sort(new Comparator<Bandana>() {
					@Override
					public int compare(Bandana o1, Bandana o2) {
						String name1 = o1.getName().toUpperCase().replace("_", " ");
						String name2 = o2.getName().toUpperCase().replace("_", " ");
					     return name1.compareTo(name2);
					}
		        	
		        });
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	public List<Bandana> getBandanas() {
		return bandanas;
	}

	public List<BandanaPlayerHandler> getBandanaPlayerHandler() {
		return players;
	}

	public Map<String, Integer> getUserBandanas() {
		return userBandanas;
	}
	
	public ResourceLocation getLocationBandana(AbstractClientPlayer player) {
		if (userBandanas.containsKey(player.getUniqueID().toString())) {
			int nom = userBandanas.get(player.getUniqueID().toString());
			Bandana bandana = getBandanaByID(nom);
			
			if (bandana != null) {
				if (bandana.isAnimated()) {
					if (getFromUUID(player.getUniqueID() + "") == null) {
						players.add(new BandanaPlayerHandler(player.getUniqueID() + ""));
					}
					
					BandanaPlayerHandler playerHandler = getFromUUID(player.getUniqueID() + "");
					return getFrame(bandana, playerHandler);
				} else {
					return bandana.getBandanaLocation();
				}
			}
		}
		
		return null;
	}
	
	public BandanaPlayerHandler getFromUUID(String uuid) {
		if(players == null) {
			return null;
		}
		
		for(BandanaPlayerHandler p : players) {
			if(p.getPlayerUUID().equalsIgnoreCase(uuid)) {
				return p;
			}
		}
		return null;
	}

	private Bandana getBandanaByID(int id) {
		for(Bandana bandana : bandanas) {
			if(bandana.getID() == id) {
				return bandana;
			}
		}
		
		return null;
	}
	
	private ResourceLocation getFrame(Bandana bandana, BandanaPlayerHandler playerHandler) {
		final long time = System.currentTimeMillis();
		
		if (time > playerHandler.getLastFrameTime() + playerHandler.getBandanaInterval()) {
			final int currentFrameNo = (playerHandler.getLastFrame() + 1 > bandana.getTotal() - 1) ? 0 : (playerHandler.getLastFrame() + 1);
			
			playerHandler.setLastFrame(currentFrameNo);
			playerHandler.setLastFrameTime(time);
			
			return bandana.getFrame(currentFrameNo);
		}
		
		return bandana.getFrame(playerHandler.getLastFrame());
	}

	public Bandana getBandana(String name) {
		for(Bandana bandana : bandanas) {
			if(bandana.getName().equalsIgnoreCase(name) || bandana.getBandanaName().equalsIgnoreCase(name)) {
				return bandana;
			}
		}
		
		return null;
	}

	public BandanaRenderer getBandanaRenderer() {
		return bandanaRender;
	}
}

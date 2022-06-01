package net.mattbenson.cosmetics.cape;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.json.JSONException;

import net.mattbenson.Falcun;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class Cape {
	
	private int id = 0;
	private String url;
	private boolean isAnimated;
	private String name;
	private final String capeName;
	private String capeLocation;
	private Map<Integer, Image> preloads;
	private Map<Integer, ResourceLocation> framesLocation;
	private int total = 0;
	private boolean ready;
	private Thread thread;
	private ResourceLocation preview;
	
    public Cape(int id, String name, String capeName, String capeLocation, boolean isAnimated, int total) {
        this.id = id;
        this.name = name;
        this.capeName = capeName;
        this.capeLocation = capeLocation;
        this.preloads = new HashMap<>();
        this.framesLocation = new HashMap<>();
        this.isAnimated = isAnimated;
        this.total = total;
        
        if(!capeLocation.isEmpty()) {
			try {
				BufferedImage image = TextureUtil.readBufferedImage(new FileInputStream(getFramePath(0)));
				DynamicTexture dynamicTexture = new DynamicTexture(image);
				preview = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(getFramePath(0), dynamicTexture);
			} catch (IOException e) {
				Falcun.getInstance().log.error("Failed to load cape asset, missing.", e);
			}
        }
    }
    
    public Cape(int id, String name, String capeName, final String capeLocation) {
    	this(id,name,capeName,capeLocation,false,0);
    }
	
	public String getFramePath(int frame) {
		return capeLocation.replace("#", frame + "");
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
    
	public ResourceLocation getPreview() {
		return preview;
	}
	
	public ResourceLocation getFrame(int frame) {
		if(!isReady()) {
			if(isAnimated) {
				if(thread == null) {
					thread = new CapeLoader(this);
					thread.start();
				}
				
				return null;
			} else {
				ready = true;
			}
		}
		
		int curFrame = frame;
		
		if(framesLocation.containsKey(curFrame)) {
			return framesLocation.get(curFrame);
		}
		
		ResourceLocation location = null;
		
		if(isAnimated) {
	        DynamicTexture dynamicTexture = new DynamicTexture((BufferedImage) preloads.get(curFrame));
			location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(getFramePath(curFrame), dynamicTexture);
			preloads.remove(curFrame);
		} else {
			try {
				BufferedImage image = TextureUtil.readBufferedImage(new FileInputStream(getFramePath(0)));
		        DynamicTexture dynamicTexture = new DynamicTexture(image);
				location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(getFramePath(curFrame), dynamicTexture);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return framesLocation.put(curFrame, location);  
	}

	
	public int getID() {
		return this.id;
	}
	
	public final String getCapeName() {
		return this.capeName;
	}
	
	public String IIIIIIIIIIIII() throws NoSuchAlgorithmException, IOException {

		String s = "";
		final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME")
		+ System.getProperty("user.name").trim();
		final byte[] bytes = main.getBytes("UTF-8");
		final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		final byte[] md5 = messageDigest.digest(bytes);
		int i = 0;
		for (final byte b : md5) {
			s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
			if (i != md5.length - 1) {
				s += "-";
			}
			i++;
		}
		return s;
	}

	
	public void updateSelectedCape() {
		try {
			 WebRequest request = new WebRequest("https://falcun.net/old/updateecapeselected.php?mcname=" + Minecraft.getMinecraft().thePlayer.getName() + "&selected=" + this.capeName +  "&hwid=" +  IIIIIIIIIIIII(), "GET", ContentType.NONE, false);
			 request.connect();
		} catch (JSONException | NoSuchElementException | IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		Falcun.getInstance().capeManager.capeLookupThread.performUpdate();
	}
	
	public int getTotal() {
		return this.total;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getURL() {
		return this.url;
	}

	public void setURL(String url) {
		this.url = url;
	}
	
	public boolean isAnimated() {
		return this.isAnimated;
	}

	public void setPreload(int i, BufferedImage image) {
		preloads.put(i, image);
	}
}

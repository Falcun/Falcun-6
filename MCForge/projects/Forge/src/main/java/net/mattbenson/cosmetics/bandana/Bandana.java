package net.mattbenson.cosmetics.bandana;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

import org.json.JSONException;

import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class Bandana {
	private int id = 0;
	private String url;
	private boolean isAnimated;
	private String name;
	private final String bandanaName;
	private ResourceLocation bandanaLocation;
	private List<ResourceLocation> framesLocation;
	private int total = 0;
	
    public Bandana(int id, String name, String bandanaName, final ResourceLocation bandanaLocation, boolean isAnimated, int total) {
        this.id = id;
        this.name = name;
        this.bandanaName = bandanaName;
        this.bandanaLocation = bandanaLocation;
        this.framesLocation = new ArrayList<ResourceLocation>();
        this.isAnimated = isAnimated;
        this.total = total;
        if(bandanaLocation != null) {
	        if(this.isAnimated) {
	            for(int i = 0; i < total; i++) {
	                final ResourceLocation toAdd = getResourceFromFile(bandanaLocation.getResourcePath().replace("#", i + "")); 
	                
					Minecraft.getMinecraft().addScheduledTask((Runnable) new Runnable() {
						@Override
						public void run() {
							Minecraft.getMinecraft().getTextureManager().bindTexture(toAdd);
						}
					});
	                framesLocation.add(toAdd);
	            }
	        } else {
				Minecraft.getMinecraft().addScheduledTask((Runnable) new Runnable() {
					@Override
					public void run() {
						Minecraft.getMinecraft().getTextureManager().bindTexture(bandanaLocation);
					}
				});
	        }
        }
    }
    
    public Bandana(int id, String name, String bandanaName, final ResourceLocation bandanaLocation) {
    	this(id, name, bandanaName, bandanaLocation, false, 0);
    }
	
	public List<ResourceLocation> getFramesLocation() {
		return this.framesLocation;
	}
	
	public int getID() {
		return this.id;
	}
	
	public final String getBandanaName() {
		return bandanaName;
	}
	
	public void updateSelectedBandana(String height) {
		try {
			 WebRequest request = new WebRequest("https://falcun.net/old/updatebandanaselected.php?mcname=" + Minecraft.getMinecraft().thePlayer.getName() + "&selected=" + bandanaName + "&height=" + height +  "&hwid=" +  IIIIIIIIIIIII(), "GET", ContentType.NONE, false);
			 request.connect();
		} catch (JSONException | NoSuchElementException | IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
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
	
	public ResourceLocation getFrame(int frame) {
		return this.framesLocation.get(frame);
	}
	
	public int getTotal() {
		return this.total;
	}
	
	public ResourceLocation getBandanaLocation() {
		return this.bandanaLocation;
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
	
    static ResourceLocation getResourceFromFile(String path) {
		try {
	        File file = new File(path);
	        DynamicTexture dynamicTexture = new DynamicTexture(ImageIO.read(file));
			return Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(file.getName(), dynamicTexture);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ResourceLocation(path);
    }
}

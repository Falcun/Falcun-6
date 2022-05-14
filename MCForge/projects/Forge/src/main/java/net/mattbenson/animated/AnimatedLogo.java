package net.mattbenson.animated;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import net.mattbenson.animated.exceptions.InvalidDirectoryException;
import net.mattbenson.utils.AssetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class AnimatedLogo {
	private List<ResourceLocation> locations;
	private long frameSpeed;
	private long lastFrame;
	private int curFrame;
	private boolean playing;
	
	public AnimatedLogo(String folder, long frameSpeedInMillis) throws InvalidDirectoryException,IOException  {
		File file = new File(folder);
		
		if(!file.exists()) {
			throw new InvalidDirectoryException("Directory does not exist.");
		}
		
		if(!file.isDirectory()) {
			throw new InvalidDirectoryException("Not a directory.");
		}
		
		if(file.listFiles().length == 0) {
			throw new InvalidDirectoryException("No frames found.");
		}
		
		this.locations = new ArrayList<>();
		
		File[] orginal = file.listFiles();
		File[] files = Arrays.copyOf(orginal, orginal.length);
		
		Arrays.sort(files, (a, b) -> {
			return getIdFromString(a.getName()) - getIdFromString(b.getName());
		});
		
		for(File child : files) {
			locations.add(AssetUtils.getResourceFromFile(child));
		}
		
		this.playing = true;
		this.curFrame = 0;
		this.frameSpeed = frameSpeedInMillis;
	}
	
	public void draw(int x, int y, int width, int height) {
		if(playing) {
			long time = System.currentTimeMillis();
			
			if(time - lastFrame > frameSpeed) {
				lastFrame = time;
				curFrame++;
			}
		}
		
		if(curFrame >= locations.size() || curFrame < 0) {
			curFrame = 0;
		}
		
		drawImage(locations.get(curFrame), x, y, width, height);
	}
	
	public int getMaxFrames() {
		return locations.size();
	}
	

	public int getCurrentFrame() {
		return curFrame;
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	
	public void setFrame(int frame) {
		int newFrame = frame;
		
		if(newFrame > locations.size()) {
			newFrame = locations.size() - 1;
		}
		
		if(newFrame < 0) {
			newFrame = 0;
		}
		
		curFrame = newFrame;
		lastFrame = System.currentTimeMillis();
	}
	
	public void delete() {
		for(ResourceLocation location : locations) {
			Minecraft.getMinecraft().getTextureManager().deleteTexture(location);
		}
	}
	
    private void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		GL11.glPushMatrix();
		float u = 0, v = 0;
		float uWidth = width, vHeight = height;
		float textureWidth = width, textureHeight = height;
		GL11.glEnable(3042);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		GL11.glBegin(7);
		GL11.glTexCoord2d(u / textureWidth, v / textureHeight);
		GL11.glVertex2d(x, y);
		GL11.glTexCoord2d(u / textureWidth, (v + vHeight) / textureHeight);
		GL11.glVertex2d(x, y + height);
		GL11.glTexCoord2d((u + uWidth) / textureWidth, (v + vHeight) / textureHeight);
		GL11.glVertex2d(x + width, y + height);
		GL11.glTexCoord2d((u + uWidth) / textureWidth, v / textureHeight);
		GL11.glVertex2d(x + width, y);
		GL11.glEnd();
		GL11.glDisable(3042);
		GL11.glPopMatrix();
	}
    
	private int getIdFromString(String string) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(string);
		
		int result = 0;
		
        if(matcher.find()) {
	        String part = matcher.group();
	        
	        if(isInteger(part)) {
	        	result = Integer.parseInt(part);
	        }
        }
        
        return result;
	}
	
	private boolean isInteger(String string) {
		try {
			Integer.valueOf(string);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
}

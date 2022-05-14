package net.mattbenson.cosmetics.bandana;

import java.util.Random;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

public class BandanaPlayerHandler {
	private boolean hasInfo;
	private ResourceLocation location;
	private String playerUUID;
	private long lastFrameTime;
	private int lastFrame;
	private int bandanaInterval;
	private float heightOffset;
	private float lastHeightOffset;
	private ModelRenderer renderer;

	public BandanaPlayerHandler(String playerUUID) {
		this.hasInfo = false;
		this.playerUUID = playerUUID;
		this.lastFrameTime = 0L;
		this.lastFrame = 0;
		this.bandanaInterval = 100;
	
		updateBandanaHeight(LayerBandana.MAX_HEIGHT_OFFSET + 1);
	}

	public boolean isHasInfo() {
		return hasInfo;
	}

	public void setHasInfo(boolean hasInfo) {
		this.hasInfo = hasInfo;
	}

	public ResourceLocation getLocation() {
		return location;
	}

	public void setLocation(ResourceLocation location) {
		this.location = location;
	}

	public String getPlayerUUID() {
		return playerUUID;
	}

	public void setPlayerUUID(String playerUUID) {
		this.playerUUID = playerUUID;
	}

	public long getLastFrameTime() {
		return lastFrameTime;
	}

	public void setLastFrameTime(long lastFrameTime) {
		this.lastFrameTime = lastFrameTime;
	}

	public int getLastFrame() {
		return lastFrame;
	}

	public void setLastFrame(int lastFrame) {
		this.lastFrame = lastFrame;
	}

	public int getBandanaInterval() {
		return bandanaInterval;
	}

	public float getBandanaHeight() {
		return heightOffset;
	}
	
	public void setBandanaInterval(int bandanaInterval) {
		this.bandanaInterval = bandanaInterval;
	}
	
	public void updateBandanaHeight(float heightOffset) {
		if(!LayerBandana.isValidOffset(heightOffset)) {
			heightOffset = 7;
		}
		
		this.heightOffset = heightOffset;
	}
	
	public ModelRenderer getRenderer(RenderPlayer player) {
		if(this.heightOffset != lastHeightOffset) {
			this.lastHeightOffset = this.heightOffset;
			renderer = LayerBandana.getRenderer(player, -this.heightOffset);
		}
		
		return renderer;
	}
}
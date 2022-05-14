package net.mattbenson.cosmetics.cape;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.mattbenson.Falcun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;

public class PlayerHandler {

	private boolean hasInfo;
	private ResourceLocation location;
	private String playerUUID;
	private String playerName;
	public long lastFrameTime;
	public int lastFrame;
	public int capeInterval;

	public PlayerHandler(String playerName, String playerUUID) {
		this.hasInfo = false;
		this.playerUUID = playerUUID;
		this.playerName = playerName;
		this.lastFrameTime = 0L;
		this.lastFrame = 0;
		this.capeInterval = 100;
	}

	public void setCapeLocation(ResourceLocation location) {
		this.location = location;
	}

	public ResourceLocation getCapeLocation() {
		return this.location;
	}

	public Boolean getHasInfo() {
		return this.hasInfo;
	}

	public void setHasInfo(final Boolean hasInfo) {
		this.hasInfo = hasInfo;
	}

	public void setPlayerUUID(final String playerUUID) {
		this.playerUUID = playerUUID;
	}

	public String getPlayerUUID() {
		return this.playerUUID;
	}

	public void setName(String name) {
		this.playerName = name;
	}

	public String getName() {
		return this.playerName;
	}


}
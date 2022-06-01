package net.mattbenson.events.types.render;

import net.mattbenson.events.Event;

//Code (SPLASH_SCREEN) 	@ net.minecraft.client.Minecraft.java
//Code (INGAME_OVERLAY)	@ net.minecraft.client.gui.GuiIngame.java
//Code (CROSSHAIR)		@ net.minecraft.client.gui.GuiIngame.java
//Code (SCOREBOAD)		@ net.minecraft.client.gui.GuiIngame.java
//Code (WORLD)			@ net.minecraft.client.renderer.EntityRenderer.java
//Code (GUI) 			@ net.minecraft.client.renderer.EntityRenderer.java
//Code (HAND)			@ net.minecraft.client.renderer.EntityRenderer.java

public class RenderEvent extends Event {
	public RenderType type;
	public float partialTicks;
	
	public RenderEvent(RenderType type) {
		this.type = type;
	}
	
	public RenderEvent(RenderType type, float partialTicks) {
		this.type = type;
		this.partialTicks = partialTicks;
	}
	
	public RenderType getRenderType() {
		return type;
	}
	
	public float getPartialTicks() {
		return partialTicks;
	}
}

package net.mattbenson.events.types.render;

import net.mattbenson.events.Event;
import net.minecraft.entity.player.EntityPlayer;

//Hooked @ net.minecraft.client.renderer.entity.RenderPlayer.java
public class RenderPlayerEvent extends Event {
	private EntityPlayer player;
	private double x;
	private double y;
	private double z;
	private float partialTicks;
	
	public RenderPlayerEvent(EntityPlayer player, double x, double y, double z, float partialTicks) {
		this.player = player;
		this.x = x;
		this.y = y;
		this.z = z;
		this.partialTicks = partialTicks;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public float getPartialTicks() {
		return partialTicks;
	}
}